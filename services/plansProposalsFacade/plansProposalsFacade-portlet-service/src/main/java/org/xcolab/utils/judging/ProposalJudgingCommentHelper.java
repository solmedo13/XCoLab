package org.xcolab.utils.judging;

import com.ext.portlet.JudgingSystemActions;
import com.ext.portlet.NoSuchProposalContestPhaseAttributeException;
import com.ext.portlet.ProposalContestPhaseAttributeKeys;
import com.ext.portlet.model.ContestPhase;
import com.ext.portlet.model.Proposal;
import com.ext.portlet.model.ProposalContestPhaseAttribute;
import com.ext.portlet.service.ProposalContestPhaseAttributeLocalServiceUtil;
import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;

/**
 * This is a helper class that interprets the Judging Feedback message made during judging contest phases
 * for email notifications and proposal comment thread messages. In addition it is a utility class to get and set
 * Screening and Advance comments
 *
 * Created by kmang on 27/05/14.
 */
public class ProposalJudgingCommentHelper {

    private final static Log _log = LogFactoryUtil.getLog(ProposalJudgingCommentHelper.class);

    public static final String REVIEW_COMMENT_BEGIN_TAG = "<review_comment>";
    public static final String REVIEW_COMMENT_END_TAG = "</review_comment>";

    private Proposal proposal;
    private ContestPhase contestPhase;

    public ProposalJudgingCommentHelper(Proposal proposal, ContestPhase contestPhase) {
        this.proposal = proposal;
        this.contestPhase = contestPhase;
    }

    public String getContestPhasePromotionCommentBody() throws NoSuchProposalContestPhaseAttributeException, SystemException {
        return extractComment(getContestPhasePromotionBody());

    }

    public String getContestPhasePromotionEmailBody() throws NoSuchProposalContestPhaseAttributeException, SystemException {
        String messageBody = StringUtil.replace(getContestPhasePromotionBody(), REVIEW_COMMENT_BEGIN_TAG, "");
        return StringUtil.replace(messageBody, REVIEW_COMMENT_END_TAG, "");
    }

    public String getScreeningComment() throws NoSuchProposalContestPhaseAttributeException, SystemException {
        //get fellow decision
        ProposalContestPhaseAttribute fellowActionAttribute = ProposalContestPhaseAttributeLocalServiceUtil.
                getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.FELLOW_ACTION, 0);
        JudgingSystemActions.FellowAction fellowAction = JudgingSystemActions.FellowAction.fromInt((int) fellowActionAttribute.getNumericValue());

        if (fellowAction != JudgingSystemActions.FellowAction.NO_DECISION &&
                fellowAction != JudgingSystemActions.FellowAction.PASS_TO_JUDGES) {
            String fellowRejectionText = ProposalContestPhaseAttributeLocalServiceUtil.
                    getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.FELLOW_ACTION_COMMENT, 0).getStringValue();

