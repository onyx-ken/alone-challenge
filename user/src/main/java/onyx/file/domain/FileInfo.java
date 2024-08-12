package onyx.file.domain;

import lombok.Getter;

import java.io.File;

@Getter
public class FileInfo {

    private final String path;
    private final String name;
    private final long size;
    private final String format;
    private final String contentType;

    public File getFile() {
        return new File(path);
    }

    public static FileInfo create(String path, String name, long size, String contentType) {
        return new FileInfo(path, name, size, contentType);
    }

    private FileInfo(String path, String name, long size, String contentType) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.format = getFileExtension(name);
        this.contentType = contentType;
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex >= 0 ? fileName.substring(dotIndex + 1) : "";
    }
}
