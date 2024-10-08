package onyx.challenge.framework.adapter.outbound.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.model.ChallengeImage;

@Entity
@Table(name = "challenge_images")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChallengeImageJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 이미지 ID (imageId)

    @Column(nullable = false)
    private String originalFilename; // 원본 파일명

    @Column(nullable = false)
    private String storedFilename; // 저장된 파일명 (UUID 등)

    @Column(nullable = false)
    private String filePath; // 파일 시스템의 경로

    @Column(nullable = false)
    private long fileSize; // 파일 크기

    @Column(nullable = false)
    private String contentType; // MIME 타입

    // 도메인 엔티티로부터 변환 메서드
    public static ChallengeImageJPAEntity fromDomain(ChallengeImage image) {
        return new ChallengeImageJPAEntity(
                image.getId(),
                image.getOriginalFilename(),
                image.getStoredFilename(),
                image.getFilePath(),
                image.getFileSize(),
                image.getContentType()
        );
    }

    // 도메인 엔티티로 변환 메서드
    public ChallengeImage toDomain() {
        return ChallengeImage.from(
                this.id,
                this.originalFilename,
                this.storedFilename,
                this.filePath,
                this.fileSize,
                this.contentType
        );
    }

    // 생성자
    private ChallengeImageJPAEntity(Long id, String originalFilename, String storedFilename,
                                    String filePath,long fileSize, String contentType) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
    }

    static ChallengeImageJPAEntity testInstance(Long id, String originalFilename, String storedFilename,
                                       String filePath, long fileSize, String contentType) {
        ChallengeImageJPAEntity entity = new ChallengeImageJPAEntity();
        entity.id = id;
        entity.originalFilename = originalFilename;
        entity.storedFilename = storedFilename;
        entity.filePath = filePath;
        entity.fileSize = fileSize;
        entity.contentType = contentType;
        return entity;
    }
}
