package com.ext.portlet.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

/**
 * The base model interface for the ProposalRatingType service. Represents a row in the &quot;xcolab_ProposalRatingType&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.ext.portlet.model.impl.ProposalRatingTypeModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.ext.portlet.model.impl.ProposalRatingTypeImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ProposalRatingType
 * @see com.ext.portlet.model.impl.ProposalRatingTypeImpl
 * @see com.ext.portlet.model.impl.ProposalRatingTypeModelImpl
 * @generated
 */
public interface ProposalRatingTypeModel extends BaseModel<ProposalRatingType> {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this interface directly. All methods that expect a proposal rating type model instance should use the {@link ProposalRatingType} interface instead.
     */

    /**
     * Returns the primary key of this proposal rating type.
     *
     * @return the primary key of this proposal rating type
     */
    public long getPrimaryKey();

    /**
     * Sets the primary key of this proposal rating type.
     *
     * @param primaryKey the primary key of this proposal rating type
     */
    public void setPrimaryKey(long primaryKey);

    /**
     * Returns the ID of this proposal rating type.
     *
     * @return the ID of this proposal rating type
     */
    public long getId();

    /**
     * Sets the ID of this proposal rating type.
     *
     * @param id the ID of this proposal rating type
     */
    public void setId(long id);

    /**
     * Returns the label of this proposal rating type.
     *
     * @return the label of this proposal rating type
     */
    @AutoEscape
    public String getLabel();

    /**
     * Sets the label of this proposal rating type.
     *
     * @param label the label of this proposal rating type
     */
    public void setLabel(String label);

    /**
     * Returns the description of this proposal rating type.
     *
     * @return the description of this proposal rating type
     */
    @AutoEscape
    public String getDescription();

    /**
     * Sets the description of this proposal rating type.
     *
     * @param description the description of this proposal rating type
     */
    public void setDescription(String description);

    /**
     * Returns the judge type of this proposal rating type.
     *
     * @return the judge type of this proposal rating type
     */
    public int getJudgeType();

    /**
     * Sets the judge type of this proposal rating type.
     *
     * @param judgeType the judge type of this proposal rating type
     */
    public void setJudgeType(int judgeType);

    /**
     * Returns the is active of this proposal rating type.
     *
     * @return the is active of this proposal rating type
     */
    public boolean getIsActive();

    /**
     * Returns <code>true</code> if this proposal rating type is is active.
     *
     * @return <code>true</code> if this proposal rating type is is active; <code>false</code> otherwise
     */
    public boolean isIsActive();

    /**
     * Sets whether this proposal rating type is is active.
     *
     * @param isActive the is active of this proposal rating type
     */
    public void setIsActive(boolean isActive);

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
    public int compareTo(
        com.ext.portlet.model.ProposalRatingType proposalRatingType);

    @Override
    public int hashCode();

    @Override
    public CacheModel<com.ext.portlet.model.ProposalRatingType> toCacheModel();

    @Override
    public com.ext.portlet.model.ProposalRatingType toEscapedModel();

    @Override
    public com.ext.portlet.model.ProposalRatingType toUnescapedModel();

    @Override
    public String toString();

    @Override
    public String toXmlString();
}
