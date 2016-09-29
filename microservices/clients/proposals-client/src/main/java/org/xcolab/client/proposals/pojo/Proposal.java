/**
 * This class is generated by jOOQ
 */
package org.xcolab.client.proposals.pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.core.ParameterizedTypeReference;

import org.xcolab.client.contest.ContestClient;
import org.xcolab.client.contest.exceptions.ContestNotFoundException;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.ContestPhase;
import org.xcolab.client.contest.pojo.ContestType;
import org.xcolab.client.proposals.ProposalsClient;
import org.xcolab.client.proposals.enums.ProposalAttributeKeys;
import org.xcolab.util.enums.contest.ProposalContestPhaseAttributeKeys;
import org.xcolab.util.http.client.types.TypeProvider;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class Proposal implements Serializable {
    public static final TypeProvider<Proposal> TYPES =
            new TypeProvider<>(Proposal.class,
                    new ParameterizedTypeReference<List<Proposal>>() {
                    });

    private static final long serialVersionUID = 1313799162;

    private Long      proposalid;
    private Timestamp createdate;
    private Timestamp updateddate;
    private Integer   currentversion;
    private Long      authorid;
    private Boolean   visible;
    private Long      discussionid;
    private Long      resultsdiscussionid;
    private Long      judgediscussionid;
    private Long      fellowdiscussionid;
    private Long      advisordiscussionid;
    private Long      groupid;

    public Proposal() {}

    public Proposal(Proposal value) {
        this.proposalid = value.proposalid;
        this.createdate = value.createdate;
        this.updateddate = value.updateddate;
        this.currentversion = value.currentversion;
        this.authorid = value.authorid;
        this.visible = value.visible;
        this.discussionid = value.discussionid;
        this.resultsdiscussionid = value.resultsdiscussionid;
        this.judgediscussionid = value.judgediscussionid;
        this.fellowdiscussionid = value.fellowdiscussionid;
        this.advisordiscussionid = value.advisordiscussionid;
        this.groupid = value.groupid;
    }

    public Proposal(
        Long      proposalid,
        Timestamp createdate,
        Timestamp updateddate,
        Integer   currentversion,
        Long      authorid,
        Boolean   visible,
        Long      discussionid,
        Long      resultsdiscussionid,
        Long      judgediscussionid,
        Long      fellowdiscussionid,
        Long      advisordiscussionid,
        Long      groupid
    ) {
        this.proposalid = proposalid;
        this.createdate = createdate;
        this.updateddate = updateddate;
        this.currentversion = currentversion;
        this.authorid = authorid;
        this.visible = visible;
        this.discussionid = discussionid;
        this.resultsdiscussionid = resultsdiscussionid;
        this.judgediscussionid = judgediscussionid;
        this.fellowdiscussionid = fellowdiscussionid;
        this.advisordiscussionid = advisordiscussionid;
        this.groupid = groupid;
    }

    public Long getProposalId() {
        return this.proposalid;
    }

    public void setProposalId(Long proposalid) {
        this.proposalid = proposalid;
    }

    public Timestamp getCreateDate() {
        return this.createdate;
    }

    public void setCreateDate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public Timestamp getUpdatedDate() {
        return this.updateddate;
    }

    public void setUpdatedDate(Timestamp updateddate) {
        this.updateddate = updateddate;
    }

    public Integer getCurrentVersion() {
        return this.currentversion;
    }

    public void setCurrentVersion(Integer currentversion) {
        this.currentversion = currentversion;
    }

    public Long getAuthorId() {
        return this.authorid;
    }

    public void setAuthorId(Long authorid) {
        this.authorid = authorid;
    }

    public Boolean getVisible() {
        return this.visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Long getDiscussionId() {
        return this.discussionid;
    }

    public void setDiscussionId(Long discussionid) {
        this.discussionid = discussionid;
    }

    public Long getResultsDiscussionId() {
        return this.resultsdiscussionid;
    }

    public void setResultsDiscussionId(Long resultsdiscussionid) {
        this.resultsdiscussionid = resultsdiscussionid;
    }

    public Long getJudgeDiscussionId() {
        return this.judgediscussionid;
    }

    public void setJudgeDiscussionId(Long judgediscussionid) {
        this.judgediscussionid = judgediscussionid;
    }

    public Long getFellowDiscussionId() {
        return this.fellowdiscussionid;
    }

    public void setFellowDiscussionId(Long fellowdiscussionid) {
        this.fellowdiscussionid = fellowdiscussionid;
    }

    public Long getAdvisorDiscussionId() {
        return this.advisordiscussionid;
    }

    public void setAdvisorDiscussionId(Long advisordiscussionid) {
        this.advisordiscussionid = advisordiscussionid;
    }

    public Long getGroupId() {
        return this.groupid;
    }

    public void setGroupId(Long groupid) {
        this.groupid = groupid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Proposal (");

        sb.append(proposalid);
        sb.append(", ").append(createdate);
        sb.append(", ").append(updateddate);
        sb.append(", ").append(currentversion);
        sb.append(", ").append(authorid);
        sb.append(", ").append(visible);
        sb.append(", ").append(discussionid);
        sb.append(", ").append(resultsdiscussionid);
        sb.append(", ").append(judgediscussionid);
        sb.append(", ").append(fellowdiscussionid);
        sb.append(", ").append(advisordiscussionid);
        sb.append(", ").append(groupid);

        sb.append(")");
        return sb.toString();
    }
    @JsonIgnore
    public boolean isOpen(){
        ProposalAttribute attribute = ProposalsClient.getProposalAttribute(this.getProposalId(), ProposalAttributeKeys.OPEN, 0l);
        if(attribute!= null) {
            return attribute.getNumericValue() > 0;
        }else{
            return false;
        }
    }

    @JsonIgnore
    public String getProposalLinkUrl(Contest contest) {
        return getProposalLinkUrl(contest, 0l);
    }
    @JsonIgnore
    public String getProposalLinkUrl(Contest contest, long contestPhaseId) {
        String link = "/";
        String friendlyUrlStringProposal;
        Long proposalId = this.getProposalId();

            final ContestType contestType = ContestClient.getContestType(contest.getContestTypeId());
            link += contestType.getFriendlyUrlStringContests();
            friendlyUrlStringProposal = contestType.getFriendlyUrlStringProposal();

        if (contestPhaseId > 0) {

            long activePhaseId = ContestClient.getActivePhase(contest.getContestPK()).getContestPhasePK();
            if (activePhaseId == contestPhaseId) {
                link += "/%d/%s/c/" + friendlyUrlStringProposal + "/%d";
                return String.format(link, contest.getContestYear(), contest.getContestUrlName(), proposalId);
            }

            link += "/%d/%s/phase/%d/" + friendlyUrlStringProposal + "/%d";
            return String.format(link, contest.getContestYear(), contest.getContestUrlName(),
                    contestPhaseId, proposalId);
        }

        link += "/%d/%s/c/" + friendlyUrlStringProposal + "/%d";
        return String.format(link, contest.getContestYear(), contest.getContestUrlName(), proposalId);
    }
    @JsonIgnore
    public boolean isDeleted(){
        final ContestPhase contestPhase = ContestClient.getContestPhase(ProposalsClient.getLatestContestPhaseIdInProposal(this.getProposalId()));
        long visibleAttributeValue = 1;
        if (contestPhase != null) {
            ProposalContestPhaseAttribute pcpa = ProposalsClient.getProposalContestPhaseAttribute(this.getProposalId(),
                    contestPhase.getContestPhasePK(), ProposalContestPhaseAttributeKeys.VISIBLE);
            if(pcpa!=null) {
                visibleAttributeValue = pcpa.getNumericValue();
            }
        }
        return !this.getVisible() || visibleAttributeValue == 0;

    }
    @JsonIgnore
    public boolean isVisibleInContest( long contestId) {
        try{
            final Contest currentContest = ProposalsClient.getCurrentContestForProposal(this.getProposalId());
            return !isDeleted() && currentContest.getContestPK() == contestId;
        }catch (ContestNotFoundException ignored){
            return false;
        }

    }
}
