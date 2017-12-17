package org.xcolab.service.contest.utils.promotion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.ProposalPhaseClientUtil;
import org.xcolab.client.proposals.exceptions.Proposal2PhaseNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.client.proposals.pojo.phases.Proposal2Phase;
import org.xcolab.entity.utils.email.ContestPhasePromotionEmail;
import org.xcolab.model.tables.pojos.Contest;
import org.xcolab.model.tables.pojos.ContestPhase;
import org.xcolab.model.tables.pojos.ContestPhaseType;
import org.xcolab.service.contest.domain.contest.ContestDao;
import org.xcolab.service.contest.domain.contestphase.ContestPhaseDao;
import org.xcolab.service.contest.domain.contestphasetype.ContestPhaseTypeDao;
import org.xcolab.service.contest.exceptions.NotFoundException;
import org.xcolab.service.contest.service.contest.ContestService;
import org.xcolab.service.contest.service.contestphase.ContestPhaseService;
import org.xcolab.service.contest.utils.email.EmailToAdminDispatcher;
import org.xcolab.util.enums.contest.ProposalContestPhaseAttributeKeys;
import org.xcolab.util.enums.promotion.ContestPhasePromoteType;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Helper class to automatically promote proposals to the next phase.
 */
@Service
public class PromotionService {

    private static final Logger log = LoggerFactory.getLogger(PromotionService.class);

    private static final String STATUS_OPEN_FOR_SUBMISSION = "OPEN_FOR_SUBMISSION";
    private static final String STATUS_CLOSED = "CLOSED";
    private static final long SEMIFINALIST_RIBBON_ID = 3L;
    private static final long FINALIST_RIBBON_ID = 1L;

    private Date now;

    private final ContestPhaseDao contestPhaseDao;
    private final ContestDao contestDao;
    private final ContestPhaseTypeDao contestPhaseTypeDao;
    private final ContestPhaseService contestPhaseService;
    private final ContestService serviceNamespace;

    @Autowired
    public PromotionService( ContestPhaseDao contestPhaseDao, ContestDao contestDao,
            ContestPhaseService contestPhaseService, ContestPhaseTypeDao contestPhaseTypeDao,
            ContestService serviceNamespace) {

        this.contestPhaseDao = contestPhaseDao;
        this.contestDao = contestDao;
        this.contestPhaseTypeDao = contestPhaseTypeDao;
        this.contestPhaseService = contestPhaseService;
        this.serviceNamespace = serviceNamespace;
    }

    public synchronized int doPromotion(Date now) {
        setNow(now);
        int promotedProposals = 0;
        promotedProposals += doBasicPromotion();
        promotedProposals += doJudgingBasedPromotion();
        promotedProposals += distributeRibbons();
        return promotedProposals;
    }

    private int doBasicPromotion() {
        int promotedProposals = 0;
        final List<ContestPhase> phasesToPromote =
                contestPhaseDao.findByPhaseAutopromote(ContestPhasePromoteType.PROMOTE.getValue());
        for (ContestPhase phase : phasesToPromote) {
            PhasePromotionHelper phasePromotionHelper = new PhasePromotionHelper(phase);
            if (phasePromotionHelper.isPhaseContestScheduleTemplatePhase()) {
                continue;
            }

            if (hasValidContest(phase)) {
                continue;
            }

            final boolean hasPhaseEnded = phase.getPhaseEndDate() != null
                    && phase.getPhaseEndDate().before(now);
            if (hasPhaseEnded && !contestPhaseDao.isPhaseActive(phase)) {
                // we have a candidate for promotion, find next phase
                try {
                    log.info("promoting phase {}", phase.getContestPhasePK());
                    Contest contest = contestDao.get(phase.getContestPK());
                    log.info("promoting contest {}", contest.getContestPK());
                    ContestPhase nextPhase = contestPhaseService.getNextContestPhase(phase);
                    for (Proposal p : ProposalClientUtil.getProposalsInContestPhase(phase.getContestPhasePK())) {
                        if (phasePromotionHelper.isProposalPromoted(p)) {
                            continue;
                        }

                        if (!phasePromotionHelper.isProposalVisible(p)) {
                            continue;
                        }

                        ProposalPhaseClientUtil.promoteProposal(p.getProposalId(), nextPhase.getContestPhasePK(), phase.getContestPhasePK());
                        promotedProposals++;
                    }

                    // update phase for which promotion was done (mark it as
                    // "promotion done")
                    phase.setContestPhaseAutopromote("PROMOTE_DONE");
                    contestPhaseDao.update(phase);

                    // if transition is to voting phase
                    if (contestPhaseService.getContestStatus(nextPhase).isCanVote()) {
                        contestPhaseService.transferSupportsToVote(contest, nextPhase);
                    }

                    log.info("done promoting phase {}", phase.getContestPhasePK());
                } catch (NotFoundException e) {
                    log.error("Exception thrown when doing autopromotion for phase {}",
                            phase.getContestPhasePK(), e);
                }
            }
        }
        return promotedProposals;
    }

