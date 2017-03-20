package org.xcolab.view.pages.contenteditor;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xcolab.client.contents.ContentsClient;
import org.xcolab.client.contents.exceptions.ContentNotFoundException;
import org.xcolab.client.contents.pojo.ContentArticleVersion;
import org.xcolab.client.contents.pojo.ContentFolder;
import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.members.PermissionsClient;
import org.xcolab.view.auth.MemberAuthUtil;
import org.xcolab.view.errors.ErrorText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ContentEditorResourceController extends BaseContentEditor {

    @GetMapping("/content-editor/resourcePagesEditor")
    public String handleRenderRequest(HttpServletRequest request, HttpServletRequest response,
            Model model) {
        long memberId = MemberAuthUtil.getMemberId(request);
        if (PermissionsClient.canAdminAll(memberId)) {
            return "contenteditor/resourcePagesEditor";
        } else {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }
    }

    @GetMapping("/content-editor/contentEditorGetLatestResourceArticleVersion")
    public void contentEditorGetLatestArticleVersion(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) Long articleId)
            throws IOException, ContentNotFoundException {

        ContentArticleVersion contentArticleVersion = ContentsClient.getLatestContentArticleVersion(articleId);

        Contest contest = ContestClientUtil.getContestByResourceArticleId(articleId);


        JSONObject articleVersion = getContentArticleVersion(articleId, contentArticleVersion,
                contest.getContestUrl(), contest.getResourceArticleUrl());

        response.getOutputStream().write(articleVersion.toString().getBytes());
    }
    private JSONObject getContentArticleVersion(Long articleId,
            ContentArticleVersion contentArticleVersion, String contestURL, String contestArticleUrl) {

        JSONArray versions = new JSONArray();
        List<ContentArticleVersion> cavs = ContentsClient
                .getContentArticleVersions(0,Integer.MAX_VALUE,null,articleId,null,null);

        JSONObject articleVersion;
        for(ContentArticleVersion cav: cavs){
            articleVersion = new JSONObject();
            articleVersion.put("createdDate",cav.getCreateDate());
            articleVersion.put("contentArticleVersionId", cav.getContentArticleVersionId());
            versions.put(articleVersion);
        }
        articleVersion = new JSONObject();
        if (contentArticleVersion != null) {
            articleVersion.put("title", contentArticleVersion.getTitle());
            articleVersion.put("folderId", contentArticleVersion.getFolderId());
            articleVersion.put("articleId", contentArticleVersion.getContentArticleId());
            articleVersion.put("content", contentArticleVersion.getContent());
            articleVersion.put("createdDate",contentArticleVersion.getCreateDate());
            articleVersion.put("versions",versions);
            articleVersion.put("contestURL",contestURL);
            articleVersion.put("contestArticleURL",contestArticleUrl);
        }
        return articleVersion;
    }
    @GetMapping("/content-editor/resourcePagesListFolder")
    public void contentEditorListFolder(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String node)
            throws IOException {


        JSONArray responseArray = new JSONArray();
        Map<String, String> yearFolders = new LinkedHashMap<>();


        if (node == null || node.isEmpty()) {//root
            List<Contest> allContests = ContestClientUtil.getAllContests();
            for (Contest c : allContests) {
                yearFolders.put(c.getContestYear().toString(), "");

            }
            List<String> yearsList = new ArrayList<>(yearFolders.keySet());
            Collections.sort(yearsList);


            for (int i = yearsList.size() - 1; i >= 0; i--) {
                String year = yearsList.get(i);
                responseArray.put(folderNode(year, year));
            }
        } else {//year
            Integer year = 2016;
            year = Integer.parseInt(node);//should be the year
            List<Contest> contestsInYear = ContestClientUtil.getAllContestsInYear(year);
            for (Contest c : contestsInYear) {
                if (c.getResourceArticleId() != null && c.getResourceArticleId() != 0l) {
                    responseArray
                            .put(articleNode(c.getContestShortName(),
                                    c.getResourceArticleId()));
                }
            }
        }

        response.getOutputStream().write(responseArray.toString().getBytes());

    }


}