package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.port.inbound.CreateChallengeUseCase;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.EventPublisher;
import onyx.challenge.domain.event.ChallengeCreatedEvent;
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
public class CreateChallengeService implements CreateChallengeUseCase {

    private final ChallengeRepository challengeRepository;

    private final ChallengeImageProcessService challengeImageProcessService;

    private final EventPublisher eventPublisher;

    @Override
    public ChallengeOutputDTO createChallenge(ChallengeInputDTO challengeInputDTO) {
        // 1. 이미지 저장
        List<Long> imageIds = new ArrayList<>();
        List<ChallengeInputDTO.ImageData> images = challengeInputDTO.getAttachedImages();
        if (images != null && !images.isEmpty()) {
            images.stream()
                    .map(imageData -> challengeImageProcessService // 파일 저장과 메타데이터 저장 위임
                            .processImage(imageData.getFileData(), imageData.getOrder(), imageData.getType()))
                    .forEach(imageIds::add); // 저장된 imageId를 리스트에 추가
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

        // 4/ Challenge 이벤트 생성 및 발행
        eventPublisher.publish(ChallengeCreatedEvent.create(savedChallenge.getChallengeId(), challengeInputDTO.getUserId()));

        return ChallengeOutputDTO.from(savedChallenge, 0);
    }
}
