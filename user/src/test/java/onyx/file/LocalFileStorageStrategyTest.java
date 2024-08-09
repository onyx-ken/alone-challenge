package onyx.file;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class LocalFileStorageStrategyTest {

    @Autowired
    private LocalFileStorageStrategy localFileStorageStrategy;

    @Mock
    private MultipartFile mockMultipartFile;

    @Value("${storage.path}")
    private String uploadDir;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveFileAndReturnFilePath() throws IOException {
        // given
        String subDir = "profile";
        String fileName = "testFile.txt";
        byte[] content = "test content".getBytes();
        mockMultipartFile = new MockMultipartFile("file", fileName, "text/plain", content);
        String expectedFilePath = new File(uploadDir + File.separator + subDir + File.separator + fileName).getAbsolutePath();

        // when
        String savedFilePath = localFileStorageStrategy.saveFile(mockMultipartFile, subDir, fileName);

        // then
        assertThat(savedFilePath).isEqualTo(expectedFilePath);
        File savedFile = new File(savedFilePath);
        assertThat(savedFile).exists();
    }

    @Test
    void shouldDeleteFileSuccessfully() {
        // given
        String subDir = "profile";
        String fileName = "testFileToDelete.txt";
        String filePath = uploadDir + File.separator + subDir + File.separator + fileName;
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Ensure directories are created
        try {
            file.createNewFile(); // Create a dummy file
        } catch (IOException e) {
            e.printStackTrace();
        }

        // when
        localFileStorageStrategy.deleteFile(subDir, fileName);

        // then
        assertThat(file.exists()).isFalse();
    }

    @Test
    void shouldReturnCorrectFileUrl() {
        // given
        String subDir = "profile";
        String fileName = "testFile.txt";
        String expectedFileUrl = new File(uploadDir + File.separator + subDir + File.separator + fileName).getAbsolutePath();

        // when
        String fileUrl = localFileStorageStrategy.getFileUrl(subDir, fileName);

        // then
        assertThat(fileUrl).isEqualTo(expectedFileUrl);
    }
}