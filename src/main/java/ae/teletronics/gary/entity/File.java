package ae.teletronics.gary.entity;

import ae.teletronics.gary.enums.FileVisibility;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "files")
@Data
public class File {
    @Id
    private String id;
    private String fileName;
    private FileVisibility visibility;
    private List<String> tags;
}
