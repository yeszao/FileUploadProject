package ae.teletronics.gary.service;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.enums.FileVisibility;
import ae.teletronics.gary.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    private FileRepository fileRepository;

    private final Path rootLocation = Paths.get("uploads");
    private final String userId = "USER";

    public void storeFile(byte[] bytes, String filename) throws IOException {
        Files.write(this.rootLocation.resolve(filename), bytes);
    }

    public File saveMultipartFile(MultipartFile multipartFile,
                                  FileVisibility visibility,
                                  List<String> tags,
                                  String contentMd5) {
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setVisibility(visibility);
        file.setUserId(userId);
        file.setSize(multipartFile.getSize());
        file.setContentType(multipartFile.getContentType());
        file.setTags(tags);

        // To be more strict, we should use Tika to detect the file type
        file.setExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        file.setContentMd5(contentMd5);

        file.setUploadedAt(LocalDateTime.now());
        file.setUpdatedAt(LocalDateTime.now());

        return fileRepository.save(file);
    }

    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    public List<File> findAll() {
        // filter by visibility, user, tags, etc.


        return fileRepository.findAll();
    }

    public File findById(String id) {
        return fileRepository.findById(id).orElse(null);
    }

    public File findFirstByFileNameOrContentMd5(String fileName, String contentMd5) {
        return fileRepository.findFirstByFileNameOrContentMd5(fileName, contentMd5);
    }

    public void deleteById(String id) {
        fileRepository.deleteById(id);
    }

    public Page<File> getFiles(FileVisibility visibility, List<String> tags, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return fileRepository.findByUserIdAndVisibilityAndTagsIn(userId, visibility, tags, pageable);
    }
}
