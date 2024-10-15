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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateChallengeServiceTest {

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private FileStorage fileStorage;

    @Mock
    private ChallengeImageRepository challengeImageRepository;

    @InjectMocks
    private CreateChallengeService createChallengeService;

    @Test
    public void givenValidChallengeInputDTO_whenCreateChallenge_thenReturnsChallengeOutputDTO() {
        // Given
        // 1. 파일 저장과 메타데이터 저장의 목 객체 동작 정의

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

        ChallengeImage savedImage = ChallengeImage.create(
                1L,
                challengeInputDTO.getAttachedImages().getFirst().getFileData().getOriginalFilename(),
                "stored-filename",
                "/path/to/stored-file",
                challengeInputDTO.getAttachedImages().getFirst().getFileData().getContent().length,
                challengeInputDTO.getAttachedImages().getFirst().getFileData().getContentType(),
                challengeInputDTO.getAttachedImages().getFirst().getOrder(),
                challengeInputDTO.getAttachedImages().getFirst().getType()
        );

        given(fileStorage.store(any(byte[].class), anyString())).willReturn("/path/to/stored-file");
        given(challengeImageRepository.saveImage(any(ChallengeImage.class))).willReturn(savedImage);

        // 2. ChallengeRepository 목 객체 동작 정의
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
                .isInstanceOf(IllegalArgumentException.class);
    }

}