            return extractComment(fellowRejectionText);
        }

        return null;
    }

    public void setScreeningComment(String template, String comment) throws NoSuchProposalContestPhaseAttributeException, SystemException {
        ProposalContestPhaseAttribute fellowActionAttribute = ProposalContestPhaseAttributeLocalServiceUtil.
                getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.FELLOW_ACTION, 0);
        JudgingSystemActions.FellowAction fellowAction = JudgingSystemActions.FellowAction.fromInt((int) fellowActionAttribute.getNumericValue());

        if (fellowAction != JudgingSystemActions.FellowAction.NO_DECISION &&
                fellowAction != JudgingSystemActions.FellowAction.PASS_TO_JUDGES) {


            String screeningMessage = wrapComment(template, comment);
            persistAttribute(ProposalContestPhaseAttributeKeys.FELLOW_ACTION_COMMENT, screeningMessage);
        }
    }

    public String getAdvancingComment() throws NoSuchProposalContestPhaseAttributeException, SystemException {
        ProposalContestPhaseAttribute advanceDecisionAttribute = ProposalContestPhaseAttributeLocalServiceUtil.
                getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.JUDGE_DECISION, 0);
        JudgingSystemActions.AdvanceDecision advanceDecision = JudgingSystemActions.AdvanceDecision.fromInt((int) advanceDecisionAttribute.getNumericValue());

        if (advanceDecision != JudgingSystemActions.AdvanceDecision.NO_DECISION) {
            String advanceDecisionText = ProposalContestPhaseAttributeLocalServiceUtil.
                    getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.PROPOSAL_REVIEW, 0).getStringValue();

            return extractComment(advanceDecisionText);
        }

        return null;
    }

    public void setAdvancingComment(String template, String comment) throws NoSuchProposalContestPhaseAttributeException, SystemException {
        ProposalContestPhaseAttribute advanceDecisionAttribute = getProposalContestPhaseAttributeCreateIfNotExists(ProposalContestPhaseAttributeKeys.JUDGE_DECISION);
        JudgingSystemActions.AdvanceDecision advanceDecision = JudgingSystemActions.AdvanceDecision.fromInt((int) advanceDecisionAttribute.getNumericValue());

        if (advanceDecision != JudgingSystemActions.AdvanceDecision.NO_DECISION) {

            String advanceMessage = wrapComment(template, comment);
            persistAttribute(ProposalContestPhaseAttributeKeys.PROPOSAL_REVIEW, advanceMessage);
        }
    }

    public static String getCommentHeader(String message) {
        int commentTagBeginIndex = message.indexOf(REVIEW_COMMENT_BEGIN_TAG);

        if (commentTagBeginIndex == -1) {
            return "";
        }
        return message.substring(0, commentTagBeginIndex);
    }

    public static String getCommentFooter(String message) {
        int commentTagEndIndex = message.indexOf(REVIEW_COMMENT_END_TAG) + REVIEW_COMMENT_END_TAG.length();

        if (commentTagEndIndex == -1 + REVIEW_COMMENT_END_TAG.length()) {
            return "";
        }
        return message.substring(commentTagEndIndex);
    }

    public static String extractComment(String message) {
        int commentBeginIndex = message.indexOf(REVIEW_COMMENT_BEGIN_TAG) + REVIEW_COMMENT_BEGIN_TAG.length();
        int commentEndIndex = message.indexOf(REVIEW_COMMENT_END_TAG);

        // Is comment pattern valid?
        if ((commentBeginIndex - REVIEW_COMMENT_BEGIN_TAG.length()) == -1 ||
                commentEndIndex == -1) {
            return "";
        }

        return message.substring(commentBeginIndex, commentEndIndex);
    }

    public static String wrapComment(String template, String comment) {
        if (!template.contains(REVIEW_COMMENT_BEGIN_TAG) || !template.contains(REVIEW_COMMENT_END_TAG)) {
            return comment;
        }

        int commentBeginIndex = template.indexOf(REVIEW_COMMENT_BEGIN_TAG) + REVIEW_COMMENT_BEGIN_TAG.length();
        return template.substring(0, commentBeginIndex) + comment + template.substring(commentBeginIndex);
    }

    private String getContestPhasePromotionBody() throws NoSuchProposalContestPhaseAttributeException, SystemException {
        //get fellow decision
        JudgingSystemActions.FellowAction fellowAction;
        try {
            ProposalContestPhaseAttribute fellowActionAttribute = ProposalContestPhaseAttributeLocalServiceUtil.
                    getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.FELLOW_ACTION, 0);
            fellowAction = JudgingSystemActions.FellowAction.fromInt((int) fellowActionAttribute.getNumericValue());
        } catch (NoSuchProposalContestPhaseAttributeException e) {
            fellowAction = JudgingSystemActions.FellowAction.NO_DECISION;
        }

        if (fellowAction == JudgingSystemActions.FellowAction.PASS_TO_JUDGES || !contestPhase.isFellowScreeningActive()) {
            try {
                String reviewText = ProposalContestPhaseAttributeLocalServiceUtil.
                        getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.PROPOSAL_REVIEW, 0).getStringValue();

                ProposalContestPhaseAttribute advanceDecisionAttribute = ProposalContestPhaseAttributeLocalServiceUtil.
                        getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.JUDGE_DECISION, 0);
                JudgingSystemActions.AdvanceDecision advanceDecision = JudgingSystemActions.AdvanceDecision.fromInt((int) advanceDecisionAttribute.getNumericValue());

                if (advanceDecision != JudgingSystemActions.AdvanceDecision.NO_DECISION) {
                    return reviewText;
                }
            } catch (NoSuchProposalContestPhaseAttributeException e) {
                _log.error("Could not extract contest phase promotion body for proposal " + proposal.getProposalId() + " in contestPhase " + contestPhase.getContestPhasePK(), e);
            }
        } else if (fellowAction != JudgingSystemActions.FellowAction.NO_DECISION) {
            //fellow decided
            String fellowReviewText = ProposalContestPhaseAttributeLocalServiceUtil.
                    getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.FELLOW_ACTION_COMMENT, 0).getStringValue();

            return fellowReviewText;
        }

        return StringPool.BLANK;
    }

    private boolean persistAttribute(String attributeName, long numericValue) {
        ProposalContestPhaseAttribute attribute = getProposalContestPhaseAttributeCreateIfNotExists(attributeName);

        attribute.setNumericValue(numericValue);

        try {
            ProposalContestPhaseAttributeLocalServiceUtil.updateProposalContestPhaseAttribute(attribute);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean persistAttribute(String attributeName, String stringValue) {
        ProposalContestPhaseAttribute attribute = getProposalContestPhaseAttributeCreateIfNotExists(attributeName);

        attribute.setStringValue(stringValue);

        try {
            ProposalContestPhaseAttributeLocalServiceUtil.updateProposalContestPhaseAttribute(attribute);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private ProposalContestPhaseAttribute getProposalContestPhaseAttributeCreateIfNotExists(String attributeName) {
        try {
            return ProposalContestPhaseAttributeLocalServiceUtil.
                    getProposalContestPhaseAttribute(proposal.getProposalId(), contestPhase.getContestPhasePK(), attributeName, 0);
        } catch (NoSuchProposalContestPhaseAttributeException e) {
            try {
                ProposalContestPhaseAttribute attribute = ProposalContestPhaseAttributeLocalServiceUtil.createProposalContestPhaseAttribute(
                        CounterLocalServiceUtil.increment(ProposalContestPhaseAttribute.class.getName()));
                attribute.setProposalId(proposal.getProposalId());
                attribute.setContestPhaseId(contestPhase.getContestPhasePK());
                attribute.setName(attributeName);
                ProposalContestPhaseAttributeLocalServiceUtil.addProposalContestPhaseAttribute(attribute);
                return attribute;
            } catch (Exception e2) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
