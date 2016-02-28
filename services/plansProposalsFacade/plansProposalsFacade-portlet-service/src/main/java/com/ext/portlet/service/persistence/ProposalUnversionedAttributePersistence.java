package com.ext.portlet.service.persistence;

import com.ext.portlet.model.ProposalUnversionedAttribute;

import com.liferay.portal.service.persistence.BasePersistence;

/**
 * The persistence interface for the proposal unversioned attribute service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ProposalUnversionedAttributePersistenceImpl
 * @see ProposalUnversionedAttributeUtil
 * @generated
 */
public interface ProposalUnversionedAttributePersistence extends BasePersistence<ProposalUnversionedAttribute> {
    /*
     * NOTE FOR DEVELOPERS:
     *
     * Never modify or reference this interface directly. Always use {@link ProposalUnversionedAttributeUtil} to access the proposal unversioned attribute persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this interface.
     */

    /**
    * Returns all the proposal unversioned attributes where proposalId = &#63;.
    *
    * @param proposalId the proposal ID
    * @return the matching proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> findByProposalId_ProposalUnversionedAttribute(
        long proposalId)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns a range of all the proposal unversioned attributes where proposalId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.ext.portlet.model.impl.ProposalUnversionedAttributeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param proposalId the proposal ID
    * @param start the lower bound of the range of proposal unversioned attributes
    * @param end the upper bound of the range of proposal unversioned attributes (not inclusive)
    * @return the range of matching proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> findByProposalId_ProposalUnversionedAttribute(
        long proposalId, int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns an ordered range of all the proposal unversioned attributes where proposalId = &#63;.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.ext.portlet.model.impl.ProposalUnversionedAttributeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param proposalId the proposal ID
    * @param start the lower bound of the range of proposal unversioned attributes
    * @param end the upper bound of the range of proposal unversioned attributes (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of matching proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> findByProposalId_ProposalUnversionedAttribute(
        long proposalId, int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the first proposal unversioned attribute in the ordered set where proposalId = &#63;.
    *
    * @param proposalId the proposal ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching proposal unversioned attribute
    * @throws com.ext.portlet.NoSuchProposalUnversionedAttributeException if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute findByProposalId_ProposalUnversionedAttribute_First(
        long proposalId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the first proposal unversioned attribute in the ordered set where proposalId = &#63;.
    *
    * @param proposalId the proposal ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the first matching proposal unversioned attribute, or <code>null</code> if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute fetchByProposalId_ProposalUnversionedAttribute_First(
        long proposalId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the last proposal unversioned attribute in the ordered set where proposalId = &#63;.
    *
    * @param proposalId the proposal ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching proposal unversioned attribute
    * @throws com.ext.portlet.NoSuchProposalUnversionedAttributeException if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute findByProposalId_ProposalUnversionedAttribute_Last(
        long proposalId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the last proposal unversioned attribute in the ordered set where proposalId = &#63;.
    *
    * @param proposalId the proposal ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the last matching proposal unversioned attribute, or <code>null</code> if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute fetchByProposalId_ProposalUnversionedAttribute_Last(
        long proposalId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the proposal unversioned attributes before and after the current proposal unversioned attribute in the ordered set where proposalId = &#63;.
    *
    * @param id the primary key of the current proposal unversioned attribute
    * @param proposalId the proposal ID
    * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
    * @return the previous, current, and next proposal unversioned attribute
    * @throws com.ext.portlet.NoSuchProposalUnversionedAttributeException if a proposal unversioned attribute with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute[] findByProposalId_ProposalUnversionedAttribute_PrevAndNext(
        long id, long proposalId,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Removes all the proposal unversioned attributes where proposalId = &#63; from the database.
    *
    * @param proposalId the proposal ID
    * @throws SystemException if a system exception occurred
    */
    public void removeByProposalId_ProposalUnversionedAttribute(long proposalId)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the number of proposal unversioned attributes where proposalId = &#63;.
    *
    * @param proposalId the proposal ID
    * @return the number of matching proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public int countByProposalId_ProposalUnversionedAttribute(long proposalId)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the proposal unversioned attribute where proposalId = &#63; and name = &#63; or throws a {@link com.ext.portlet.NoSuchProposalUnversionedAttributeException} if it could not be found.
    *
    * @param proposalId the proposal ID
    * @param name the name
    * @return the matching proposal unversioned attribute
    * @throws com.ext.portlet.NoSuchProposalUnversionedAttributeException if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute findByProposalId_ProposalUnversionedAttributeName(
        long proposalId, java.lang.String name)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the proposal unversioned attribute where proposalId = &#63; and name = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
    *
    * @param proposalId the proposal ID
    * @param name the name
    * @return the matching proposal unversioned attribute, or <code>null</code> if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute fetchByProposalId_ProposalUnversionedAttributeName(
        long proposalId, java.lang.String name)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the proposal unversioned attribute where proposalId = &#63; and name = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
    *
    * @param proposalId the proposal ID
    * @param name the name
    * @param retrieveFromCache whether to use the finder cache
    * @return the matching proposal unversioned attribute, or <code>null</code> if a matching proposal unversioned attribute could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute fetchByProposalId_ProposalUnversionedAttributeName(
        long proposalId, java.lang.String name, boolean retrieveFromCache)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Removes the proposal unversioned attribute where proposalId = &#63; and name = &#63; from the database.
    *
    * @param proposalId the proposal ID
    * @param name the name
    * @return the proposal unversioned attribute that was removed
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute removeByProposalId_ProposalUnversionedAttributeName(
        long proposalId, java.lang.String name)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the number of proposal unversioned attributes where proposalId = &#63; and name = &#63;.
    *
    * @param proposalId the proposal ID
    * @param name the name
    * @return the number of matching proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public int countByProposalId_ProposalUnversionedAttributeName(
        long proposalId, java.lang.String name)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Caches the proposal unversioned attribute in the entity cache if it is enabled.
    *
    * @param proposalUnversionedAttribute the proposal unversioned attribute
    */
    public void cacheResult(
        com.ext.portlet.model.ProposalUnversionedAttribute proposalUnversionedAttribute);

