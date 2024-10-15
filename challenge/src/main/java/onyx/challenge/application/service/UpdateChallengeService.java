package onyx.challenge.application.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeModifyDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.port.inbound.UpdateChallengeUseCase;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.service.exceptiron.ChallengeNotFoundException;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateChallengeService implements UpdateChallengeUseCase {

    private final ChallengeRepository challengeRepository;
    private final ChallengeImageProcessService challengeImageProcessService;

    @Override
    public ChallengeOutputDTO updateChallenge(ChallengeModifyDTO dto) {

        // 1. 챌린지 존재 여부 확인
        Challenge challenge = challengeRepository.load(dto.getChallengeId())
                .orElseThrow(() -> new ChallengeNotFoundException(dto.getChallengeId()));

        // 2. 이미지 분류
        ImageProcessingResult imageProcessingResult = classifyImages(dto.getAttachedImages());

        // 3. 삭제할 이미지 결정

        List<Long> imageIdsToDelete = challenge.determineImagesToDelete(imageProcessingResult.getExistingImageIds());

        // 4. 삭제할 이미지 처리
        deleteImages(imageIdsToDelete);

        // 5. 업데이트된 이미지 ID 리스트 생성
        List<Long> updatedImageIds =
                mergeImageIds(imageProcessingResult.getExistingImageIds(), imageProcessingResult.getNewImageIds());

        // 6. 챌린지 업데이트
        Challenge updatedChallenge = challenge.update(
                dto.getNickName(),
                new Period(dto.getStartDate(), dto.getEndDate()),
                GoalContent.create(
                        dto.getMainContent(),
                        dto.getAdditionalContent(),
                        GoalType.valueOf(dto.getGoalType())
                ),
                updatedImageIds,
                null,
                challenge.isActive()
        );

        // 7. 챌린지 저장
        Challenge savedChallenge = challengeRepository.save(updatedChallenge);

        return ChallengeOutputDTO.from(savedChallenge, 0);
    }

    private ImageProcessingResult classifyImages(List<ChallengeModifyDTO.ImageData> images) {
        List<Long> newImageIds = new ArrayList<>();
        List<Long> existingImageIds = new ArrayList<>();

        if (images != null && !images.isEmpty()) {
            for (ChallengeModifyDTO.ImageData imageData : images) {
                if (imageData.getImageId() != null) {
                    // 기존 이미지
                    existingImageIds.add(imageData.getImageId());
                } else {
                    // 새로운 이미지
                    Long imageId = challengeImageProcessService.processImage(
                            imageData.getFileData(),
                            imageData.getOrder(),
                            imageData.getType()
                    );
                    newImageIds.add(imageId);
                }
            }
        }

        return new ImageProcessingResult(existingImageIds, newImageIds);
    }

    private void deleteImages(List<Long> imageIdsToDelete) {
        if (!imageIdsToDelete.isEmpty()) {
            for (Long imageId : imageIdsToDelete) {
                challengeImageProcessService.deleteImage(imageId);
            }
        }
    }

    private List<Long> mergeImageIds(List<Long> existingImageIds, List<Long> newImageIds) {
        List<Long> updatedImageIds = new ArrayList<>();
        updatedImageIds.addAll(existingImageIds);
        updatedImageIds.addAll(newImageIds);
        return updatedImageIds;
    }

    // ImageProcessingResult 내부 클래스
    @Getter
    private static class ImageProcessingResult {
        private final List<Long> existingImageIds;
        private final List<Long> newImageIds;

        public ImageProcessingResult(List<Long> existingImageIds, List<Long> newImageIds) {
            this.existingImageIds = existingImageIds;
            this.newImageIds = newImageIds;
        }

    }
}
