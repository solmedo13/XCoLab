package org.xcolab.portlets.proposals.utils;


import org.apache.commons.lang3.tuple.Pair;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.service.ClassNameLocalServiceUtil;

import org.xcolab.client.activities.ActivitiesClient;
import org.xcolab.client.activities.pojo.ActivitySubscription;
import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.OntologyClient;
import org.xcolab.client.contest.PlanTemplateClient;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.OntologyTerm;
import org.xcolab.client.contest.pojo.PlanSectionDefinition;
import org.xcolab.client.proposals.ProposalSupporterClient;
import org.xcolab.client.proposals.ProposalsClient;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.ProposalSupporter;
import org.xcolab.portlets.proposals.wrappers.ContestWrapper;
import org.xcolab.utils.IdListUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.ResourceRequest;

public class ProposalPickerFilterUtil {

    public static final String CONTEST_FILTER_REASON_FOCUS_AREA = "FOCUS_AREA";
    public static final String CONTEST_FILTER_REASON_TIER = "TIER";

    private static final Log _log = LogFactoryUtil.getLog(ProposalPickerFilterUtil.class);

    /**
     * Parse filter from frontend parameter and filter the contents of the proposals parameter
     * @param filterKey the filter key passed as parameter from the frontend
     * @param proposals A list of Proposals paired with their date
     */
    public static void filterByParameter(String filterKey, List<Pair<Proposal, Date>> proposals) {
        if (filterKey != null && filterKey.equalsIgnoreCase("WINNERSONLY")) {
            ProposalPickerFilter.WINNERS_ONLY.filter(proposals);
        } else {
            ProposalPickerFilter.ACCEPT_ALL.filter(proposals);
        }
    }

    public static List<Pair<ContestWrapper, Date>> getFilteredContests(
            long sectionId, ResourceRequest request, ProposalsContext proposalsContext)
            throws SystemException, PortalException {
        List<Pair<ContestWrapper, Date>> contests = ProposalPickerFilterUtil.getAllContests();
        ProposalPickerFilterUtil.filterContests(contests, sectionId, request, proposalsContext, false);
        return contests;
    }

    public static List<Pair<ContestWrapper, Date>> getAllContests() throws SystemException, PortalException {
        List<Pair<ContestWrapper, Date>> contests = new ArrayList<>();

        for (Contest c: ContestClient.getAllContests()) {
                contests.add(Pair.of(new ContestWrapper(c),  //c
                        c.getCreated() == null ? new Date(0) : c.getCreated()));

        }
        return contests;
    }



    public static List<Pair<ContestWrapper, Date>> getTextFilteredContests( long sectionId, String contestName) throws SystemException, PortalException {
        if(contestName == null || contestName.isEmpty()) {
            return getAllContests();
        }
        List<Pair<ContestWrapper, Date>> contests = new ArrayList<>();
        PlanSectionDefinition planSectionDefinition = PlanTemplateClient.getPlanSectionDefinition(sectionId);
        final long contestFocusAreaId;

        List<OntologyTerm> ontologyTerms = OntologyClient.getOntologyTermsForFocusArea(OntologyClient.getFocusArea(planSectionDefinition.getFocusAreaId()));
        List<Long> ontologyTermIds = new ArrayList<>();
        for(OntologyTerm term : ontologyTerms) {
            ontologyTermIds.add(term.getId_());
        }

        for (Contest c: ContestClient.findContestsByName(contestName, ontologyTermIds, IdListUtil.getIdsFromString(planSectionDefinition.getAllowedContestTypeIds()))) {
            contests.add(Pair.of(new ContestWrapper(c),  //c
                    c.getCreated() == null ? new Date(0) : c.getCreated()));

        }
        return contests;
    }


