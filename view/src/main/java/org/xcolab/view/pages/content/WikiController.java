package org.xcolab.view.pages.content;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.contents.ContentsClient;
import org.xcolab.client.contents.exceptions.ContentNotFoundException;
import org.xcolab.client.contents.pojo.ContentArticle;
import org.xcolab.client.contents.pojo.ContentArticleVersion;
import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.members.PermissionsClient;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.view.errors.ErrorText;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WikiController {

    @GetMapping("/wiki")
    public String home(HttpServletRequest request, HttpServletResponse response, Model model,
            Member member) {
        final long folderId = ConfigurationAttributeKey.WIKI_CONTENT_FOLDER_ID.get();

        if (folderId > 0 && PermissionsClient.canAdminAll(member)) {
            final List<ContentArticleVersion> contentArticleVersions = ContentsClient
                    .getContentArticleVersions(0, Integer.MAX_VALUE, folderId, null, null,
                            null);
            model.addAttribute("contentArticleVersions", contentArticleVersions);
        }
        return "content/wikiList";
    }

    @GetMapping("/wiki/{pageTitle}")
    public String showWikiPage(HttpServletRequest request, HttpServletResponse response,
            Model model, Member member, @PathVariable String pageTitle)
            throws ContentNotFoundException, IOException {
        final long folderId = ConfigurationAttributeKey.WIKI_CONTENT_FOLDER_ID.get();

        if (folderId > 0 && StringUtils.isNotBlank(pageTitle)) {
            final ContentArticleVersion contentArticleVersion =
                    ContentsClient.getLatestContentArticleVersion(folderId, pageTitle);
            final ContentArticle contentArticle = ContentsClient
                    .getContentArticle(contentArticleVersion.getContentArticleId());

            if (!contentArticle.canView(member)) {
                return ErrorText.ACCESS_DENIED.flashAndReturnRedirect(request);
            }
            model.addAttribute("contentArticleVersion", contentArticleVersion);
        }
        return "content/wiki";
    }

    @GetMapping("/resources/{contestYear}/{contestUrlName}")
    public String showResourcePage(HttpServletRequest request, HttpServletResponse response,
            Model model, @PathVariable long contestYear, @PathVariable String contestUrlName)
            throws ContestNotFoundException, IOException {

        final Contest contest = ContestClientUtil.getContest(contestUrlName, contestYear);

        try {
            if (contest.getResourceArticleId() > 0) {
                final ContentArticle contentArticle = ContentsClient
                        .getContentArticle(contest.getResourceArticleId());
                ContentArticleVersion contentArticleVersion =
                        ContentsClient.getContentArticleVersion(contentArticle.getMaxVersionId());
                model.addAttribute("contentArticleVersion", contentArticleVersion);
            }
        } catch (ContentNotFoundException e) {
            return ErrorText.NOT_FOUND.flashAndReturnRedirect(request);
        }

        return "content/wiki";
    }
}
