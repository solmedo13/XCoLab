package org.xcolab.client.proposals;

import org.xcolab.client.activities.ActivitiesClient;
import org.xcolab.client.contest.resources.ProposalResource;
import org.xcolab.client.proposals.exceptions.MembershipRequestNotFoundException;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.ProposalDto;
import org.xcolab.client.proposals.pojo.team.ProposalTeamMembershipRequest;
import org.xcolab.client.proposals.pojo.team.ProposalTeamMembershipRequestDto;
import org.xcolab.commons.exceptions.InternalException;
import org.xcolab.util.activities.enums.ActivityCategory;
import org.xcolab.util.activities.enums.ProposalActivityType;
import org.xcolab.util.enums.membershiprequest.MembershipRequestStatus;
import org.xcolab.util.http.caching.CacheKeys;
import org.xcolab.util.http.caching.CacheName;
import org.xcolab.util.http.client.RestResource1;
import org.xcolab.util.http.client.RestResource2;
import org.xcolab.util.http.client.enums.ServiceNamespace;
import org.xcolab.util.http.client.types.TypeProvider;
import org.xcolab.util.http.dto.DtoUtil;
import org.xcolab.util.http.exceptions.Http409ConflictException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MembershipClient {

    private static final Map<ServiceNamespace, MembershipClient> instances = new HashMap<>();

    private final ServiceNamespace serviceNamespace;

    private final RestResource1<ProposalDto, Long> proposalResource;

    private final RestResource1<ProposalTeamMembershipRequestDto, Long> membershipRequestResource;
    private final RestResource2<ProposalDto, Long, Long, Long> proposalTeamMemberResource;

    private final ProposalClient proposalClient;

    private MembershipClient(ServiceNamespace serviceNamespace) {
        proposalResource = new RestResource1<>(ProposalResource.PROPOSAL, ProposalDto.TYPES);
        membershipRequestResource = new RestResource1<>(ProposalResource.MEMBERSHIP_REQUEST,
                ProposalTeamMembershipRequestDto.TYPES);
        proposalClient = ProposalClient.fromNamespace(serviceNamespace);
        this.serviceNamespace = serviceNamespace;
        proposalTeamMemberResource = proposalResource
                .nestedResource("teamMembers", TypeProvider.LONG);
    }

    public static MembershipClient fromNamespace(ServiceNamespace proposalService) {
        return instances
                .computeIfAbsent(proposalService, MembershipClient::new);
    }

    public void denyMembershipRequest(Proposal proposal, long userId, long membershipRequestId,
            String reply, long updateauthorUserId) {
        if (hasUserRequestedMembership(proposal, userId)) {
            try {
                ProposalTeamMembershipRequest membershipRequest = getMembershipRequest(membershipRequestId);
                membershipRequest.setStatusId(MembershipRequestStatus.STATUS_DENIED);
                membershipRequest.setReplierUserId(updateauthorUserId);
                membershipRequest.setReplyComments(reply);
                membershipRequest.setReplyDate(new Timestamp((new Date()).getTime()));
                updateMembershipRequest(membershipRequest);
            } catch (MembershipRequestNotFoundException ignored) {

            }
        }
    }

    public boolean updateMembershipRequest(ProposalTeamMembershipRequest membershipRequest) {
        return membershipRequestResource
                .update(new ProposalTeamMembershipRequestDto(membershipRequest),
                        membershipRequest.getId())
                .execute();
    }

    public Boolean hasUserRequestedMembership(Proposal proposal, Long userId) {
        try {
            ProposalTeamMembershipRequest userRequest = getActiveMembershipRequestByUser(proposal, userId);
            if (userRequest != null) {
                return true;
            }
        } catch (ProposalNotFoundException ignored) {

        }
        return false;
    }

    public ProposalTeamMembershipRequest getActiveMembershipRequestByUser(Proposal proposal, Long userId) {
        return DtoUtil.toPojo(membershipRequestResource.list()
                .optionalQueryParam("proposalId", proposal.getId())
                .optionalQueryParam("userId", userId)
                .executeWithResult().getOneIfExists(), serviceNamespace);
    }

    public ProposalTeamMembershipRequest getMembershipRequest(long MembershipRequestId)
            throws MembershipRequestNotFoundException {
        return membershipRequestResource.get(MembershipRequestId)
                .execute().toPojo(serviceNamespace);
    }

    public void approveMembershipRequest(Proposal proposal, Long userId, ProposalTeamMembershipRequest request,
            String reply, Long updateauthorUserId) {

        if (hasUserRequestedMembership(proposal, userId)) {
            try {
                ProposalTeamMembershipRequest membershipRequest =
                        getMembershipRequest(request.getId());
                membershipRequest.setStatusId(MembershipRequestStatus.STATUS_APPROVED);
                membershipRequest.setReplierUserId(updateauthorUserId);
                membershipRequest.setReplyComments(reply);
                membershipRequest.setReplyDate(new Timestamp((new Date()).getTime()));
                updateMembershipRequest(membershipRequest);
                addUserToProposalTeam(userId, proposal);
            } catch (MembershipRequestNotFoundException e) {
                throw new InternalException(e);
            }
        }
    }

    public void addUserToProposalTeam(Long userId, Proposal proposal) {
        long proposalId = proposal.getId();
        try {
            proposalTeamMemberResource.resolveParentId(proposalResource.id(proposalId))
                    .create(userId)
                    .execute();

            ActivitiesClient activityClient = ActivitiesClient.fromNamespace(serviceNamespace);

            activityClient.createActivityEntry(ProposalActivityType.MEMBER_ADDED, userId,
                    proposalId);

            if (!activityClient.isSubscribedToActivity(userId, ActivityCategory.PROPOSAL,
                    proposalId)) {
                activityClient
                        .addSubscription(userId, ActivityCategory.PROPOSAL, proposalId, null);

            }
        } catch (Http409ConflictException ignored) {
            // already a member - don't do anything
        }
    }

    public ProposalTeamMembershipRequest addInvitedMembershipRequest(Long proposalId, Long userId,
            String comment) {
        return createMembershipRequest(proposalId, userId, comment,
                MembershipRequestStatus.STATUS_PENDING_INVITED);
    }

    private ProposalTeamMembershipRequest createMembershipRequest(Long proposalId, Long userId, String comment,
            Integer status) {
        try {
            ProposalTeamMembershipRequest membershipRequest = new ProposalTeamMembershipRequest();
            membershipRequest.setComments(comment == null ? "" : comment);
            membershipRequest.setProposalId(proposalId);
            membershipRequest.setUserId(userId);
            membershipRequest.setStatusId(status);
            membershipRequest = createMembershipRequest(membershipRequest);
            return membershipRequest;
        } catch (ProposalNotFoundException ignored) {

        }
        return null;
    }

    public ProposalTeamMembershipRequest createMembershipRequest(
            ProposalTeamMembershipRequest membershipRequest) {
        return membershipRequestResource.create(new ProposalTeamMembershipRequestDto(membershipRequest))
                .execute().toPojo(serviceNamespace);
    }

    public ProposalTeamMembershipRequest addRequestedMembershipRequest(Long proposalId, Long userId,
            String comment) {
        return createMembershipRequest(proposalId, userId, comment,
                MembershipRequestStatus.STATUS_PENDING_REQUESTED);
    }

    public List<ProposalTeamMembershipRequest> getMembershipRequests(Long proposalId) {

        try {
            Proposal proposal = proposalClient.getProposal(proposalId);
            List<ProposalTeamMembershipRequest> invited = getMembershipRequestsByStatus(proposal.getId(),
                    MembershipRequestStatus.STATUS_PENDING_INVITED);
            List<ProposalTeamMembershipRequest> requested = getMembershipRequestsByStatus(proposal.getId(),
                    MembershipRequestStatus.STATUS_PENDING_REQUESTED);
            List<ProposalTeamMembershipRequest> olderRequests =
                    getMembershipRequestsByStatus(proposal.getId(),
                            MembershipRequestStatus.STATUS_PENDING);
            List<ProposalTeamMembershipRequest> combined = new ArrayList<>();
            if (invited != null && !invited.isEmpty()) {
                combined.addAll(invited);
            }
            if (requested != null && !requested.isEmpty()) {
                combined.addAll(requested);
            }
            if (olderRequests != null && !olderRequests.isEmpty()) {
                combined.addAll(olderRequests);
            }
            return combined;
        } catch (ProposalNotFoundException ignored) {
            return null;
        }
    }

    public List<ProposalTeamMembershipRequest> getMembershipRequestsByStatus(Long proposalId, Integer statusId) {
        return DtoUtil.toPojos(membershipRequestResource.list()
                .withCache(CacheKeys.withClass(ProposalTeamMembershipRequestDto.class)
                        .withParameter("proposalId", proposalId)
                        .withParameter("statusId", statusId).asList(), CacheName.MISC_REQUEST)
                .optionalQueryParam("proposalId", proposalId)
                .optionalQueryParam("statusId", statusId)
                .execute(), serviceNamespace);
    }

}
