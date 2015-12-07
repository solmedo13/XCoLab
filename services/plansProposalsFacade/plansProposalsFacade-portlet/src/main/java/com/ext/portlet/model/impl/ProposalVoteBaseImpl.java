package com.ext.portlet.model.impl;

import com.ext.portlet.model.ProposalVote;
import com.ext.portlet.service.ProposalVoteLocalServiceUtil;

import com.liferay.portal.kernel.exception.SystemException;

/**
 * The extended model base implementation for the ProposalVote service. Represents a row in the &quot;xcolab_ProposalVote&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link ProposalVoteImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ProposalVoteImpl
 * @see com.ext.portlet.model.ProposalVote
 * @generated
 */
public abstract class ProposalVoteBaseImpl extends ProposalVoteModelImpl
    implements ProposalVote {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this class directly. All methods that expect a proposal vote model instance should use the {@link ProposalVote} interface instead.
     */
    @Override
    public void persist() throws SystemException {
        if (this.isNew()) {
            ProposalVoteLocalServiceUtil.addProposalVote(this);
        } else {
            ProposalVoteLocalServiceUtil.updateProposalVote(this)
        }
    }
}
