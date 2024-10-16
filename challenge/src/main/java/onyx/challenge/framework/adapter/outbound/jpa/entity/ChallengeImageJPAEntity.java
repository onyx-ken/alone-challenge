package onyx.challenge.framework.adapter.outbound.jpa.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.domain.vo.ImageType;

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

    @Column(name = "image_order", nullable = false)
    private int order; // 이미지 순서

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    // 도메인 엔티티로부터 변환 메서드
    public static ChallengeImageJPAEntity fromDomain(ChallengeImage image) {
        return new ChallengeImageJPAEntity(
                image.getId(),
                image.getOriginalFilename(),
                image.getStoredFilename(),
                image.getFilePath(),
                image.getFileSize(),
                image.getContentType(),
                image.getOrder(),
                image.getType()
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
                this.contentType,
                this.order,
                this.imageType
        );
    }

    // 생성자
    private ChallengeImageJPAEntity(Long id, String originalFilename, String storedFilename,
                                    String filePath,long fileSize, String contentType, int order, ImageType imageType) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.storedFilename = storedFilename;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.order = order;
        this.imageType = imageType;
    }

    static ChallengeImageJPAEntity testInstance(Long id, String originalFilename, String storedFilename,
                                       String filePath, long fileSize, String contentType, int order, ImageType imageType) {
        ChallengeImageJPAEntity entity = new ChallengeImageJPAEntity();
        entity.id = id;
        entity.originalFilename = originalFilename;
        entity.storedFilename = storedFilename;
        entity.filePath = filePath;
        entity.fileSize = fileSize;
        entity.contentType = contentType;
        entity.order = order;
        entity.imageType = imageType;
        return entity;
    }
}
