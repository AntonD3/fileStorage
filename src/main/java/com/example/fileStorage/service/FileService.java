package com.example.fileStorage.service;

import com.example.fileStorage.dto.FilePage;
import com.example.fileStorage.dto.Status;
import com.example.fileStorage.model.File;

import java.util.Set;

public interface FileService {

    File getById(String id);

    Status save(File file);

    File delete(String id);

    boolean assignTags(String id, Set<String> tags);

    boolean removeTags(String id, Set<String> tags);

    FilePage getFiles(Set<String> tags, int page, int size, String q);

}
