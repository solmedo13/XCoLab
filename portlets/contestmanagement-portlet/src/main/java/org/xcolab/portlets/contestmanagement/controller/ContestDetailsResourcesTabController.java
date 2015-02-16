package org.xcolab.portlets.contestmanagement.controller;

import com.ext.portlet.model.ContestPhase;
import com.ext.portlet.model.ContestWrapper;
import com.ext.portlet.service.ContestPhaseLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portlet.wiki.NoSuchPageException;
import com.liferay.portlet.wiki.NoSuchPageResourceException;
import com.liferay.portlet.wiki.model.WikiPage;
import com.liferay.portlet.wiki.model.WikiPageResource;
import com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil;
import com.liferay.portlet.wiki.service.WikiPageResourceLocalServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xcolab.interfaces.TabEnum;
import org.xcolab.portlets.contestmanagement.beans.ContestResourcesBean;
import org.xcolab.portlets.contestmanagement.views.ContestDetailsTabs;
import org.xcolab.portlets.contestmanagement.wrappers.WikiPageWrapper;
import org.xcolab.wrapper.TabWrapper;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.List;

/**
 * Created by Thomas on 2/13/2015.
 */

@Controller
@RequestMapping("view")
public class ContestDetailsResourcesTabController extends ContestDetailsBaseTabController {

    @Autowired
    private Validator validator;
    static final private TabEnum tab = ContestDetailsTabs.RESOURCES;
    static final private String TAB_VIEW = "details/resourcesTab";

    private WikiPageWrapper wikiPageWrapper;


    @InitBinder("contestResourcesBean")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @ModelAttribute("currentTabWrapped")
    @Override
    public TabWrapper populateCurrentTabWrapped(PortletRequest request) throws PortalException, SystemException{
        tabWrapper = new TabWrapper(tab, request, tabContext);
        request.getPortletSession().setAttribute("tabWrapper", tabWrapper);
        return tabWrapper;
    }

    @RequestMapping(params = "tab=RESOURCES")
    public String showResourcesTabController(PortletRequest request, PortletResponse response, Model model)
            throws PortalException, SystemException {

        if(!tabWrapper.getCanView()) {
            return NO_PERMISSION_TAB_VIEW;
        }

        try {
            wikiPageWrapper = new WikiPageWrapper(getContest());
            setPageAttributes(request, model, tab);
            model.addAttribute("contestResourcesBean", wikiPageWrapper.getContestResourcesBean());
            return TAB_VIEW;
        } catch (Exception e){
        }
        return NOT_FOUND_TAB_VIEW;
    }

    @RequestMapping(params = "action=updateContestResources")
    public void updateResourcesTabController(ActionRequest request, Model model, ActionResponse response,
                                             @ModelAttribute ContestResourcesBean updatedContestResourcesBean, BindingResult result) {

        if(!tabWrapper.getCanEdit()) {
            setNoPermissionErrorRenderParameter(response);
            return;
        }

        if (result.hasErrors()) {
            setErrorRenderParameter(response, "updateContestResources");
            return;
        }

        try{
            wikiPageWrapper.updateWikiPage(updatedContestResourcesBean);
            setSuccessRenderRedirect(response, tab.getName());
        } catch(Exception e){
            e.printStackTrace();
            setNotFoundErrorRenderParameter(response);
        }
    }

    @RequestMapping(params = {"action=updateContestResources", "error=true"})
    public String reportError(PortletRequest request, Model model) throws PortalException, SystemException {
        return TAB_VIEW;
    }


}