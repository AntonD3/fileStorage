package com.example.fileStorage.controller;

import com.example.fileStorage.dto.FilePage;
import com.example.fileStorage.dto.Status;
import com.example.fileStorage.model.File;
import com.example.fileStorage.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
class FileControllerTest {

    @MockBean
    FileService fileService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    void uploadCorrectFile() throws Exception {
        File file = new File();
        file.setName("name.txt");
        file.setSize(123);

        String id = "123";

        when(fileService.save(any(File.class))).thenReturn(new Status(id));

        mockMvc.perform(post("/file")
                .content(objectMapper.writeValueAsString(file))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ID").value(id));
    }

    @Test
    void uploadFileWithInvalidName() throws Exception {
        File file = new File();
        file.setName("name*.txt");
        file.setSize(123);

        when(fileService.save(any(File.class))).thenReturn(new Status(false, "invalid filename"));

        mockMvc.perform(post("/file")
                .content(objectMapper.writeValueAsString(file))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
    }

    @Test
    void uploadFileWithInvalidSize() throws Exception {
        File file = new File();
        file.setName("name.txt");
        file.setSize(-123);

        when(fileService.save(any(File.class))).thenReturn(new Status(false, "invalid size"));

        mockMvc.perform(post("/file")
                .content(objectMapper.writeValueAsString(file))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
    }

    @Test
    void deleteCorrectFile() throws Exception {
        File file = new File();
        file.setId("123");
        file.setName("name.txt");
        file.setSize(123);
        file.setTags(new HashSet<>());
        file.getTags().add("text");

        when(fileService.delete(any(String.class))).thenReturn(file);

        mockMvc.perform(delete("/file/"+file.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    void deleteIncorrectFile() throws Exception {
        File file = new File();
        file.setId("123");
        file.setName("name.txt");
        file.setSize(123);
        file.setTags(new HashSet<>());
        file.getTags().add("text");

        when(fileService.delete(any(String.class))).thenReturn(null);

        mockMvc.perform(delete("/file/"+file.getId()))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
    }

    @Test
    void assignTagsToCorrectFile() throws Exception {
        String id = "123";
        Set<String> tags = new HashSet<>();
        tags.add("tag1");

        when(fileService.assignTags(any(String.class), any(Set.class))).thenReturn(true);

        mockMvc.perform(post("/file/" + id + "/tags")
                .content(objectMapper.writeValueAsString(tags))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    void assignTagsToIncorrectFile() throws Exception {
        String id = "123";
        Set<String> tags = new HashSet<>();
        tags.add("tag1");

        when(fileService.assignTags(any(String.class), any(Set.class))).thenReturn(false);

        mockMvc.perform(post("/file/" + id + "/tags")
                .content(objectMapper.writeValueAsString(tags))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
    }

    @Test
    void deleteTagsValid() throws Exception {
        String id = "123";
        Set<String> tags = new HashSet<>();
        tags.add("tag1");

        when(fileService.removeTags(any(String.class), any(Set.class))).thenReturn(true);

        mockMvc.perform(delete("/file/" + id + "/tags")
                .content(objectMapper.writeValueAsString(tags))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
    }

    @Test
    void deleteTagsInvalid() throws Exception {
        String id = "123";
        Set<String> tags = new HashSet<>();
        tags.add("tag1");

        when(fileService.removeTags(any(String.class), any(Set.class))).thenReturn(false);

        mockMvc.perform(delete("/file/" + id + "/tags")
                .content(objectMapper.writeValueAsString(tags))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
    }

    @Test
    void getFilesWithoutFilteringByName() throws Exception {
        Set<String> tags = new HashSet<>();
        String tag = "tag1";
        tags.add(tag);

        FilePage filePage = new FilePage(1, new ArrayList<>());
        filePage.getPage().add(new File("123","name.txt",123,tags));

        when(fileService.getFiles(any(Set.class),anyInt(),anyInt(),any(String.class))).thenReturn(filePage);

        mockMvc.perform(get("/file")
                .param("tags", tag))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(filePage)));

    }
    @Test
    void getFilesWithFilteringByName() throws Exception {
        Set<String> tags = new HashSet<>();
        String tag = "tag1";
        String q = "me.";
        tags.add(tag);

        FilePage filePage = new FilePage(1, new ArrayList<>());
        filePage.getPage().add(new File("123","name.txt",123,tags));

        when(fileService.getFiles(any(Set.class),anyInt(),anyInt(),any(String.class))).thenReturn(filePage);

        mockMvc.perform(get("/file")
                .param("tags", tag)
                .param("q", q))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(filePage)));

    }
}