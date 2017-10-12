package org.xcolab.view.pages.contestmanagement.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.pojo.ContestSchedule;
import org.xcolab.client.contest.pojo.phases.ContestPhaseType;
import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.util.enums.promotion.ContestPhasePromoteType;
import org.xcolab.util.html.LabelStringValue;
import org.xcolab.util.html.LabelValue;
import org.xcolab.view.errors.AccessDeniedPage;
import org.xcolab.view.pages.contestmanagement.entities.ContestManagerTabs;
import org.xcolab.view.pages.contestmanagement.utils.schedule.ContestScheduleLifecycleUtil;
import org.xcolab.view.pages.contestmanagement.wrappers.ContestScheduleBean;
import org.xcolab.view.pages.contestmanagement.wrappers.ElementSelectIdWrapper;
import org.xcolab.view.pages.contestmanagement.wrappers.PlatformTeamBean;
import org.xcolab.view.taglibs.xcolab.wrapper.TabWrapper;
import org.xcolab.view.util.entity.flash.AlertMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/contest/manager")
public class TeamsTabController extends AbstractTabController {

    private static final ContestManagerTabs tab = ContestManagerTabs.TEAMS;
    private static final String TAB_VIEW = "contestmanagement/manager/teamsTab";

    private static final String CONTEST_TEAM_BEAN_ATTRIBUTE_KEY = "teamBean";

    private List<PlatformTeam> teams;

    @ModelAttribute("currentTabWrapped")
    @Override
    public TabWrapper populateCurrentTabWrapped(HttpServletRequest request) {
        tabWrapper = new TabWrapper(tab, request, tabContext);
        request.getSession().setAttribute("tabWrapper", tabWrapper);
        return tabWrapper;
    }

    @GetMapping("tab/TEAMS")
    public String showTeamTabController(HttpServletRequest request,
            HttpServletResponse response, Model model, Member member,
            @RequestParam(value = "elementId", required = false) Long elementId) {
        if (!tabWrapper.getCanView()) {
            return new AccessDeniedPage(member).toViewName(response);
        }

        if (this.teams == null) {
            this.teams = getMockTeamList();
        }

        Long teamId = -1L;
        PlatformTeam team = null;
        if (elementId != null) {
            team = getTeamWithId(elementId);
        } else {
            team = getFirstTeam();
        }
        if (!this.teams.isEmpty() && team != null && !model.containsAttribute(CONTEST_TEAM_BEAN_ATTRIBUTE_KEY)) {
            model.addAttribute(CONTEST_TEAM_BEAN_ATTRIBUTE_KEY, new PlatformTeamBean(team));
            teamId = team.getId_();
        }
        model.addAttribute("teamId", teamId);
        model.addAttribute("elementSelectIdWrapper", new ElementSelectIdWrapper(teamId,
                getAllTeamItems()));
        return TAB_VIEW;
    }

    @PostMapping("tab/TEAMS")
    public void updateTeam(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute PlatformTeamBean teamBean) throws IOException {

        if (!tabWrapper.getCanEdit()) {
            response.sendRedirect(ContestManagerTabs.TEAMS.getTabUrl());
        }

        PlatformTeam team = null;
        if (teamBean != null && teamBean.getTeamId() != null) {
            team = getTeamWithId(teamBean.getTeamId());
            team.setName(teamBean.getName());
        } else {
            team = addNewTeam();
        }

        response.sendRedirect(ContestManagerTabs.TEAMS.getTabUrl(team.getId_()));
    }

    @PostMapping("tab/TEAMS/{teamId}/delete")
    public void deleteTeam(HttpServletRequest request, HttpServletResponse response,
            @PathVariable long teamId) throws IOException {

        if (!tabWrapper.getCanEdit()) {
            response.sendRedirect(ContestManagerTabs.TEAMS.getTabUrl());
            return;
        }

        deleteTeam(teamId);

        response.sendRedirect(ContestManagerTabs.TEAMS.getTabUrl());
    }

    @PostMapping("tab/TEAMS/{teamId}/removeMember/{memberId}")
    public void removeMember(HttpServletRequest request,
            HttpServletResponse response, Model model, Member member,
            @PathVariable long teamId, @PathVariable long memberId) throws IOException {

        if (!tabWrapper.getCanEdit()) {
            response.sendRedirect(ContestManagerTabs.TEAMS.getTabUrl(teamId));
            return;
        }

        removeMember(teamId, memberId);

        response.sendRedirect(ContestManagerTabs.TEAMS.getTabUrl(teamId));

    }

    @PostMapping("tab/TEAMS/{teamId}/addMember")
    public void addMember(HttpServletRequest request,
            HttpServletResponse response, Model model, Member member,
            @PathVariable long teamId, @RequestParam long userId) throws IOException {

        if (!tabWrapper.getCanEdit()) {
            return;
        }

        addMember(teamId, userId);

    }

    private void addMember(Long teamId, Long memberId) {
        PlatformTeam team = getTeamWithId(teamId);
        if (team != null) {
            try {
                Member teamMember = MembersClient.getMember(memberId);
                team.add(teamMember);
            } catch (MemberNotFoundException ignored) {}
        }
    }

    private void removeMember(Long teamId, Long memberId) {
        PlatformTeam team = getTeamWithId(teamId);
        if (team != null) {
            try {
                Member teamMember = MembersClient.getMember(memberId);
                team.remove(teamMember);
            } catch (MemberNotFoundException ignored) {}
        }
    }

    private void deleteTeam(Long teamId) {
        PlatformTeam team = getTeamWithId(teamId);
        teams.remove(team);
    }

    private PlatformTeam addNewTeam() {
        String NEW_TEAM_NAME = "New team";
        PlatformTeam team = new PlatformTeam();
        team.setName(NEW_TEAM_NAME);
        teams.add(team);
        return team;
    }

    private PlatformTeam getTeamWithId(long teamId) {
        PlatformTeam team = null;
        for (PlatformTeam curTeam : teams) {
            if (curTeam.getId_().equals(teamId)) {
                team = curTeam;
                break;
            }
        }
        return team;
    }

    private List<LabelValue> getAllTeamItems() {
        List<LabelValue> teamItems = new ArrayList<>();
        for (PlatformTeam team : teams) {
            teamItems.add(new LabelValue(team.getId_(), team.getName()));
        }
        return teamItems;
    }

    private PlatformTeam getFirstTeam() {
        if (!this.teams.isEmpty()) {
            return this.teams.get(0);
        } else {
            return null;
        }
    }

    private List<PlatformTeam> getMockTeamList() {
        List<PlatformTeam> teams = new ArrayList<>();
        PlatformTeam team1 = new PlatformTeam();
        PlatformTeam team2 = new PlatformTeam();
        PlatformTeam team3 = new PlatformTeam();
        team1.setName("Team Uno");
        team2.setName("Team Dos");
        team3.setName("Team Tres");
        try {
            Member aleks = MembersClient.getMember(2666734);
            Member schwanzo = MembersClient.getMember(2666735);
            Member schmibo = MembersClient.getMember(2666736);
            team1.add(aleks);
            team1.add(schwanzo);
            team1.add(schmibo);
        } catch (MemberNotFoundException ignored) {}
        teams.add(team1);
        teams.add(team2);
        teams.add(team3);
        return teams;
    }
}