    /**
    * Caches the proposal unversioned attributes in the entity cache if it is enabled.
    *
    * @param proposalUnversionedAttributes the proposal unversioned attributes
    */
    public void cacheResult(
        java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> proposalUnversionedAttributes);

    /**
    * Creates a new proposal unversioned attribute with the primary key. Does not add the proposal unversioned attribute to the database.
    *
    * @param id the primary key for the new proposal unversioned attribute
    * @return the new proposal unversioned attribute
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute create(long id);

    /**
    * Removes the proposal unversioned attribute with the primary key from the database. Also notifies the appropriate model listeners.
    *
    * @param id the primary key of the proposal unversioned attribute
    * @return the proposal unversioned attribute that was removed
    * @throws com.ext.portlet.NoSuchProposalUnversionedAttributeException if a proposal unversioned attribute with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute remove(long id)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    public com.ext.portlet.model.ProposalUnversionedAttribute updateImpl(
        com.ext.portlet.model.ProposalUnversionedAttribute proposalUnversionedAttribute)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the proposal unversioned attribute with the primary key or throws a {@link com.ext.portlet.NoSuchProposalUnversionedAttributeException} if it could not be found.
    *
    * @param id the primary key of the proposal unversioned attribute
    * @return the proposal unversioned attribute
    * @throws com.ext.portlet.NoSuchProposalUnversionedAttributeException if a proposal unversioned attribute with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute findByPrimaryKey(
        long id)
        throws com.ext.portlet.NoSuchProposalUnversionedAttributeException,
            com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the proposal unversioned attribute with the primary key or returns <code>null</code> if it could not be found.
    *
    * @param id the primary key of the proposal unversioned attribute
    * @return the proposal unversioned attribute, or <code>null</code> if a proposal unversioned attribute with the primary key could not be found
    * @throws SystemException if a system exception occurred
    */
    public com.ext.portlet.model.ProposalUnversionedAttribute fetchByPrimaryKey(
        long id) throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns all the proposal unversioned attributes.
    *
    * @return the proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> findAll()
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns a range of all the proposal unversioned attributes.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.ext.portlet.model.impl.ProposalUnversionedAttributeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param start the lower bound of the range of proposal unversioned attributes
    * @param end the upper bound of the range of proposal unversioned attributes (not inclusive)
    * @return the range of proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> findAll(
        int start, int end)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns an ordered range of all the proposal unversioned attributes.
    *
    * <p>
    * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS}), then the query will include the default ORDER BY logic from {@link com.ext.portlet.model.impl.ProposalUnversionedAttributeModelImpl}. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
    * </p>
    *
    * @param start the lower bound of the range of proposal unversioned attributes
    * @param end the upper bound of the range of proposal unversioned attributes (not inclusive)
    * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
    * @return the ordered range of proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public java.util.List<com.ext.portlet.model.ProposalUnversionedAttribute> findAll(
        int start, int end,
        com.liferay.portal.kernel.util.OrderByComparator orderByComparator)
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Removes all the proposal unversioned attributes from the database.
    *
    * @throws SystemException if a system exception occurred
    */
    public void removeAll()
        throws com.liferay.portal.kernel.exception.SystemException;

    /**
    * Returns the number of proposal unversioned attributes.
    *
    * @return the number of proposal unversioned attributes
    * @throws SystemException if a system exception occurred
    */
    public int countAll()
        throws com.liferay.portal.kernel.exception.SystemException;
}