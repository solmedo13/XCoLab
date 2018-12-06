package org.xcolab.service.contents.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.xcolab.client.content.FilesClient;
import org.xcolab.client.content.pojo.IFileEntry;
import org.xcolab.service.contents.domain.fileentry.FileEntryDao;
import org.xcolab.service.contents.exceptions.NotFoundException;
import org.xcolab.service.contents.providers.FileSystemPersistenceProvider;
import org.xcolab.service.contents.providers.PersistenceProvider;

import java.io.File;
import java.util.Optional;

@RestController
public class FilesController implements FilesClient {

    private static final PersistenceProvider persistenceProvider =
            new FileSystemPersistenceProvider();

    private final FileEntryDao fileEntryDao;

    @Autowired
    public FilesController(FileEntryDao fileEntryDao) {
        Assert.notNull(fileEntryDao, "FileEntryDao is required");
        this.fileEntryDao = fileEntryDao;
    }

    @Override
    @PostMapping("/fileEntries")
    public IFileEntry createFileEntry(@RequestBody IFileEntry fileEntry,
            @RequestParam byte[] imgBArr,
            @RequestParam String path) {
        persistenceProvider.saveFileToFinalDestination(imgBArr, fileEntry, path);
        return this.fileEntryDao.create(fileEntry);
    }

    @Override
    @GetMapping("/imageFile")
    public File getImageFile(@RequestBody IFileEntry fileEntry, @RequestParam String path) {
        String filePath = persistenceProvider.getFilePathFromFinalDestination(fileEntry, path);
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            return file;
        }
        return null;
    }

    @Override
    @GetMapping("/fileEntries/{fileEntryId}")
    public Optional<IFileEntry> getFileEntry(@PathVariable Long fileEntryId) {
        if (fileEntryId != null && fileEntryId > 0) {
            try {
                return Optional.of(this.fileEntryDao.get(fileEntryId));
            } catch (NotFoundException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