    public static Map<Long, String> filterContests(List<Pair<ContestWrapper, Date>> contests,
            long sectionId, ResourceRequest request, ProposalsContext proposalsContext, boolean trackRemovedContests)
            throws SystemException, PortalException {
        PlanSectionDefinition planSectionDefinition = PlanTemplateClient.getPlanSectionDefinition(sectionId);
        ProposalPickerFilter.CONTEST_TYPE_FILTER.filterContests(contests, planSectionDefinition.getAllowedContestTypeIds());

        List<Long> alwaysIncludedContestIds = planSectionDefinition.getAdditionalIdsAsList();

        final long sectionFocusAreaId = planSectionDefinition.getFocusAreaId();
        final long contestFocusAreaId;
        if (request != null) {
            Contest contest = proposalsContext.getContest(request);
            contestFocusAreaId = contest.getFocusAreaId();
        } else {
            contestFocusAreaId = 0;
        }

        _log.debug(String.format("%d contests before filtering", contests.size()));
        final SectionDefFocusAreaArgument sectionDefFocusAreaArgument =
                new SectionDefFocusAreaArgument(sectionFocusAreaId,
                        contestFocusAreaId, alwaysIncludedContestIds);
        Set<Long> focusAreaRemovedContests =
                ProposalPickerFilter.SECTION_DEF_FOCUS_AREA_FILTER.filterContests(contests,
                sectionDefFocusAreaArgument);

        _log.debug(String.format("%d contests left after filtering for focus areas %d and %d",
                contests.size(), sectionFocusAreaId, contestFocusAreaId));
        final long filterTier = planSectionDefinition.getTier();
        Set<Long> tierRemovedContests = ProposalPickerFilter.CONTEST_TIER.filterContests(contests, filterTier);
        _log.debug(String.format("%d contests left after filtering for tier %d", contests.size(), filterTier));

        Map<Long, String> removedContests = new HashMap<>();
        if (trackRemovedContests) {
            for (Long contestId : focusAreaRemovedContests) {
                removedContests.put(contestId, CONTEST_FILTER_REASON_FOCUS_AREA);
            }
            for (Long contestId : tierRemovedContests) {
                removedContests.put(contestId, CONTEST_FILTER_REASON_TIER);
            }
        }
        return removedContests;
    }

    public static List<Pair<Proposal, Date>> getFilteredSubscribedSupportingProposalsForUser(
            long userId, String filterKey, long sectionId, PortletRequest request, ProposalsContext proposalsContext)
            throws SystemException, PortalException {
        List<Pair<Proposal, Date>> proposals = getFilteredSubscribedProposalsForUser(
                userId, filterKey, sectionId, request, proposalsContext);

        Set<Long> includedProposals = new HashSet<>();
        for (Pair<Proposal, Date> entry : proposals) {
            includedProposals.add(entry.getLeft().getProposalId());
        }
        for (Pair<Proposal, Date> entry : getFilteredSupportingProposalsForUser(
                userId, filterKey, sectionId, request, proposalsContext)) {
            if (includedProposals.contains(entry.getLeft().getProposalId())) {
                continue;
            }
            proposals.add(entry);
        }

        return proposals;
    }

    public static List<Pair<Proposal, Date>> getFilteredSubscribedProposalsForUser(
            long userId, String filterKey, long sectionId, PortletRequest request, ProposalsContext proposalsContext)
            throws SystemException, PortalException {
        List<Pair<Proposal, Date>> proposals = new ArrayList<>();
        List<ActivitySubscription> activitySubscriptions = ActivitiesClient.getActivitySubscriptions(null, null, userId);

        for (ActivitySubscription as : activitySubscriptions) {

            try {
                if (as.getClassNameId() == ClassNameLocalServiceUtil
                        .getClassNameId(Proposal.class)) {
                    proposals.add(Pair.of(
                            ProposalsClient.getProposal(as.getClassPK()),
                            new Date(as.getCreateDate().getTime())
                    ));
                }
            }catch (ProposalNotFoundException ignored){

            }
        }

        filterProposals(proposals, filterKey, sectionId, request, proposalsContext);

        return proposals;
    }

    public static List<Pair<Proposal, Date>> getFilteredSupportingProposalsForUser(
            long userId, String filterKey, long sectionId, PortletRequest request, ProposalsContext proposalsContext)
            throws SystemException, PortalException {
        List<Pair<Proposal, Date>> proposals = new ArrayList<>();
        for (ProposalSupporter ps : ProposalSupporterClient.getProposalSupportersByUserId(userId)) {
            try{
                proposals.add(Pair.of(ProposalsClient.getProposal(ps.getProposalId()), new Date(ps.getCreateDate().getTime())));
            }catch (ProposalNotFoundException ignored){

            }
        }

        filterProposals(proposals, filterKey, sectionId, request, proposalsContext);

        return proposals;
    }

