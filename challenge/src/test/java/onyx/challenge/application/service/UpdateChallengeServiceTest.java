package onyx.challenge.application.service;

import onyx.challenge.application.dto.ChallengeModifyDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.outbound.ChallengeRepository;
import onyx.challenge.application.service.exceptiron.ChallengeNotFoundException;
import onyx.challenge.domain.model.Challenge;
import onyx.challenge.domain.vo.GoalContent;
import onyx.challenge.domain.vo.GoalType;
import onyx.challenge.domain.vo.ImageType;
import onyx.challenge.domain.vo.Period;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateChallengeServiceTest {
    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private ChallengeImageProcessService challengeImageProcessService;

    @InjectMocks
    private UpdateChallengeService updateChallengeService;

    @Test
    @DisplayName("유효한 ChallengeModifyDTO로 챌린지를 업데이트하면 성공적으로 처리된다")
    void givenValidChallengeModifyDTO_whenUpdateChallenge_thenSuccess() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String originalNickName = "OriginalNick";
        Period originalPeriod = new Period(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));
        GoalContent originalGoalContent = GoalContent.create("OriginalGoal", "OriginalAdditional", GoalType.POSITIVE);
        List<Long> originalAttachedImageIds = Arrays.asList(10L, 11L);

        Challenge existingChallenge = Challenge.create(
                challengeId, userId, originalNickName, originalPeriod, originalGoalContent, originalAttachedImageIds
        );

        // 새로운 이미지 데이터 생성
        FileData newFileData = FileData.create(
                "newImage.jpg",
                "new image data".getBytes(StandardCharsets.UTF_8),
                "image/jpeg"
        );
        ChallengeModifyDTO.ImageData newImageData = new ChallengeModifyDTO.ImageData(
                null, // 새로운 이미지이므로 imageId는 null
                newFileData,
                1,
                ImageType.USER_UPLOAD
        );

        // 기존 이미지를 유지하기 위한 데이터 생성
        ChallengeModifyDTO.ImageData existingImageData = new ChallengeModifyDTO.ImageData(
                11L, // 기존 이미지 ID
                null,
                2,
                ImageType.USER_UPLOAD
        );

        List<ChallengeModifyDTO.ImageData> imageDataList = Arrays.asList(existingImageData, newImageData);

        ChallengeModifyDTO modifyDTO = ChallengeModifyDTO.builder()
                .challengeId(challengeId)
                .nickName("UpdatedNick")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .mainContent("UpdatedGoal")
                .additionalContent("UpdatedAdditional")
                .goalType("NEGATIVE")
                .attachedImages(imageDataList)
                .build();

        // 모의 객체 설정
        given(challengeRepository.load(challengeId)).willReturn(Optional.of(existingChallenge));

        // 새로운 이미지 처리 결과 모의 설정
        Long newImageId = 20L;
        given(challengeImageProcessService.processImage(
                any(FileData.class), anyInt(), any(ImageType.class)
        )).willReturn(newImageId);

        // Challenge 저장 모의 설정 (저장된 객체를 그대로 반환)
        given(challengeRepository.save(any(Challenge.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        ChallengeOutputDTO result = updateChallengeService.updateChallenge(modifyDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(challengeId);

        // 추가 검증
        verify(challengeRepository, times(1)).load(challengeId);
        verify(challengeImageProcessService, times(1)).processImage(
                eq(newFileData), eq(1), eq(ImageType.USER_UPLOAD)
        );
        verify(challengeImageProcessService, times(1)).deleteImage(eq(10L)); // 삭제할 이미지 ID
        verify(challengeRepository, times(1)).save(any(Challenge.class));

        // 저장된 Challenge의 이미지 ID 검증
        ArgumentCaptor<Challenge> challengeCaptor = ArgumentCaptor.forClass(Challenge.class);
        verify(challengeRepository).save(challengeCaptor.capture());
        Challenge savedChallenge = challengeCaptor.getValue();
        List<Long> expectedImageIds = Arrays.asList(11L, 20L); // 기존 이미지 11L, 새로운 이미지 20L
        assertThat(savedChallenge.getAttachedImageIds()).containsExactlyInAnyOrderElementsOf(expectedImageIds);
    }

    @Test
    @DisplayName("존재하지 않는 챌린지를 업데이트하려고 하면 ChallengeNotFoundException이 발생한다")
    void givenNonExistingChallengeId_whenUpdateChallenge_thenThrowsException() {
        // Given
        Long challengeId = 999L;
        ChallengeModifyDTO modifyDTO = ChallengeModifyDTO.builder()
                .challengeId(challengeId)
                .nickName("UpdatedNick")
                .build();

        given(challengeRepository.load(challengeId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> updateChallengeService.updateChallenge(modifyDTO))
                .isInstanceOf(ChallengeNotFoundException.class)
                .hasMessageContaining("challenge.not.found");

        // 추가 검증
        verify(challengeRepository, times(1)).load(challengeId);
        verifyNoMoreInteractions(challengeRepository);
        verifyNoInteractions(challengeImageProcessService);
    }

    @Test
    @DisplayName("이미지 없이 챌린지를 업데이트하면 기존 이미지를 삭제한다")
    void givenNoImages_whenUpdateChallenge_thenExistingImagesAreDeleted() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        String originalNickName = "OriginalNick";
        Period originalPeriod = new Period(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7));
        GoalContent originalGoalContent = GoalContent.create("OriginalGoal", "OriginalAdditional", GoalType.POSITIVE);
        List<Long> originalAttachedImageIds = Arrays.asList(10L, 11L);

        Challenge existingChallenge = Challenge.create(
                challengeId, userId, originalNickName, originalPeriod, originalGoalContent, originalAttachedImageIds
        );

        ChallengeModifyDTO modifyDTO = ChallengeModifyDTO.builder()
                .challengeId(challengeId)
                .nickName("UpdatedNick")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(14))
                .mainContent("UpdatedGoal")
                .additionalContent("UpdatedAdditional")
                .goalType("NEGATIVE")
                .attachedImages(Collections.emptyList()) // 이미지 없음
                .build();

        given(challengeRepository.load(challengeId)).willReturn(Optional.of(existingChallenge));
        given(challengeRepository.save(any(Challenge.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        ChallengeOutputDTO result = updateChallengeService.updateChallenge(modifyDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getChallengeId()).isEqualTo(challengeId);

        // 모의 객체 상호 작용 검증
        verify(challengeRepository, times(1)).load(challengeId);
        verify(challengeImageProcessService, never()).processImage(any(), anyInt(), any());
        verify(challengeImageProcessService, times(2)).deleteImage(anyLong());
        verify(challengeImageProcessService).deleteImage(10L);
        verify(challengeImageProcessService).deleteImage(11L);
        verify(challengeRepository, times(1)).save(any(Challenge.class));
    }

    @Test
    @DisplayName("기존 이미지를 모두 삭제하면 deleteImage가 모두 호출된다")
    void givenAllImagesToDelete_whenUpdateChallenge_thenAllImagesDeleted() {
        // Given
        Long challengeId = 1L;
        Long userId = 100L;
        List<Long> originalAttachedImageIds = Arrays.asList(10L, 11L);

        Challenge existingChallenge = Challenge.create(
                challengeId, userId, "OriginalNick",
                new Period(LocalDate.now().minusDays(7), LocalDate.now().plusDays(7)),
                GoalContent.create("OriginalGoal", "OriginalAdditional", GoalType.POSITIVE),
                originalAttachedImageIds
        );

        ChallengeModifyDTO modifyDTO = ChallengeModifyDTO.builder()
                .challengeId(challengeId)
                .nickName("UpdatedNick")
                .attachedImages(Collections.emptyList()) // 이미지 없음 -> 모든 이미지 삭제
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .goalType("NEGATIVE")
                .build();

        given(challengeRepository.load(challengeId)).willReturn(Optional.of(existingChallenge));
        given(challengeRepository.save(any(Challenge.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        updateChallengeService.updateChallenge(modifyDTO);

        // Then
        // 모의 객체 상호 작용 검증
        verify(challengeImageProcessService, never()).processImage(any(), anyInt(), any());
        verify(challengeImageProcessService, times(2)).deleteImage(anyLong());
        verify(challengeImageProcessService).deleteImage(10L);
        verify(challengeImageProcessService).deleteImage(11L);
    }
}
