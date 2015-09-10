package com.ext.portlet.model;

import com.ext.portlet.service.persistence.Proposal2PhasePK;

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the Proposal2Phase service. Represents a row in the &quot;xcolab_Proposal2Phase&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.ext.portlet.model.impl.Proposal2PhaseModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.ext.portlet.model.impl.Proposal2PhaseImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see Proposal2Phase
 * @see com.ext.portlet.model.impl.Proposal2PhaseImpl
 * @see com.ext.portlet.model.impl.Proposal2PhaseModelImpl
 * @generated
 */
public interface Proposal2PhaseModel extends BaseModel<Proposal2Phase> {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this interface directly. All methods that expect a proposal2 phase model instance should use the {@link Proposal2Phase} interface instead.
     */

    /**
     * Returns the primary key of this proposal2 phase.
     *
     * @return the primary key of this proposal2 phase
     */
    public Proposal2PhasePK getPrimaryKey();

    /**
     * Sets the primary key of this proposal2 phase.
     *
     * @param primaryKey the primary key of this proposal2 phase
     */
    public void setPrimaryKey(Proposal2PhasePK primaryKey);

    /**
     * Returns the proposal ID of this proposal2 phase.
     *
     * @return the proposal ID of this proposal2 phase
     */
    public long getProposalId();

    /**
     * Sets the proposal ID of this proposal2 phase.
     *
     * @param proposalId the proposal ID of this proposal2 phase
     */
    public void setProposalId(long proposalId);

    /**
     * Returns the contest phase ID of this proposal2 phase.
     *
     * @return the contest phase ID of this proposal2 phase
     */
    public long getContestPhaseId();

    /**
     * Sets the contest phase ID of this proposal2 phase.
     *
     * @param contestPhaseId the contest phase ID of this proposal2 phase
     */
    public void setContestPhaseId(long contestPhaseId);

    /**
     * Returns the version from of this proposal2 phase.
     *
     * @return the version from of this proposal2 phase
     */
    public int getVersionFrom();

    /**
     * Sets the version from of this proposal2 phase.
     *
     * @param versionFrom the version from of this proposal2 phase
     */
    public void setVersionFrom(int versionFrom);

    /**
     * Returns the version to of this proposal2 phase.
     *
     * @return the version to of this proposal2 phase
     */
    public int getVersionTo();

    /**
     * Sets the version to of this proposal2 phase.
     *
     * @param versionTo the version to of this proposal2 phase
     */
    public void setVersionTo(int versionTo);

    /**
     * Returns the sort weight of this proposal2 phase.
     *
     * @return the sort weight of this proposal2 phase
     */
    public int getSortWeight();

    /**
     * Sets the sort weight of this proposal2 phase.
     *
     * @param sortWeight the sort weight of this proposal2 phase
     */
    public void setSortWeight(int sortWeight);

    /**
     * Returns the autopromote candidate of this proposal2 phase.
     *
     * @return the autopromote candidate of this proposal2 phase
     */
    public boolean getAutopromoteCandidate();

    /**
     * Returns <code>true</code> if this proposal2 phase is autopromote candidate.
     *
     * @return <code>true</code> if this proposal2 phase is autopromote candidate; <code>false</code> otherwise
     */
    public boolean isAutopromoteCandidate();

    /**
     * Sets whether this proposal2 phase is autopromote candidate.
     *
     * @param autopromoteCandidate the autopromote candidate of this proposal2 phase
     */
    public void setAutopromoteCandidate(boolean autopromoteCandidate);

    @Override
    public boolean isNew();

    @Override
    public void setNew(boolean n);

    @Override
    public boolean isCachedModel();

    @Override
    public void setCachedModel(boolean cachedModel);

    @Override
    public boolean isEscapedModel();

    @Override
    public Serializable getPrimaryKeyObj();

    @Override
    public void setPrimaryKeyObj(Serializable primaryKeyObj);

    @Override
    public ExpandoBridge getExpandoBridge();

    @Override
    public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

    @Override
    public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

    @Override
    public void setExpandoBridgeAttributes(ServiceContext serviceContext);

    @Override
    public Object clone();

    @Override
    public int compareTo(com.ext.portlet.model.Proposal2Phase proposal2Phase);

    @Override
    public int hashCode();

    @Override
    public CacheModel<com.ext.portlet.model.Proposal2Phase> toCacheModel();

    @Override
    public com.ext.portlet.model.Proposal2Phase toEscapedModel();

    @Override
    public com.ext.portlet.model.Proposal2Phase toUnescapedModel();

    @Override
    public String toString();

    @Override
    public String toXmlString();
}
