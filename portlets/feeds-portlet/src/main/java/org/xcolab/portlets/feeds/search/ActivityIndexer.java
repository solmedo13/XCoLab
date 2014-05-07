package org.xcolab.portlets.feeds.search;

import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BaseIndexer;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.BooleanQueryFactoryUtil;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.ParseException;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchEngineUtil;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.search.Summary;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.social.model.SocialActivity;
import com.liferay.portlet.social.service.SocialActivityLocalServiceUtil;

import javax.portlet.PortletURL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class indexes all User activities that are already clustered together (like seen in the Feeds portlet)
 *
 * Created by kmang on 07/05/14.
 */
public class ActivityIndexer extends BaseIndexer {

    public static final String CLASS_NAME_ID_KEY = "classNameId";
    public static final String CLASS_PK_KEY = "classPK";
    public static final String USER_ID_KEY = "userId";
    public static final String CREATE_DATE_KEY = "createDate";
    private static final String ACTIVITY_ID_KEY = "activityId";

    private static final long AGGREGATION_TIME_WINDOW = 1000 * 60 * 60 * 24l; // 1d

    public static final String[] CLASS_NAMES = {SocialActivity.class.getName()};

    public static final String PORTLET_ID = PortletKeys.USERS_ADMIN;

    private static Log _log = LogFactoryUtil.getLog(ActivityIndexer.class);

    @Override
    protected void doDelete(Object obj) throws Exception {

    }

    @Override
    protected Document doGetDocument(Object obj) throws Exception {
        return getSocialActivityDocument(getAggregatedActivity((SocialActivity)obj));
    }

    @Override
    protected Summary doGetSummary(Document document, Locale locale, String snippet, PortletURL portletURL) throws Exception {
        return null;
    }

    @Override
    protected void doReindex(Object obj) throws Exception {
        SocialActivity aggregatedActivity = getAggregatedActivity((SocialActivity)obj);

        SearchEngineUtil.updateDocument(SearchEngineUtil.getDefaultSearchEngineId(), aggregatedActivity.getCompanyId(), getSocialActivityDocument(aggregatedActivity));
    }

    @Override
    protected void doReindex(String className, long classPK) throws Exception {
        doReindex(SocialActivityLocalServiceUtil.getSocialActivity(classPK));
    }

    @Override
    protected void doReindex(String[] ids) throws Exception {
        reindexActivities();
    }

    @Override
    protected String getPortletId(SearchContext searchContext) {
        return PORTLET_ID;
    }

    @Override
    public String[] getClassNames() {
        return CLASS_NAMES;
    }

    @Override
    public String getPortletId() {
        return PORTLET_ID;
    }

    private SocialActivity getAggregatedActivity(SocialActivity sa) {
        try {
            SearchContext context = new SearchContext();
            context.setCompanyId(10112L);
            BooleanQuery query = BooleanQueryFactoryUtil.create(context);
            query.addRequiredTerm(Field.ENTRY_CLASS_NAME, CLASS_NAMES[0]);

            BooleanQuery subQuery = BooleanQueryFactoryUtil.create(context);
            subQuery.addExactTerm(CLASS_NAME_ID_KEY, sa.getClassNameId());
            query.add(subQuery, BooleanClauseOccur.MUST);

            subQuery = BooleanQueryFactoryUtil.create(context);
            subQuery.addExactTerm(CLASS_PK_KEY, sa.getClassPK());
            query.add(subQuery, BooleanClauseOccur.MUST);

            subQuery = BooleanQueryFactoryUtil.create(context);
            subQuery.addExactTerm(USER_ID_KEY, sa.getUserId());
            query.add(subQuery, BooleanClauseOccur.MUST);

            // Try to get a similar activity in the time frame
            subQuery = BooleanQueryFactoryUtil.create(context);
            subQuery.addNumericRangeTerm(CREATE_DATE_KEY, 0L, (sa.getCreateDate() - AGGREGATION_TIME_WINDOW));
            query.add(subQuery, BooleanClauseOccur.MUST_NOT);

            Hits hits = SearchEngineUtil.search(context, query);

            if (hits.getLength() > 0) {
                Document activityDoc =  hits.getDocs()[0];
                try {
                    return SocialActivityLocalServiceUtil.getSocialActivity(GetterUtil.getLong(activityDoc.getField(ACTIVITY_ID_KEY).getValue()));
                } catch (SystemException e) {
                    e.printStackTrace();
                } catch (PortalException e) {
                    e.printStackTrace();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SearchException e) {
            e.printStackTrace();
        }

        return sa;
    }

    private Document getSocialActivityDocument(SocialActivity sa) throws SystemException {
        Document document = getBaseModelDocument(PORTLET_ID, sa);

        document.addNumber(ACTIVITY_ID_KEY, sa.getActivityId());
        document.addNumber(CLASS_NAME_ID_KEY, sa.getClassNameId());
        document.addNumber(CLASS_PK_KEY, sa.getClassPK());
        document.addNumber(USER_ID_KEY, sa.getUserId());
        document.addNumber(CREATE_DATE_KEY, sa.getCreateDate());

        return document;
    }

    private void reindexActivities() throws SystemException {
        List<SocialActivity> allActivities = SocialActivityLocalServiceUtil.getSocialActivities(QueryUtil.ALL_POS, QueryUtil.ALL_POS);
        List<SocialActivity> aggregatedActivities = groupActivities(allActivities);

        for (SocialActivity sa : aggregatedActivities) {
            try {
                SearchEngineUtil.updateDocument(SearchEngineUtil.getDefaultSearchEngineId(), sa.getCompanyId(), getSocialActivityDocument(sa));
            } catch (SearchException |SystemException e) {
                _log.error("Could not reindex SocialActivity with primary key " + sa.getActivityId(), e);
            }
        }
    }

    private List<SocialActivity> groupActivities(List<SocialActivity> activities) {
        //find all activities of same type
        Map<String, List<SocialActivity>> activitiesMap = new HashMap<>(10000);
        for (SocialActivity a : activities) {
            if (!activitiesMap.containsKey(getSocialActivityKey(a))) {
                activitiesMap.put(getSocialActivityKey(a), new LinkedList<SocialActivity>());
            }
            activitiesMap.get(getSocialActivityKey(a)).add(a);
        }

        //cluster
        List<SocialActivity> aggregatedActivities = new LinkedList<>();
        Comparator<SocialActivity> sorter = new Comparator<SocialActivity>() {
            @Override
            public int compare(SocialActivity o1, SocialActivity o2) {
                return new Long(o1.getCreateDate()).compareTo(o2.getCreateDate());
            }
        };
        for (Collection<SocialActivity> sal : activitiesMap.values()) {
            List<SocialActivity> ascending = new ArrayList<>(sal); //convert to array for sorting
            Collections.sort(ascending, sorter);

            SocialActivity curMin = null;
            for (SocialActivity sa : ascending) {
                if (curMin == null || sa.getCreateDate() - curMin.getCreateDate() < AGGREGATION_TIME_WINDOW) curMin = sa;
                else {
                    aggregatedActivities.add(curMin);
                    curMin = sa;
                }
            }
            aggregatedActivities.add(curMin);
        }
        //horrible code start
        Collections.sort(aggregatedActivities, sorter);
        Collections.reverse(aggregatedActivities);
        //horrible code end

        return aggregatedActivities;
    }

    private String getSocialActivityKey(SocialActivity sa) {
        return sa.getClassNameId() + "_" + sa.getClassPK() + "_" + sa.getType() + "_" + sa.getUserId();
    }
}
