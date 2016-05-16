package org.xcolab.service.activities.domain.activityEntry;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.xcolab.model.tables.pojos.ActivityEntry;
import org.xcolab.model.tables.records.ActivityEntryRecord;
import org.xcolab.service.utils.PaginationHelper;

import java.util.List;

import static org.xcolab.model.Tables.ACTIVITY_ENTRY;

@Repository
public class ActivityEntryDaoImpl implements ActivityEntryDao {

    @Autowired
    private DSLContext dslContext;

    public ActivityEntry create(ActivityEntry activityEntry) {
        ActivityEntryRecord ret = this.dslContext.insertInto(ACTIVITY_ENTRY)
                .set(ACTIVITY_ENTRY.MEMBER_ID, activityEntry.getMemberId())
                .set(ACTIVITY_ENTRY.CREATE_DATE, activityEntry.getCreateDate())
                .set(ACTIVITY_ENTRY.PRIMARY_TYPE, activityEntry.getPrimaryType())
                .set(ACTIVITY_ENTRY.SECONDARY_TYPE, activityEntry.getSecondaryType())
                .set(ACTIVITY_ENTRY.CLASS_PRIMARY_KEY, activityEntry.getClassPrimaryKey())
                .set(ACTIVITY_ENTRY.EXTRA_DATA, activityEntry.getExtraData())
                .set(ACTIVITY_ENTRY.ACTIVITY_ENTRY_TITLE, activityEntry.getActivityEntryTitle())
                .set(ACTIVITY_ENTRY.ACTIVITY_ENTRY_BODY, activityEntry.getActivityEntryBody())
                .set(ACTIVITY_ENTRY.ACTIVITY_ENTRY_NAME, activityEntry.getActivityEntryName())
                .returning(ACTIVITY_ENTRY.ACTIVITY_ENTRY_ID)
                .fetchOne();
        if (ret != null) {
            activityEntry.setActivityEntryId(ret.getValue(ACTIVITY_ENTRY.ACTIVITY_ENTRY_ID));
            return activityEntry;
        } else {
            return null;
        }
    }

    public List<ActivityEntry> findByGiven(PaginationHelper paginationHelper,
                                           Long memberId, List<Long> memberIdsToExclude) {
        final SelectQuery<Record> query = dslContext.select()
                .from(ACTIVITY_ENTRY)
                .getQuery();

        if (memberId != null) {
            query.addConditions(ACTIVITY_ENTRY.MEMBER_ID.eq(memberId));
        }

        if (memberIdsToExclude != null) {
            query.addConditions(ACTIVITY_ENTRY.MEMBER_ID.notIn(memberIdsToExclude));
        }

        query.addOrderBy(ACTIVITY_ENTRY.ACTIVITY_ENTRY_ID.desc());


        query.addLimit(paginationHelper.getStartRecord(), paginationHelper.getLimitRecord());
        return query.fetchInto(ActivityEntry.class);
    }
}
