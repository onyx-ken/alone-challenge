package onyx.file.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.common.BaseJPAEntity;

import java.io.File;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileInfo extends BaseJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    private String originalName;

    private String storedName;

    private long size;

    private String format;

    private String contentType;

    public File getFile() {
        return new File(path);
    }

    public static FileInfo create(String path, String name, String originalName, long size, String contentType) {
        return new FileInfo(path, name, originalName, size, contentType);
    }

    private FileInfo(String path, String originalName, String storedName, long size, String contentType) {
        this.path = path;
        this.originalName = originalName;
        this.storedName = storedName;
        this.size = size;
        this.format = getFileExtension(originalName);
        this.contentType = contentType;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex >= 0 ? fileName.substring(dotIndex + 1) : "";
    }

}
