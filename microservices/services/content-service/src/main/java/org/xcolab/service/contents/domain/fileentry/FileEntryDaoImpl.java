package org.xcolab.service.contents.domain.fileentry;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import org.xcolab.client.content.pojo.IFileEntry;
import org.xcolab.model.tables.records.FileEntryRecord;
import org.xcolab.service.contents.exceptions.NotFoundException;

import static org.xcolab.model.Tables.FILE_ENTRY;

@Repository
public class FileEntryDaoImpl implements FileEntryDao {

    private final DSLContext dslContext;

    @Autowired
    public FileEntryDaoImpl(DSLContext dslContext) {
        Assert.notNull(dslContext, "DSLContext is required");
        this.dslContext = dslContext;
    }

    @Override
    public IFileEntry create(IFileEntry IFileEntry) {

        FileEntryRecord ret = this.dslContext.insertInto(FILE_ENTRY)
                .set(FILE_ENTRY.FILE_EXTENSION, IFileEntry.getFileExtension())
                .set(FILE_ENTRY.CREATED_AT, IFileEntry.getCreatedAt())
                .set(FILE_ENTRY.FILE_SIZE, IFileEntry.getFileSize())
                .set(FILE_ENTRY.FILE_NAME, IFileEntry.getFileName())
                .returning(FILE_ENTRY.ID)
                .fetchOne();
        if (ret != null) {
            IFileEntry.setId(ret.getValue(FILE_ENTRY.ID));
            return IFileEntry;
        } else {
            return null;
        }

    }

    @Override
    public IFileEntry get(Long fileEntryid) throws NotFoundException {
        final Record record = this.dslContext.select()
                .from(FILE_ENTRY)
                .where(FILE_ENTRY.ID.eq(fileEntryid))
                .fetchOne();
        if (record == null) {
            throw new NotFoundException();
        }
        return record.into(IFileEntry.class);
    }
}
