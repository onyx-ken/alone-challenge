package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.inbound.CreateChallengeUseCase;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateChallengeService implements CreateChallengeUseCase {

    private final ChallengeRepository challengeRepository;
    private final ChallengeImageRepository challengeImageRepository;
    private final FileStorage fileStorage;

    @Override
    public ChallengeOutputDTO createChallenge(ChallengeInputDTO challengeInputDTO) {
        // 1. 이미지 저장
        List<Long> imageIds = new ArrayList<>();
        List<ChallengeInputDTO.ImageData> images = challengeInputDTO.getAttachedImages();

        if (images != null && !images.isEmpty()) {
            for (ChallengeInputDTO.ImageData imageData : images) {
                Long imageId = processImage(
                        imageData.getFileData(),
                        imageData.getOrder(),
                        imageData.getType()
                );
                imageIds.add(imageId);
            }
        }

        // 2. 도메인 모델 생성
        Challenge challenge = Challenge.create(
                null,
                challengeInputDTO.getUserId(),
                challengeInputDTO.getNickName(),
                new Period(challengeInputDTO.getStartDate(), challengeInputDTO.getEndDate()),
                GoalContent.create(
                        challengeInputDTO.getMainContent(),
                        challengeInputDTO.getAdditionalContent(),
                        GoalType.valueOf(challengeInputDTO.getGoalType())
                ),
                imageIds
        );

        // 3. Challenge 저장
        Challenge savedChallenge = challengeRepository.save(challenge);

        return ChallengeOutputDTO.from(savedChallenge, 0);
    }

    private Long processImage(FileData fileData, int order, ChallengeImage.ImageType type) {
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

}
