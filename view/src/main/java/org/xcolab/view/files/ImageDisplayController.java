package org.xcolab.view.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.files.FilesClient;
import org.xcolab.client.files.exceptions.FileEntryNotFoundException;
import org.xcolab.client.files.pojo.FileEntry;
import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@PropertySource({"file:${user.home}/.xcolab.application.properties"})
public class ImageDisplayController {

    private final String fileUploadPath;
    private final boolean isProduction;

    @Autowired
    public ImageDisplayController(Environment env) {
        fileUploadPath = env.getProperty("files.upload.dir");
        isProduction = "production".equals(env.getProperty("environment"));
    }

    @GetMapping({"/image/{whatever}", "/image"})
    public void serveImage(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "img_id", required = false) Long imgId,
            @RequestParam(required = false) Long portraitId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false, defaultValue = "NONE") DefaultImage defaultImage)
            throws IOException {
        Long imageId = null;

        if (imgId != null) {
            imageId = imgId;
        } else if (userId != null) {
            try {
                Member member =
                        MembersClient.getMember(Long.parseLong(request.getParameter("userId")));
                if (member.getPortraitFileEntryId() != null) {
                    imageId = member.getPortraitFileEntryId();
                }
            } catch (MemberNotFoundException ignored) {
            }
        }

        if (imageId == null && portraitId != null) {
            imageId = portraitId;
        }

        if (imageId != null && imageId > 0) {
            String path = request.getSession().getServletContext().getRealPath("/");
            path = (fileUploadPath != null) ? (fileUploadPath) : (path);

            try {
                FileEntry fileEntry = FilesClient.getFileEntry(imageId);
                String filePath = FilesClient.getFilePathFromFinalDestination(fileEntry, path);
                if (filePath != null) {
                    //check if file exists
                    File f = new File(filePath);
                    if (f.exists() && !f.isDirectory()) {
                        sendImageToResponse(request, response, filePath);
                        return;
                    } else {
                        if (!isProduction) {
                            response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
                            String newURL =
                                    ConfigurationAttributeKey.COLAB_URL_PRODUCTION.get() + request
                                            .getRequestURI() + "?" + request.getQueryString();
                            response.setHeader("Location", newURL);
                            return;
                        }
                    }
                }
            } catch (FileEntryNotFoundException ignored) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        }

        if (request.getRequestURI().contains("user_male_portrait")) {
            defaultImage = DefaultImage.MEMBER;
        }

        if (!defaultImage.equals(DefaultImage.NONE)) {
            response.sendRedirect(defaultImage.getImagePath());
            return;
        }
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    private void sendImageToResponse(HttpServletRequest request, HttpServletResponse response,
            String filePath) {

        try {
            ServletContext servletContext = request.getSession().getServletContext();
            String mime = servletContext.getMimeType(filePath);
            if (mime == null) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

            response.setContentType(mime);
            File file = new File(filePath);
            response.setContentLength((int) file.length());

            FileInputStream in = new FileInputStream(file);
            OutputStream out = response.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[1024];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
            out.close();
            in.close();
            return;
        } catch (IOException ignored) {

        }
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException ignored) {

        }
    }

    public enum DefaultImage {
        NONE(""),
        MEMBER("/images/user_default.png"),
        PROPOSAL("/images/proposal_default.png"),
        CONTEST("/images/proposal_default.png");

        private final String imagePath;

        DefaultImage(String imagePath) {
            this.imagePath = imagePath;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}