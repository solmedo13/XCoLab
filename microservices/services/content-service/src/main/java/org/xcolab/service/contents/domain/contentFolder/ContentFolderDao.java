package org.xcolab.service.contents.domain.contentFolder;

import org.xcolab.client.content.pojo.IContentFolder;
import org.xcolab.service.utils.PaginationHelper;

import java.util.List;

public interface ContentFolderDao {
    IContentFolder create(IContentFolder IContentFolder);

    boolean update(IContentFolder IContentFolder);

    IContentFolder get(Long contentFolderId);

    List<IContentFolder> findByGiven(PaginationHelper paginationHelper, Long parentFolderId);

    List<IContentFolder> findByAncestorFolderId(long ancestorFolderId);
}
