package ae.teletronics.gary.controller;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.enums.FileVisibility;
import ae.teletronics.gary.service.FileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
        fileInfo.setTags(tags);

        File result = fileService.saveFile(fileInfo);

        return ResponseEntity.ok(result);
    }

}