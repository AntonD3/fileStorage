package com.example.fileStorage.controller;

import com.example.fileStorage.dto.FilePage;
import com.example.fileStorage.dto.Status;
import com.example.fileStorage.model.File;
import com.example.fileStorage.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/file")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<Status> uploadFile(@RequestBody File file)
    {
        Status status = fileService.save(file);
        if(status.getId() != null)
            return new ResponseEntity<>(status, HttpStatus.OK);
        else
            return new ResponseEntity<>(status, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Status>  deleteFile(@PathVariable String id)
    {
        File file = fileService.delete(id);
        if(file == null)
            return new ResponseEntity<>(new Status(false, "file not found"), HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(new Status(true), HttpStatus.OK);
    }

    @PostMapping("{id}/tags")
    public ResponseEntity<Status> assignTags(@PathVariable String id, @RequestBody Set<String> tags)
    {
        if(fileService.assignTags(id,tags))
            return new ResponseEntity<>(new Status(true), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Status(false, "file not found"), HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("{id}/tags")
    public ResponseEntity<Status> deleteTags(@PathVariable String id, @RequestBody Set<String> tags)
    {
        if(fileService.removeTags(id,tags))
            return new ResponseEntity<>(new Status(true), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Status(false, "tag not found on file"), HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    public FilePage getFiles(@RequestParam(required = false) Set<String> tags,
                             @RequestParam(defaultValue = "0") String page,
                             @RequestParam(defaultValue = "10") String size,
                             @RequestParam(defaultValue = "") String q
    )
    {
        return fileService.getFiles(tags, Integer.parseInt(page), Integer.parseInt(size), q);
    }
}
