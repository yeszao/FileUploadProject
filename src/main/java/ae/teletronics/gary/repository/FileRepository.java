package ae.teletronics.gary.repository;

import ae.teletronics.gary.entity.File;
import ae.teletronics.gary.enums.FileVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
    // get file by file name or content md5
    File findFirstByFileNameOrContentMd5(String fileName, String contentMd5);
    Page<File> findByUserIdAndVisibilityAndTagsIn(String userId, FileVisibility visibility, List<String> tags, Pageable pageable);

}
