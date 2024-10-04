package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.application.service.CreateChallengeService;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateChallengeUseCaseTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private ChallengeImageRepository challengeImageRepository;

    @InjectMocks
    private CreateChallengeService createChallengeService;

    private ChallengeInputDTO challengeInputDTO;

    @BeforeEach
    public void setUp() {
        FileData fileData = FileData.create(
                "테스트 파일.jpg",
                "테스트 데이터".getBytes(StandardCharsets.UTF_8),
                "image/jpeg"
        );

        challengeInputDTO = ChallengeInputDTO.builder()
                .userId(1L)
                .nickName("JohnDoe")
                .startDate(LocalDate.of(2024, 9, 19))
                .endDate(LocalDate.of(2024, 9, 25))
                .mainContent("Run 5K")
                .additionalContent("Do It!")
                .goalType("POSITIVE")
                .attachedImages(List.of(fileData))
                .build();
    }

    @Test
    public void givenValidChallengeInputDTO_whenCreateChallenge_thenReturnsChallengeOutputDTO() {
        // Given
        // 1. 파일 저장과 메타데이터 저장의 목 객체 동작 정의
        ChallengeImage savedImage = ChallengeImage.create(
                1L,
                challengeInputDTO.getAttachedImages().get(0).getOriginalFilename(),
                "stored-filename",
                "/path/to/stored-file",
                challengeInputDTO.getAttachedImages().get(0).getContent().length,
                challengeInputDTO.getAttachedImages().get(0).getContentType()
        );

        given(fileStorage.store(any(byte[].class), anyString())).willReturn("/path/to/stored-file");
        given(challengeImageRepository.saveImage(any(ChallengeImage.class))).willReturn(savedImage);

        // 2. ChallengeRepository의 목 객체 동작 정의
        Challenge challenge = Challenge.create(
                1L,
                challengeInputDTO.getUserId(),
                challengeInputDTO.getNickName(),
                new Period(challengeInputDTO.getStartDate(), challengeInputDTO.getEndDate()),
                GoalContent.create(
                        challengeInputDTO.getMainContent(),
                        challengeInputDTO.getAdditionalContent(),
                        GoalType.valueOf(challengeInputDTO.getGoalType())
                ),
                List.of(savedImage.getId())
        );

        given(challengeRepository.save(any(Challenge.class))).willReturn(challenge);

        // When
        ChallengeOutputDTO result = createChallengeService.createChallenge(challengeInputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(challenge.getChallengeId());

        // 추가 검증
        verify(fileStorage, times(1)).store(any(byte[].class), anyString());
        verify(challengeImageRepository, times(1)).saveImage(any(ChallengeImage.class));
        verify(challengeRepository, times(1)).save(any(Challenge.class));
    }
}