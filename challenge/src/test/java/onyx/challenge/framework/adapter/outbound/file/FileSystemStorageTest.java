package onyx.challenge.framework.adapter.outbound.file;


import onyx.challenge.application.service.exceptiron.file.FileNotFoundException;
import onyx.challenge.application.service.exceptiron.file.StorageException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileSystemStorageTest {
    private FileSystemStorage fileSystemStorage;
    private Path rootLocation;

    @BeforeEach
    public void setUp() throws IOException {
        rootLocation = Files.createTempDirectory("test-storage");
        fileSystemStorage = new FileSystemStorage(rootLocation.toString());
    }

    @AfterEach
    public void tearDown() throws IOException {
        Files.walk(rootLocation)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    @DisplayName("파일을 저장하고 로드할 수 있다")
    void whenStoreAndLoadFile_thenSuccess() throws IOException {
        // Given
        String storedFilename = "stored-file.txt";
        byte[] data = "테스트 데이터".getBytes(StandardCharsets.UTF_8);

        // When
        String filePath = fileSystemStorage.store(data, storedFilename);

        // Then
        assertThat(filePath).isNotNull();
        Path storedFilePath = Paths.get(filePath);
        assertThat(Files.exists(storedFilePath)).isTrue();

        // Load
        byte[] loadedData = fileSystemStorage.load(storedFilename);
        assertThat(loadedData).isEqualTo(data);
    }

    @Test
    @DisplayName("빈 파일을 저장하려고 하면 StorageException이 발생한다")
    void whenStoreEmptyFile_thenThrowsException() {
        // Given
        String storedFilename = "empty-file.txt";
        byte[] data = new byte[0];

        // When & Then
        assertThatThrownBy(() -> fileSystemStorage.store(data, storedFilename))
                .isInstanceOf(StorageException.class)
                .hasMessageContaining("storage.empty.file");
    }

    @Test
    @DisplayName("존재하지 않는 파일을 로드하려고 하면 StorageFileNotFoundException이 발생한다")
    void whenLoadNonExistentFile_thenThrowsException() {
        // Given
        String storedFilename = "non-existent-file.txt";

        // When & Then
        assertThatThrownBy(() -> fileSystemStorage.load(storedFilename))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining("storage.load.failed");
    }

    @Test
    @DisplayName("파일을 삭제할 수 있다")
    void whenDeleteFile_thenFileIsDeleted() throws IOException {
        // Given
        String storedFilename = "file-to-delete.txt";
        byte[] data = "삭제할 파일 데이터".getBytes(StandardCharsets.UTF_8);
        fileSystemStorage.store(data, storedFilename);

        Path filePath = rootLocation.resolve(storedFilename);
        assertThat(Files.exists(filePath)).isTrue();

        // When
        fileSystemStorage.delete(storedFilename);

        // Then
        assertThat(Files.exists(filePath)).isFalse();
    }
}