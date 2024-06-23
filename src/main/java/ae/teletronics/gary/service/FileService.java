package ae.teletronics.gary.service;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
@AllArgsConstructor
public class FileService {
    private FileRepository fileRepository;

    private final Path rootLocation = Paths.get("uploads");

    public void storeFile(byte[] bytes, String filename) throws IOException {
        Files.write(this.rootLocation.resolve(filename), bytes);
    }

    public File saveFile(File file) {
        return fileRepository.save(file);
    }

    public List<File> findAll() {
        return fileRepository.findAll();
    }

    public File findById(String id) {
        return fileRepository.findById(id).orElse(null);
    }
}
