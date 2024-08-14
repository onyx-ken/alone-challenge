package onyx.file;


import onyx.file.domain.FileInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LocalFileStorageStrategyTest {

    @Autowired
    private LocalFileStorageStrategy localFileStorageStrategy;

    @Value("${storage.path.profile}")
    private String uploadDir;

//    @Test
//    void shouldSaveFileAndReturnFilePath() throws IOException {
//        // given
//        String fileName = "testFile.txt";
//        File tempFile = File.createTempFile("testFile", ".txt");
//
//        // Write some content to the temporary file to set its size
//        try (FileWriter writer = new FileWriter(tempFile)) {
//            writer.write("test content");
//        }
//
//        String expectedFilePath = new File(uploadDir + fileName).getAbsolutePath();
//        long expectedFileSize = tempFile.length();
//        String expectedFileFormat = "txt";
//        String expectedContentType = Files.probeContentType(tempFile.toPath());
//
//        // when
//        FileInfo savedFileInfo = localFileStorageStrategy.saveFile(tempFile, uploadDir, fileName);
//
//        // then
//        assertThat(savedFileInfo.getPath()).isEqualTo(expectedFilePath);
//        assertThat(savedFileInfo.getName()).isEqualTo(fileName);
//        assertThat(savedFileInfo.getSize()).isEqualTo(expectedFileSize);
//        assertThat(savedFileInfo.getFormat()).isEqualTo(expectedFileFormat);
//        assertThat(savedFileInfo.getContentType()).isEqualTo(expectedContentType);
//
//        File savedFile = savedFileInfo.getFile();
//        assertThat(savedFile).exists();
//        assertThat(savedFile.getName()).isEqualTo(fileName);
//        assertThat(savedFile.length()).isEqualTo(expectedFileSize);
//        assertThat(savedFile.getAbsolutePath()).isEqualTo(expectedFilePath);
//    }

    @Test
    void shouldDeleteFileSuccessfully() {
        // given
        String fileName = "testFileToDelete.txt";
        String filePath = uploadDir + fileName;
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Ensure directories are created
        try {
            file.createNewFile(); // Create a dummy file
        } catch (IOException e) {
            e.printStackTrace();
        }

        // when
        localFileStorageStrategy.deleteFile(uploadDir, fileName);

        // then
        assertThat(file.exists()).isFalse();
    }

    @Test
    void shouldReturnCorrectFileInfo() throws IOException {
        // given
        String fileName = "testFile.txt";
        String content = "test content";
        File tempFile = new File(uploadDir + fileName);

        tempFile.getParentFile().mkdirs();

        // Write content to the file
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }

        String expectedFilePath = tempFile.getAbsolutePath();
        long expectedFileSize = tempFile.length();
        String expectedFileFormat = "txt";
        String expectedContentType = Files.probeContentType(tempFile.toPath());

        // when
        FileInfo fileInfo = localFileStorageStrategy.getFileInfo(uploadDir, fileName);

        // then
        assertThat(fileInfo.getPath()).isEqualTo(expectedFilePath);
        assertThat(fileInfo.getOriginalName()).isEqualTo(fileName);
        assertThat(fileInfo.getSize()).isEqualTo(expectedFileSize);
        assertThat(fileInfo.getFormat()).isEqualTo(expectedFileFormat);
        assertThat(fileInfo.getContentType()).isEqualTo(expectedContentType);

        File retrievedFile = fileInfo.getFile();
        assertThat(retrievedFile).exists();
        assertThat(retrievedFile.getName()).isEqualTo(fileName);
        assertThat(retrievedFile.length()).isEqualTo(expectedFileSize);
        assertThat(retrievedFile.getAbsolutePath()).isEqualTo(expectedFilePath);
    }
}