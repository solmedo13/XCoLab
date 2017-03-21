package org.xcolab.view.pages.profile.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.PermissionsClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.view.auth.MemberAuthUtil;
import org.xcolab.view.errors.ErrorText;
import org.xcolab.view.util.entity.flash.AlertMessage;
import org.xcolab.view.util.entity.flash.ErrorMessage;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MergeDuplicateMemberController {

    @GetMapping("/members/profile/{memberId}/mergeduplicate")
    public String show(HttpServletRequest request, HttpServletResponse response,
            @PathVariable Long memberId,
            Model model) throws IOException {

        long currentUser = MemberAuthUtil.getMemberId(request);
        if (PermissionsClient.canAdminAll(currentUser)) {
            try {
                Member member = MembersClient.getMember(memberId);
                model.addAttribute("originalMember", member);
                return "profile/mergeDuplicate";
            }catch (MemberNotFoundException ignored){
                return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
            }
        } else {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }
    }
    @PostMapping("/members/profile/{memberId}/mergeduplicate")
    public String mergeDuplicate(HttpServletRequest request, HttpServletResponse response,
            @PathVariable Long memberId,
            @RequestParam("memberToJoin") Long memberToJoin,
            Model model) throws IOException {
        try {
            Member originalMember = MembersClient.getMember(memberId);
            Member memberToBeJoined = MembersClient.getMember(memberToJoin);

            /*
            -- Before
            -- user and groups and permissions
            UPDATE User_ set userId = 2000811 where userId = 623;
            UPDATE members_Member set id_ = 2000811 where id_ = 623;
            UPDATE Users_Groups set userId = 2000811 where userId = 623;
            UPDATE Users_Roles set userId = 2000811 where userId = 623;
            UPDATE Contact_ set classPK = 2000811 , userId = 2000811 where classPK = 623 OR userId = 623;
            UPDATE Group_ set creatorUserId = 2000811 where creatorUserId = 623;
            UPDATE Group_ set classPK = 2000811 where classPK = 623;

            UPDATE AssetEntry set userId = 2000811 , classPK = 2000811 where userId = 623 or classPK = 623;

            -- activities
            UPDATE xcolab_ActivitySubscription set receiverId = 2000811 where receiverId = 623;
            UPDATE activities_ActivityEntry set memberId = 2000811 where memberId =623;

            -- proposal
            UPDATE xcolab_Proposal set authorId = 2000811 where authorId = 623;
            UPDATE xcolab_ProposalMoveHistory set movingUserId = 2000811 where movingUserId = 623;
            UPDATE xcolab_ProposalSupporter set userId = 2000811 where userId = 623;
            UPDATE xcolab_ProposalVote set userId = 2000811 where userId = 623;

            UPDATE Users_Groups set userId = 2000811 where userId = 623;

            -- comment
            UPDATE comment_Comment set authorId = 2000811 where authorId = 623;
            UPDATE comment_Thread set authorId = 2000811 where authorId = 623;

            -- messaging user pref
            UPDATE xcolab_MessagingUserPreferences set userId = 2000811 where userId = 623;
            UPDATE xcolab_Message set fromId = 2000811 where fromId = 623;
            UPDATE xcolab_Message set repliesTo = 2000811 where repliesTo = 623;
            UPDATE xcolab_MessageRecipientStatus set userId = 2000811 where userId = 623;

            -- contest team member
            UPDATE xcolab_ContestTeamMember set userId = 2000811 where userId = 623;


            */



            AlertMessage.success("The profiles were merged, this is the original member's profile!").flash(request);
            response.sendRedirect("/members/profile/" + originalMember.getId_());
        }catch (MemberNotFoundException mnfe){

        }


        return ErrorMessage.ERROR_VIEW;

    }
}
