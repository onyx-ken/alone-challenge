package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.application.service.exceptiron.ChallengeImageNotFoundException;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.domain.vo.ImageType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ChallengeImageProcessService {

    private final ChallengeImageRepository challengeImageRepository;
    private final FileStorage fileStorage;

    public Long processImage(FileData fileData, int order, ImageType type) {
        if (fileData == null) {
            throw new IllegalArgumentException("파일 데이터는 필수입니다.");
        }

        // 1. 파일 저장
        String storedFilename = UUID.randomUUID().toString();
        String filePath = fileStorage.store(fileData.getContent(), storedFilename);

        // 2. 메타데이터 생성
        ChallengeImage image = ChallengeImage.create(
                null,
                fileData.getOriginalFilename(),
                storedFilename,
                filePath,
                fileData.getContent().length,
                fileData.getContentType(),
                order,
                type
        );

        // 3. 메타데이터 저장
        ChallengeImage savedImage = challengeImageRepository.saveImage(image);

        return savedImage.getId();
    }

    public void deleteImage(Long imageId) {
        // 1. 이미지 메타데이터 조회
        ChallengeImage image = challengeImageRepository.loadImage(imageId)
                .orElseThrow(() -> new ChallengeImageNotFoundException(imageId));

        // 2. 파일 삭제
        fileStorage.delete(image.getStoredFilename());

        // 3. 메타데이터 삭제
        challengeImageRepository.deleteImage(imageId);
    }
}
