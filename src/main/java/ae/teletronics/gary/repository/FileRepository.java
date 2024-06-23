package ae.teletronics.gary.repository;

import ae.teletronics.gary.entity.File;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FileRepository extends MongoRepository<File, String> {
}
