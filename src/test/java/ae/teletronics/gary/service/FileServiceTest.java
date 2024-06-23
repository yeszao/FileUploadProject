package ae.teletronics.gary.service;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.enums.FileVisibility;
import ae.teletronics.gary.repository.FileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    @Mock
    private FileRepository fileRepository;

    @InjectMocks
    private FileService fileService;

    private File testFile;

    @BeforeEach
    public void setup() {
        testFile = new File();
        testFile.setId("123");
        testFile.setFileName("test.txt");
        testFile.setVisibility(FileVisibility.PUBLIC);
        testFile.setUserId("USER");
        testFile.setSize(1024L);
        testFile.setContentType("text/plain");
        testFile.setTags(Collections.singletonList("tag1"));
        testFile.setExtension("txt");
        testFile.setContentMd5("abc123");
        testFile.setUploadedAt(LocalDateTime.now());
        testFile.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    public void testSaveMultipartFile() {
        when(fileRepository.save(any(File.class))).thenReturn(testFile);
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        File savedFile = fileService.saveMultipartFile(file, FileVisibility.PUBLIC, Collections.singletonList("tag1"), "abc123");

        verify(fileRepository, times(1)).save(any(File.class));
    }

    @Test
    public void testSaveFile() {
        when(fileRepository.save(any(File.class))).thenReturn(testFile);

        File updatedFile = new File();
        updatedFile.setId("123");
        updatedFile.setFileName("newFilename.txt");

        File savedFile = fileService.saveFile(updatedFile);

        verify(fileRepository, times(1)).save(any(File.class));
    }

    @Test
    public void testFindById() {
        when(fileRepository.findById(eq("123"))).thenReturn(Optional.of(testFile));

        File foundFile = fileService.findById("123");

        verify(fileRepository, times(1)).findById(eq("123"));
    }

    @Test
    public void testFindFirstByFileNameOrContentMd5() {
        when(fileRepository.findFirstByFileNameOrContentMd5(eq("test.txt"), eq("abc123"))).thenReturn(testFile);

        File foundFile = fileService.findFirstByFileNameOrContentMd5("test.txt", "abc123");

        verify(fileRepository, times(1)).findFirstByFileNameOrContentMd5(eq("test.txt"), eq("abc123"));
    }

    @Test
    public void testDeleteById() {
        doNothing().when(fileRepository).deleteById(eq("123"));

        fileService.deleteById("123");

        verify(fileRepository, times(1)).deleteById(eq("123"));
    }

    @Test
    public void testGetFiles() {
        Page<File> mockPage = new PageImpl<>(Collections.singletonList(testFile));
        when(fileRepository.findByUserIdAndVisibilityAndTagsIn(eq("USER"), eq(FileVisibility.PUBLIC), eq(Collections.singletonList("tag1")), any(PageRequest.class)))
                .thenReturn(mockPage);

        Page<File> resultPage = fileService.getFiles(FileVisibility.PUBLIC, Collections.singletonList("tag1"), 0, 10, "uploadedAt");

        verify(fileRepository, times(1)).findByUserIdAndVisibilityAndTagsIn(eq("USER"), eq(FileVisibility.PUBLIC), eq(Collections.singletonList("tag1")), any(PageRequest.class));
    }
}