package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.inbound.InquiryChallengeImageUseCase;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.domain.model.ChallengeImage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryChallengeImageService implements InquiryChallengeImageUseCase {

    private final ChallengeImageRepository challengeImageRepository;
    private final FileStorage fileStorage;

    @Override
    public FileData getImage(Long imageId) {
        // 1. 이미지 메타데이터 조회
        ChallengeImage image = challengeImageRepository.loadImage(imageId);

        // 2. 파일 시스템에서 이미지 데이터 로드
        byte[] content = fileStorage.load(image.getStoredFilename());

        // 3. ChallengeImageData 생성 및 반환
        return FileData.create(image.getOriginalFilename(), content, image.getContentType());
    }
}
