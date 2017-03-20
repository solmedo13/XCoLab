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

            //do the crazy things

            AlertMessage.success("The profiles were merged, this is the original member's profile!").flash(request);
            response.sendRedirect("/members/profile/" + originalMember.getId_());
        }catch (MemberNotFoundException mnfe){

        }


        return ErrorMessage.ERROR_VIEW;

    }
}
