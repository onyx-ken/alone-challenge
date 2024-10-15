package onyx.challenge.application.service;

import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.outbound.ChallengeImageRepository;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.port.outbound.FileStorage;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.model.ChallengeImage;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.ImageType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private ChallengeImageProcessService challengeImageProcessService;

    @InjectMocks
    private CreateChallengeService createChallengeService;

    @Test
    @DisplayName("유효한 ChallengeInputDTO로 챌린지를 생성하면 성공적으로 처리된다")
    public void givenValidChallengeInputDTO_whenCreateChallenge_thenReturnsChallengeOutputDTO() {
        // Given
        FileData fileData = FileData.create(
                "테스트 파일.jpg",
                "테스트 데이터".getBytes(StandardCharsets.UTF_8),
                "image/jpeg"
        );

        List<ChallengeInputDTO.ImageData> attachedImages = new ArrayList<>();
        attachedImages.add(new ChallengeInputDTO.ImageData(
                fileData, 1, ImageType.USER_UPLOAD
        ));

        ChallengeInputDTO challengeInputDTO = ChallengeInputDTO.builder()
                .userId(1L)
                .nickName("JohnDoe")
                .startDate(LocalDate.of(2024, 9, 19))
                .endDate(LocalDate.of(2024, 9, 25))
                .mainContent("Run 5K")
                .additionalContent("Do It!")
                .goalType("POSITIVE")
                .attachedImages(attachedImages)
                .build();

        // Mocking ChallengeImageProcessService to return a mock image ID
        Long mockImageId = 100L;
        given(challengeImageProcessService.processImage(
                any(FileData.class), anyInt(), any(ImageType.class)
        )).willReturn(mockImageId);

        // Mocking ChallengeRepository to return a saved Challenge
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
                List.of(mockImageId)
        );

        given(challengeRepository.save(any(Challenge.class))).willReturn(challenge);

        // When
        ChallengeOutputDTO result = createChallengeService.createChallenge(challengeInputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(challenge.getChallengeId());

        // Verify interactions
        verify(challengeImageProcessService, times(1)).processImage(
                any(FileData.class), anyInt(), any(ImageType.class)
        );
        verify(challengeRepository, times(1)).save(any(Challenge.class));
    }

    @Test
    @DisplayName("필수 필드가 누락된 경우 IllegalArgumentException이 발생한다")
    void givenInvalidChallengeInputDTO_whenCreateChallenge_thenThrowsException() {
        // Given
        ChallengeInputDTO invalidInputDTO = ChallengeInputDTO.builder()
                .userId(null) // 필수 필드 누락
                .nickName("JohnDoe")
                .startDate(LocalDate.of(2024, 9, 19))
                .endDate(LocalDate.of(2024, 9, 25))
                .mainContent("Run 5K")
                .additionalContent("Do It!")
                .goalType("POSITIVE")
                .attachedImages(Collections.emptyList())
                .build();

        // When & Then
        assertThatThrownBy(() -> createChallengeService.createChallenge(invalidInputDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("회원 ID는 필수입니다.");
    }


    @Test
    @DisplayName("첨부 이미지가 없어도 챌린지가 성공적으로 생성된다")
    void givenNoAttachedImages_whenCreateChallenge_thenSuccess() {
        // Given
        ChallengeInputDTO challengeInputDTO = ChallengeInputDTO.builder()
                .userId(1L)
                .nickName("JohnDoe")
                .startDate(LocalDate.of(2024, 9, 19))
                .endDate(LocalDate.of(2024, 9, 25))
                .mainContent("Run 5K")
                .additionalContent("Do It!")
                .goalType("POSITIVE")
                .attachedImages(Collections.emptyList())
                .build();

        // Mocking ChallengeRepository to return a saved Challenge
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
                Collections.emptyList()
        );

        given(challengeRepository.save(any(Challenge.class))).willReturn(challenge);

        // When
        ChallengeOutputDTO result = createChallengeService.createChallenge(challengeInputDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(challenge.getChallengeId());

        // Verify that processImage is not called
        verify(challengeImageProcessService, never()).processImage(any(), anyInt(), any());
        verify(challengeRepository, times(1)).save(any(Challenge.class));
    }
}