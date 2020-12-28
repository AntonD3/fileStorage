package com.example.fileStorage.service;

import com.example.fileStorage.dto.FilePage;
import com.example.fileStorage.dto.Status;
import com.example.fileStorage.model.File;
import com.example.fileStorage.repository.FileRepository;
import com.example.fileStorage.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class FileServiceImpl implements FileService {

    private FileRepository fileRepository;
    private FileUtils fileUtils;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, FileUtils fileUtils) {
        this.fileRepository = fileRepository;
        this.fileUtils = fileUtils;
    }


    @Override
    public File getById(String id) {
        return fileRepository.findById(id).orElse(null);
    }

    @Override
    public Status save(File file) {
        if(file == null)
            return new Status(false, "invalid body");

        if(!fileUtils.isFilenameValid(file.getName()))
            return new Status(false,"invalid filename");

        if(!fileUtils.isSizeValid(file.getSize()))
            return new Status(false,"invalid size");

        file.setId(null);

        file.setTags(new HashSet<>());
        String contentType = fileUtils.getFileContentType(file.getName());
        if(contentType != null) file.getTags().add(contentType);

        file = fileRepository.save(file);
        return new Status(file.getId());
    }

    @Override
    public File delete(String id) {
        File file = fileRepository.findById(id).orElse(null);
        if(file == null)
            return null;

        fileRepository.deleteById(id);
        return file;
    }

    @Override
    public boolean assignTags(String id, Set<String> tags) {
        File file = fileRepository.findById(id).orElse(null);
        if(file == null)
            return false;

        for(String tag:tags)
            file.getTags().add(tag);

        fileRepository.save(file);
        return true;
    }

    @Override
    public boolean removeTags(String id, Set<String> tags) {
        File file = fileRepository.findById(id).orElse(null);
        if(file == null)
            return false;

        for(String tag:tags)
            if(!file.getTags().contains(tag)) return false;

        for(String tag:tags)
            file.getTags().remove(tag);

        fileRepository.save(file);
        return true;

    }

    @Override
    public FilePage getFiles(Set<String> tags, int page, int size, String q) {
        Pageable pageable = PageRequest.of(page, size);

        Page<File> pageFiles;
        if(tags == null || tags.isEmpty())
            pageFiles = fileRepository.findByNameContainingIgnoreCase(q, pageable);
        else
            pageFiles = fileRepository.findByTagsInAndNameContainingIgnoreCase(tags, q, pageable);

        FilePage filePage = new FilePage(pageFiles.getTotalElements(), pageFiles.getContent());

        return filePage;
    }
}