    private int doJudgingBasedPromotion() {
        int promotedProposals = 0;
        final List<ContestPhase> phasesToPromote = contestPhaseDao
                .findByPhaseAutopromote(ContestPhasePromoteType.PROMOTE_JUDGED.getValue());
        for (ContestPhase phase : phasesToPromote) {
            PhasePromotionHelper phasePromotionHelper = new PhasePromotionHelper(phase);
            if (phasePromotionHelper.isPhaseContestScheduleTemplatePhase()) {
                continue;
            }

            if (hasValidContest(phase)) {
                continue;
            }

            if (phase.getPhaseEndDate() != null && phase.getPhaseEndDate().before(now) && !contestPhaseDao.isPhaseActive(phase)) {
                log.info("promoting phase {} (judging)", phase.getContestPhasePK());

                try {
                    // Only do the promotion if all proposals have been successfully reviewed
                    if (phasePromotionHelper.isAllProposalsReviewed()) {
                        Contest contest = contestDao.get(phase.getContestPK());
                        log.info("promoting contest {} (judging) ", contest.getContestPK());
                        ContestPhase nextPhase = contestPhaseService.getNextContestPhase(phase);
                        for (Proposal p : ProposalClientUtil.getProposalsInContestPhase(phase.getContestPhasePK())) {
                            try {
                                // check if proposal isn't already associated with requested phase
                                if (ProposalPhaseClientUtil.getProposal2PhaseByProposalIdContestPhaseId(p.getProposalId(), nextPhase.getContestPhasePK()) != null) {
                                    log.warn("Proposal is already associated with given contest phase");
                                    continue;
                                }
                            } catch (Proposal2PhaseNotFoundException e) {
                                // no such proposal2phase, we can safely add association
                            }
                            //skip already promoted proposal
                            if (phasePromotionHelper.isProposalPromoted(p)) {
                                log.trace("Proposal already promoted {}", p.getProposalId());
                                continue;
                            }

                            // Decide about the promotion
                            if (phasePromotionHelper.didJudgeDecideToPromote(p)) {
                                log.info("Promote proposal {}", p.getProposalId());
                                ProposalPhaseClientUtil.promoteProposal(p.getProposalId(), nextPhase.getContestPhasePK(), phase.getContestPhasePK());
                                promotedProposals++;
                            }

                            // Enable this line to post promotion comment to Evaluation tab comment section
                            // proposalLocalService.contestPhasePromotionCommentNotifyProposalContributors(p, phase);

                            // Add this check for extra security to prevent proposal authors from being spammed (see COLAB-500)
                            if (phasePromotionHelper.isProposalReviewed(p)) {
                                //TODO: Migrate logic to send email.
                                org.xcolab.client.contest.pojo.phases.ContestPhase cp = ContestClientUtil.getContestPhase(phase.getContestPhasePK());
                                ContestPhasePromotionEmail.contestPhasePromotionEmailNotifyProposalContributors(p, cp);
                                PhasePromotionHelper.createProposalContestPhasePromotionDoneAttribute(p.getProposalId(), phase.getContestPhasePK());

                            }
                        }

                        // if transition is to voting phase
                        if (contestPhaseService.getContestStatus(nextPhase).isCanVote()) {
                            log
                            contestPhaseService.transferSupportsToVote(contest, nextPhase);
                        }
                        phase.setContestPhaseAutopromote("PROMOTE_DONE");
                        contestPhaseDao.update(phase);


                        log.info("done promoting phase {}", phase.getContestPhasePK());
                    } else {
                        log.warn(
                                "Judge promoting failed for ContestPhase with ID {} - not all "
                                        + "proposals have been reviewed",
                                phase.getContestPhasePK());
                    }
                } catch (NotFoundException ignored) {

                }
            }
        }
        return promotedProposals;
    }



