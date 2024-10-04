package onyx.challenge.framework.adapter.inbound.web;

import onyx.challenge.application.dto.ChallengeViewDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InquiryResponseTest {

    @Test
    public void testCreate_withValidData() {
        // Given
        Long challengeId = 1L;
        String nickName = "Tester";
        LocalDate startDate = LocalDate.of(2024, 10, 4);
        LocalDate endDate = LocalDate.of(2024, 10, 10);
        String mainContent = "Test main content";
        String additionalContent = "Test additional content";
        String type = "POSITIVE";
        int likeCount = 22;
        List<Long> attachedImageIds = Arrays.asList(100L, 101L, 102L);
        ChallengeViewDTO dto = new ChallengeViewDTO(challengeId, nickName, startDate, endDate,
                mainContent, additionalContent, type, likeCount, attachedImageIds);

        String imageBaseUrl = "https://example.com/images/";

        // When
        InquiryResponse response = InquiryResponse.create(dto, imageBaseUrl);

        // Then
        assertNotNull(response);
        assertEquals(dto.getChallengeId(), response.getChallengeId());
        assertEquals(dto.getNickName(), response.getNickName());
        assertEquals(dto.getStartDate(), response.getStartDate());
        assertEquals(dto.getEndDate(), response.getEndDate());
        assertEquals(dto.getMainContent(), response.getMainContent());
        assertEquals(dto.getAdditionalContent(), response.getAdditionalContent());
        assertEquals(dto.getType(), response.getType());
        assertEquals(dto.getLikeCount(), response.getLikeCount());

        List<String> expectedUrls = Arrays.asList(
                imageBaseUrl + "100",
                imageBaseUrl + "101",
                imageBaseUrl + "102"
        );
        assertEquals(expectedUrls, response.getAttachedImagesUrl());
    }

    @Test
    public void testCreate_withEmptyAttachedImages() {
        // Given
        Long challengeId = 1L;
        String nickName = "Tester";
        LocalDate startDate = LocalDate.of(2024, 10, 4);
        LocalDate endDate = LocalDate.of(2024, 10, 10);
        String mainContent = "Test main content";
        String additionalContent = "Test additional content";
        String type = "POSITIVE";
        int likeCount = 22;
        List<Long> attachedImageIds = Collections.emptyList();

        ChallengeViewDTO dto = new ChallengeViewDTO(challengeId, nickName, startDate, endDate,
                mainContent, additionalContent, type, likeCount, attachedImageIds);

        String imageBaseUrl = "https://example.com/images/";

        // When
        InquiryResponse response = InquiryResponse.create(dto, imageBaseUrl);

        // Then
        assertNotNull(response);
        assertEquals(Collections.emptyList(), response.getAttachedImagesUrl());
    }

    @Test
    public void testCreate_withNullAttachedImages() {
        // Given
        Long challengeId = 1L;
        String nickName = "Tester";
        LocalDate startDate = LocalDate.of(2024, 10, 4);
        LocalDate endDate = LocalDate.of(2024, 10, 10);
        String mainContent = "Test main content";
        String additionalContent = "Test additional content";
        String type = "POSITIVE";
        int likeCount = 22;
        List<Long> attachedImageIds = null;
        ChallengeViewDTO dto = new ChallengeViewDTO(challengeId, nickName, startDate, endDate,
                mainContent, additionalContent, type, likeCount, attachedImageIds);

        String imageBaseUrl = "https://example.com/images/";

        // When
        InquiryResponse response = InquiryResponse.create(dto, imageBaseUrl);

        // Then
        assertNotNull(response);
        assertNotNull(response.getAttachedImagesUrl());
        assertEquals(Collections.emptyList(), response.getAttachedImagesUrl());
    }

    @Test
    public void testCreate_withNullImageBaseUrl() {
        // Given
        Long challengeId = 1L;
        String nickName = "Tester";
        LocalDate startDate = LocalDate.of(2024, 10, 4);
        LocalDate endDate = LocalDate.of(2024, 10, 10);
        String mainContent = "Test main content";
        String additionalContent = "Test additional content";
        String type = "POSITIVE";
        int likeCount = 22;
        List<Long> attachedImageIds = Arrays.asList(100L, 101L, 102L);
        ChallengeViewDTO dto = new ChallengeViewDTO(challengeId, nickName, startDate, endDate,
                mainContent, additionalContent, type, likeCount, attachedImageIds);

        String imageBaseUrl = null; // imageBaseUrl이 null인 경우

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            InquiryResponse.create(dto, imageBaseUrl);
        });
    }
}