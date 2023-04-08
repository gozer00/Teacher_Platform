package com.example.bachelorarbeit;

import com.example.bachelorarbeit.controllers.files.FileController;
import com.example.bachelorarbeit.fileUpload.FileStorageService;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;



@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private FileController fileController;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(fileController)
                .build();
    }

    @Test
    public void testUploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt",
                "text/plain", "Test data".getBytes());

        when(fileStorageService.storeFile(file)).thenReturn("test.txt");

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload-file")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName").value("test.txt"))
                .andExpect(jsonPath("$.fileDownloadUri").value("http://localhost:8080/download-file/test.txt"))
                .andExpect(jsonPath("$.fileType").value("text/plain"))
                .andExpect(jsonPath("$.size").value("9"));
    }

    @Test
    public void testDownloadFile() throws Exception {
        Resource resource = new ByteArrayResource("Test data".getBytes());

        when(fileStorageService.loadFileAsResource("test.txt")).thenReturn(resource);

        mockMvc.perform(MockMvcRequestBuilders.get("/download-file/test.txt"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"test.txt\""))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, "9"))
                .andExpect(content().bytes("Test data".getBytes()));
    }

    @Test
    public void testDeleteFile() throws Exception {
        String fileName = "test.txt";
        String fileUri = "http://localhost:8080/download-file/" + fileName;

        when(fileStorageService.delete(fileUri)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-file/" + fileName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Delete the file successfully: " + fileUri));

        when(fileStorageService.delete(fileUri)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-file/" + fileName))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("The file does not exist!"));

        when(fileStorageService.delete(fileUri)).thenThrow(new IOException("Error deleting file"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/delete-file/" + fileName))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Could not delete the file: " + fileUri + ". Error: Error deleting file"));
    }

}