    private int distributeRibbons() {
        int promotedProposals = 0;
        final List<ContestPhase> phasesToPromote = contestPhaseDao
                .findByPhaseAutopromote(ContestPhasePromoteType.PROMOTE_RIBBONIZE.getValue());
        for (ContestPhase phase : phasesToPromote) {
            PhasePromotionHelper phasePromotionHelper = new PhasePromotionHelper(phase);
            if (phasePromotionHelper.isPhaseContestScheduleTemplatePhase()) {
                continue;
            }

            if (hasValidContest(phase)) {
                continue;
            }

            if (phase.getPhaseEndDate() != null && phase.getPhaseEndDate().before(now) && !contestPhaseDao.isPhaseActive(phase)) {
                log.info("promoting phase {} (ribbonize)", phase.getContestPhasePK());

                try {
                    log.info("promoting phase {}", phase.getContestPhasePK());
                    Contest contest = contestDao.get(phase.getContestPK());
                    ContestPhase nextPhase = contestPhaseService.getNextContestPhase(phase);

                    List<ContestPhase> contestPhases = serviceNamespace.getAllContestPhases(contest.getContestPK());
                    ContestPhase finalistSelection = null;
                    ContestPhase semifinalistSelection = null;
                    Set<ContestPhase> proposalCreationPhases = new HashSet<>();

                    for (ContestPhase cp : contestPhases) {
                        final Optional<ContestPhaseType> phaseType =
                                contestPhaseTypeDao.get(cp.getContestPhaseType());

                        if (phaseType.isPresent()) {
                            final String status = phaseType.get().getStatus();
                            if (status.equals(STATUS_OPEN_FOR_SUBMISSION)) {
                                proposalCreationPhases.add(cp);
                            } else if (status.equals(STATUS_CLOSED)) {
                                if (finalistSelection == null || cp.getPhaseEndDate()
                                        .after(finalistSelection.getPhaseEndDate())) {
                                    semifinalistSelection = finalistSelection;
                                    finalistSelection = cp;
                                }
                            }
                        }
                    }

                    Set<Proposal> allProposals = new HashSet<>();
                    if (!proposalCreationPhases.isEmpty()) {
                        for (ContestPhase creationPhase : proposalCreationPhases) {
                            final List<Proposal> proposalsInContestPhase = ProposalClientUtil.getProposalsInContestPhase(creationPhase.getContestPhasePK());
                            addAllVisibleProposalsToCollection(proposalsInContestPhase, allProposals, creationPhase);
                        }
                    } else {
                        log.error("Can't distribute ribbons: No proposal creation phase found in contest {}", phase.getContestPK());
                    }

                    List<Proposal> finalists = null;
                    List<Proposal> semifinalists = null;
                    if (finalistSelection != null) {
                        ContestPhase finalsPhase = contestPhaseService.getNextContestPhase(finalistSelection);
                        finalists = ProposalClientUtil.getProposalsInContestPhase(finalsPhase.getContestPhasePK());
                        //make sure all finalists are in the set
                        addAllVisibleProposalsToCollection(finalists, allProposals, finalsPhase);

                        if (semifinalistSelection != null) {
                            ContestPhase semifinalsPhase = contestPhaseService.getNextContestPhase(semifinalistSelection);
                            semifinalists = ProposalClientUtil.getProposalsInContestPhase(semifinalsPhase.getContestPhasePK());
                            semifinalists.removeAll(finalists);
                            //make sure all semifinalists are in the set
                            addAllVisibleProposalsToCollection(semifinalists, allProposals, semifinalsPhase);
                        } else {
                            log.warn("No semifinalist phase found in contest {}", phase.getContestPK());
                        }
                    } else {
                        log.error("Can't distribute ribbons: No finalist phase found in contest {}", phase.getContestPK());
                    }

                    promotedProposals += allProposals.size();
                    associateProposalsWithCompletedPhase(allProposals, phase, nextPhase);
                    if (semifinalists != null) {
                        assignRibbonsToProposals(SEMIFINALIST_RIBBON_ID, semifinalists, nextPhase.getContestPhasePK());
                    }
                    if (finalists != null) {
                        assignRibbonsToProposals(FINALIST_RIBBON_ID, finalists, nextPhase.getContestPhasePK());
                    } else {
                        log.warn("No finalists found to distribute ribbons to.");
                    }

                    // We want to wait showing all ribbons until the winners are determined
                    contest.setHideRibbons(true);
                    contestDao.update(contest);

                    // update phase for which promotion was done (mark it as "promotion done")
                    phase.setContestPhaseAutopromote("PROMOTE_DONE");
                    contestPhaseDao.update(phase);



                    log.info("done promoting phase {}", phase.getContestPhasePK());
                } catch (NotFoundException e) {
                    log.error("Exception thrown when doing auto promotion for phase {}",
                            phase.getContestPhasePK(), e);
                }
            }
        }
        return promotedProposals;
    }

