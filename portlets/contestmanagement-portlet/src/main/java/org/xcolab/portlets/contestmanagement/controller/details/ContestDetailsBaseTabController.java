package org.xcolab.portlets.contestmanagement.controller.details;

import com.ext.portlet.model.Contest;
import com.ext.portlet.service.ContestLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.xcolab.controller.BaseTabController;
import org.xcolab.interfaces.TabEnum;
import org.xcolab.portlets.contestmanagement.entities.ContestDetailsTabs;
import org.xcolab.wrapper.ContestWrapper;
import org.xcolab.wrapper.TabWrapper;

import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import java.util.List;


public abstract class ContestDetailsBaseTabController extends BaseTabController {

    private final static Log _log = LogFactoryUtil.getLog(ContestDetailsBaseTabController.class);
    private Contest contest;
    private ContestWrapper contestWrapper;
    protected TabWrapper tabWrapper;

    public static final String NO_PERMISSION_TAB_VIEW = "common/noPermissionTab";
    public static final String NOT_FOUND_TAB_VIEW = "common/notFound";

    @ModelAttribute("tabs")
    @Override
    public List<TabWrapper> populateTabs(Model model, PortletRequest request) throws PortalException, SystemException {
        return getAllVisibleTabsWrapped(model, request, ContestDetailsTabs.values());
    }

    @ModelAttribute("currentTabWrapped")
    @Override
    public abstract TabWrapper populateCurrentTabWrapped(PortletRequest request) throws PortalException, SystemException;

    @ModelAttribute("contestWrapper")
    public ContestWrapper populateContestWrapper(Model model, PortletRequest request){
        try {
            initContest(request);
            return contestWrapper;
        } catch (Exception e){
            _log.warn("Could not get contest: ", e);
        }
        return null;
    }

    private void initContest(PortletRequest request) throws Exception{
            Long contestId = getContestIdFromRequest(request);
        if (contestId != null) {
            contest = ContestLocalServiceUtil.getContest(contestId);
            contestWrapper = new ContestWrapper(contest);
        } else {
            throw new Exception("Severe. No contest Id provided.");
        }
    }

    public void setPageAttributes(PortletRequest request, Model model, TabEnum tab)
            throws PortalException, SystemException {

        String pageTitle = contest.getContestShortName();
        String pageSubTitle = tab.getDisplayName() + " - " + pageTitle;
        String pageDescription = "Contest details for " + pageTitle;
        setSeoTexts(request, pageTitle, pageSubTitle, pageDescription);
    }

    public static boolean getCreateNewContestParameterFromRequest(PortletRequest request){
        String createContestParameter = request.getParameter("createContest");
        return Boolean.parseBoolean(createContestParameter);
    }

    public Long getContestPK(){ return contest.getContestPK();}

    public Contest getContest() {
        return contest;
    }

    public Contest getContest(PortletRequest request) {
        if(contest != null){
            return contest;
        } else {
            try {
                initContest(request);
            } catch (Exception e){
                _log.warn("Could not get contest: ", e);
            }
        }
        return contest;
    }

    public void setContest(Contest contest) {
        this.contest = contest;
    }

    public TabWrapper getTabWrapper() {
        return tabWrapper;
    }

    public void setTabWrapper(TabWrapper tabWrapper) {
        this.tabWrapper = tabWrapper;
    }

    public ContestWrapper getContestWrapper() {
        return contestWrapper;
    }

    public void setContestWrapper(ContestWrapper contestWrapper) {
        this.contestWrapper = contestWrapper;
    }

}
