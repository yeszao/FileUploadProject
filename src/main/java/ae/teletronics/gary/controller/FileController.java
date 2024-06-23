package ae.teletronics.gary.controller;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.enums.FileVisibility;
import ae.teletronics.gary.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@Tag(name = "Files")
@Validated
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<File> upload(@RequestParam("file") MultipartFile file,
                                       @RequestParam("visibility") FileVisibility visibility,
                                       @RequestParam("tags") List<String> tags) throws IOException {

        fileService.storeFile(file.getBytes(), file.getOriginalFilename());

        File fileInfo = new File();
        fileInfo.setFileName(file.getOriginalFilename());
        fileInfo.setVisibility(visibility);
        fileInfo.setUserId("USER");
        fileInfo.setSize(file.getSize());
        fileInfo.setContentType(file.getContentType());
        fileInfo.setTags(tags);
        fileInfo.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));

        fileInfo.setUploadedAt(LocalDateTime.now());
        fileInfo.setUpdatedAt(LocalDateTime.now());
        File result = fileService.saveFile(fileInfo);

        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    public ResponseEntity<List<File>> getAll() {
        List<File> files = fileService.findAll();
        return ResponseEntity.ok(files);
    }

    @PutMapping("/{id}")
    public ResponseEntity<File> update(@PathVariable("id") String id,
                                       @RequestParam("filename") String filename) {

        File fileInfo = fileService.findById(id);
        if (fileInfo == null) {
            return ResponseEntity.notFound().build();
        }

        fileInfo.setFileName(filename);
        fileInfo.setUpdatedAt(LocalDateTime.now());
        File result = fileService.saveFile(fileInfo);
        return ResponseEntity.ok(result);
    }

}