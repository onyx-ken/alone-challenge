package onyx.challenge.framework.adapter.outbound;

import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.application.service.exceptiron.ChallengeImageNotFoundException;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.framework.adapter.outbound.file.FileSystemStorage;
import onyx.challenge.framework.adapter.outbound.jpa.image.ChallengeImageJpaRepository;
import onyx.challenge.framework.adapter.outbound.jpa.entity.ChallengeImageJPAEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChallengeImageJpaAdapterIntegrationTest {

    @Autowired
    private ChallengeImageRepository challengeImageRepository;

    @Autowired
    private ChallengeImageJpaRepository challengeImageJpaRepository;

    @Autowired
    private FileStorage fileStorage;

    private Path rootLocation;

    @BeforeEach
    public void setUp() throws IOException {
        // 테스트용 임시 디렉토리 생성
        rootLocation = Files.createTempDirectory("test-storage");
        // FileStorage의 rootLocation을 테스트용 디렉토리로 설정
        if (fileStorage instanceof FileSystemStorage) {
            ((FileSystemStorage) fileStorage).setRootLocation(Path.of(rootLocation.toString()));
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        // 테스트 종료 후 임시 디렉토리 삭제
        Files.walk(rootLocation)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    @Test
    @DisplayName("ChallengeImage를 실제로 저장하고 조회할 수 있다")
    public void whenSaveAndFindChallengeImage_thenSuccess() throws IOException {
        // Given
        String originalFilename = "test-image.jpg";
        String storedFilename = UUID.randomUUID().toString();
        byte[] fileContent = "테스트 이미지 데이터".getBytes(StandardCharsets.UTF_8);
        String contentType = "image/jpeg";
        long fileSize = fileContent.length;

        // 파일 저장
        String filePath = fileStorage.store(fileContent, storedFilename);

        // 메타데이터 생성
        ChallengeImage image = ChallengeImage.create(
                null,
                originalFilename,
                storedFilename,
                filePath,
                fileSize,
                contentType
        );

        // When
        ChallengeImage savedImage = challengeImageRepository.saveImage(image);

        // Then
        // 저장된 이미지의 ID가 생성되었는지 확인
        assertThat(savedImage).isNotNull();
        assertThat(savedImage.getId()).isNotNull();

        // 데이터베이스에서 조회하여 검증
        Optional<ChallengeImageJPAEntity> foundEntityOptional = challengeImageJpaRepository.findById(savedImage.getId());
        assertThat(foundEntityOptional).isPresent();

        ChallengeImageJPAEntity foundEntity = foundEntityOptional.get();
        assertThat(foundEntity.getOriginalFilename()).isEqualTo(originalFilename);
        assertThat(foundEntity.getStoredFilename()).isEqualTo(storedFilename);
        assertThat(foundEntity.getFilePath()).isEqualTo(filePath);
        assertThat(foundEntity.getFileSize()).isEqualTo(fileSize);
        assertThat(foundEntity.getContentType()).isEqualTo(contentType);

        // 파일 시스템에서 파일이 존재하는지 확인
        Path storedFilePath = rootLocation.resolve(storedFilename);
        assertThat(Files.exists(storedFilePath)).isTrue();

        // 파일 내용을 검증
        byte[] storedFileContent = Files.readAllBytes(storedFilePath);
        assertThat(storedFileContent).isEqualTo(fileContent);
    }

    @Test
    @DisplayName("존재하지 않는 ChallengeImage를 조회하려고 하면 예외가 발생한다")
    public void whenFindNonExistentChallengeImage_thenThrowsException() {
        // Given
        Long nonExistentId = 999L;

        // When & Then
        assertThatThrownBy(() -> challengeImageRepository.loadImage(nonExistentId))
                .isInstanceOf(ChallengeImageNotFoundException.class);
    }
}