    public static List<Pair<Proposal, Date>> getFilteredAllProposals(String filterText,
            String filterKey, long sectionId, Long contestPK, PortletRequest request, ProposalsContext proposalsContext)
            throws SystemException, PortalException {
        List<Pair<Proposal, Date>> proposals = new ArrayList<>();
        List<Proposal> proposalsRaw;


        //Optimize picker by retrieving just proposals out of contest with corrext tier

        PlanSectionDefinition planSectionDefinition = PlanTemplateClient.getPlanSectionDefinition(sectionId);
        final List<Long> allowedContestTiers = new ArrayList<>();
        //allowedContestTiers.addAll(getAllowedTiers(planSectionDefinition.getTier()));
        for(Long tierId : getAllowedTiers(planSectionDefinition.getTier())) {
            allowedContestTiers.add(tierId);
        }

        if (contestPK > 0) {
            proposalsRaw = ProposalsClient
                    .getProposalsInContest(contestPK);
        } else {
            //proposalsRaw = ProposalsClient.getAllProposals();
            proposalsRaw = ProposalsClient.getProposalsByCurrentContests(IdListUtil.getIdsFromString(planSectionDefinition.getAllowedContestTypeIds()), allowedContestTiers, filterText.isEmpty() ? null : filterText);
        }
        int count = 0;
        for (Proposal p : proposalsRaw) {
            proposals.add(Pair.of(p, new Date(0)));
            count++;
            System.out.println("Proposal No. " + count + " added!");
        }
        System.out.println("done!");

        //filterProposals(proposals, filterKey, sectionId, request, proposalsContext);

        return proposals;
    }

    //TODO: redundant to ProposalPickerFilter
    private static Set<Long> getAllowedTiers(Long filterTier) {
        // if filterTier < 0:
        //  allow tier <= (-filterTier)
        // else if filterTier > 0
        //  only allow tier == filterTier
        Set<Long> allowedTiers = new HashSet<>();
        final long positiveFilterTier = Math.abs(filterTier);
        allowedTiers.add(positiveFilterTier);

        if (filterTier < 0) {
            for (Long currentTier = positiveFilterTier - 1; currentTier >= 0; currentTier--) {
                allowedTiers.add(currentTier);
            }
        }
        return allowedTiers;
    }

    private static void filterProposals(List<Pair<Proposal, Date>> proposals,
            String filterKey, long sectionId, PortletRequest request,
            ProposalsContext proposalsContext)
            throws SystemException, PortalException {
        filterByParameter(filterKey, proposals);
        filterByVisibility(proposals);

        PlanSectionDefinition planSectionDefinition = PlanTemplateClient.getPlanSectionDefinition(sectionId);
        ProposalPickerFilter.CONTEST_TYPE_FILTER.filter(proposals, planSectionDefinition.getAllowedContestTypeIds());

        List<Long> filterExceptionContestIds = planSectionDefinition.getAdditionalIdsAsList();

        final long sectionFocusAreaId = planSectionDefinition.getFocusAreaId();
        final long contestFocusAreaId;
        if (request != null) {
            Contest contest = proposalsContext.getContest(request);
            contestFocusAreaId = contest.getFocusAreaId();
        } else {
            contestFocusAreaId = 0;
        }
        ProposalPickerFilter.SECTION_DEF_FOCUS_AREA_FILTER.filter(proposals,
                new SectionDefFocusAreaArgument(sectionFocusAreaId, contestFocusAreaId, filterExceptionContestIds));

        //TODO: pushed down to Microservice
        //ProposalPickerFilter.CONTEST_TIER.filter(proposals, planSectionDefinition.getTier());
    }

    //TODO:filters 8000 proposals here --> not that efficient
    private static void filterByVisibility(List<Pair<Proposal, Date>> proposals) throws SystemException, PortalException {
        int count=0;
        for (Iterator<Pair<Proposal, Date>> iterator = proposals.iterator(); iterator.hasNext(); ) {

            Proposal proposal = iterator.next().getLeft();
            if (proposal.isDeleted()) {
                iterator.remove();
                count++;
                System.out.println("Proposal  No. "+ count+" removed since deleted !");
            }
        }
    }

    public static class SectionDefFocusAreaArgument {
        private final Long sectionFocusAreaId;
        private final Long contestFocusAreaId;
        private final List<Long> filterExceptionContestIds;


        public SectionDefFocusAreaArgument(Long sectionFocusAreaId, Long contestFocusAreaId, List<Long> filterExceptionContestIds) {
            this.sectionFocusAreaId = sectionFocusAreaId;
            this.contestFocusAreaId = contestFocusAreaId;
            this.filterExceptionContestIds = filterExceptionContestIds;
        }

        public Long getSectionFocusAreaId() {
            return sectionFocusAreaId;
        }

        public Long getContestFocusAreaId() {
            return contestFocusAreaId;
        }

        public List<Long> getFilterExceptionContestIds() {
            return filterExceptionContestIds;
        }
    }
}