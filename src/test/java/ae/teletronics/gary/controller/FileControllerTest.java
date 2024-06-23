package ae.teletronics.gary.controller;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.enums.FileVisibility;
import ae.teletronics.gary.service.FileService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(FileController.class)
class FileControllerTest {

    @MockBean
    private FileService fileService;

    @InjectMocks
    private FileController fileController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(fileController).build();
    }

    @Test
    public void testFileUpload() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        when(fileService.findFirstByFileNameOrContentMd5(anyString(), anyString())).thenReturn(null);
        when(fileService.saveMultipartFile(any(), any(), any(), anyString())).thenReturn(new File());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/files/upload")
                        .file(file)
                        .param("visibility", FileVisibility.PUBLIC.toString())
                        .param("tags", "tag1", "tag2"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testFileDownload() throws Exception {
        String fileId = "123";
        File file = new File();
        file.setFileName("test.txt");
        file.setContentType(MediaType.TEXT_PLAIN_VALUE);
        when(fileService.findById(eq(fileId))).thenReturn(file);
        when(fileService.getFile(anyString())).thenReturn("Hello, World!".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/files/download")
                        .param("id", fileId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filename=\"test.txt\""));
    }

    @Test
    public void testGetAllFiles() throws Exception {
        // Mocking parameters
        FileVisibility visibility = FileVisibility.PUBLIC;
        List<String> tags = Collections.singletonList("tag1");
        int page = 0;
        int size = 10;
        String sortBy = "uploadedAt";

        // Mocking the service method
        Page<File> mockPage = new PageImpl<>(Collections.singletonList(new File()));
        when(fileService.getFiles(eq(visibility), eq(tags), eq(page), eq(size), eq(sortBy))).thenReturn(mockPage);

        // Performing GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/files")
                        .param("visibility", visibility.toString())
                        .param("tags", tags.get(0))
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sortBy", sortBy))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1))); // Adjust as per your expected JSON response structure
    }

    @Test
    public void testUpdateFile() throws Exception {
        String fileId = "123";
        String newFilename = "newFilename.txt";
        File existingFile = new File();
        existingFile.setId(fileId);
        existingFile.setFileName("oldFilename.txt");

        when(fileService.findById(eq(fileId))).thenReturn(existingFile);
        when(fileService.saveFile(any())).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/files/{id}", fileId)
                        .param("filename", newFilename))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(fileId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fileName", Matchers.is(newFilename)));
    }

    @Test
    public void testDeleteFile() throws Exception {
        String fileId = "123";
        File existingFile = new File();
        existingFile.setId(fileId);

        when(fileService.findById(eq(fileId))).thenReturn(existingFile);
        doNothing().when(fileService).deleteById(eq(fileId));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/files/{id}", fileId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


}