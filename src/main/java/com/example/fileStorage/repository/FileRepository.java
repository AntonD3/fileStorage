package com.example.fileStorage.repository;

import com.example.fileStorage.model.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Set;

public interface FileRepository extends ElasticsearchRepository<File, String> {
    Page<File> findByTagsAndNameContainingIgnoreCase(Set<String> tags, String q, Pageable pageable);

    Page<File> findByNameContainingIgnoreCase(String q, Pageable pageable);

}