    private boolean hasValidContest(ContestPhase phase) {
        try {
            contestDao.get(phase.getContestPK());
        } catch(NotFoundException e){
            log.warn("promoting phase failed due to invalid contest ", e);
            return true;
        }
        return false;
    }

    private void addAllVisibleProposalsToCollection(Collection<Proposal> sourceCollection,
            Collection<Proposal> toCollection, ContestPhase inPhase) {
        PhasePromotionHelper phasePromotionHelper = new PhasePromotionHelper(inPhase);
        for (Proposal p : sourceCollection) {
            if (!phasePromotionHelper.isProposalVisible(p)) {
                continue;
            }
            toCollection.add(p);
        }
    }

    private void associateProposalsWithCompletedPhase(Set<Proposal> proposals,
            ContestPhase previousPhase, ContestPhase completedPhase)
            throws NotFoundException {

        for (Proposal proposal : proposals) {
            //update the last phase association - set the end version to the current version
            final long proposalId = proposal.getProposalId();
            Integer currentProposalVersion = ProposalClientUtil.countProposalVersions(proposalId);
            if (currentProposalVersion <= 0) {
                log.error("Proposal {} not found: version was {}", proposalId, currentProposalVersion);
                throw new NotFoundException("Proposal not found");
            }

            try {
                //make sure that proposals in the phase directly before have final versions
                Proposal2Phase oldP2p = ProposalPhaseClientUtil.getProposal2PhaseByProposalIdContestPhaseId(proposalId, previousPhase.getContestPhasePK());

                if (oldP2p != null) {
                    if (oldP2p.getVersionTo() < 0) {
                        oldP2p.setVersionTo(currentProposalVersion);
                        ProposalPhaseClientUtil.updateProposal2Phase(oldP2p);
                    }
                }
            } catch (Proposal2PhaseNotFoundException ignored) {
            }

            Proposal2Phase p2p;
            final long completedPhasePK = completedPhase.getContestPhasePK();
            try {
                //This is a workaround for a bug that caused two new p2p's to be created
                p2p = ProposalPhaseClientUtil.getProposal2PhaseByProposalIdContestPhaseId(proposalId, completedPhasePK);
                //If this succeeds, we want to log the error to help diagnose the problem
                log.error("P2p found while associating proposal {} with phase {}.", proposalId, completedPhasePK);
                new EmailToAdminDispatcher("Duplicate primary key for P2p in auto promotion",
                        String.format("Unexpectedly found p2p. Setting versionFrom = %d and versionTo = %d: %s",
                                currentProposalVersion, currentProposalVersion, p2p)).sendMessage();
            } catch (Proposal2PhaseNotFoundException e) {
                p2p = new Proposal2Phase();
                p2p.setProposalId(proposalId);
                p2p.setContestPhaseId(completedPhasePK);
                ProposalPhaseClientUtil.createProposal2Phase(p2p);
                log.debug("Created new p2p: {}", p2p);
            }

            p2p.setVersionFrom(currentProposalVersion);
            p2p.setVersionTo(currentProposalVersion);
            ProposalPhaseClientUtil.updateProposal2Phase(p2p);
        }
    }

    private void assignRibbonsToProposals(Long ribbonId, List<Proposal> proposals, Long phasePK) {
        if (ribbonId > 0) {
            for (Proposal proposal : proposals) {
                //don't overwrite existing ribbons, as they might be manually assigned winner's ribbons!
                if (!ProposalPhaseClientUtil.hasProposalContestPhaseAttribute(proposal.getProposalId(), phasePK,
                        ProposalContestPhaseAttributeKeys.RIBBON)) {

                    if (ContestClientUtil.getContestPhaseRibbonType(ribbonId) != null) {
                        ProposalPhaseClientUtil.setProposalContestPhaseAttribute(proposal.getProposalId(), phasePK,
                                ProposalContestPhaseAttributeKeys.RIBBON, 0L, ribbonId, "");
                    } else {

                        ProposalPhaseClientUtil.deleteProposalContestPhaseAttribute(proposal.getProposalId(), phasePK,
                                ProposalContestPhaseAttributeKeys.RIBBON);
                    }
                } else {
                    log.error("Skipping ribbon (id = {}) assignment for proposal {}: "
                                    + "this proposal already has a ribbon assigned",
                            ribbonId, proposal.getProposalId());
                }
            }
        }
    }

    public void setNow(Date now) {
        this.now = now;
    }
}
