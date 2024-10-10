package onyx.challenge;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LikeExceptionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageSource messageSource;

    @Test
    void whenLikeNotFoundExceptionThrown_thenReturnsLocalizedErrorMessageInDefaultLocale() throws Exception {
        // Given
        Long challengeId = 123L;
        Long userId = 456L;

        // Expected error message in default locale (e.g., Korean)
        String expectedErrorMessage = messageSource.getMessage(
                "like.not.found",
                new Object[]{challengeId, userId},
                Locale.getDefault()
        );

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/challenges/{challengeId}/likes", challengeId)
                        .header("X-User-Id", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }

    @Test
    void whenLikeNotFoundExceptionThrown_thenReturnsLocalizedErrorMessageInEnglish() throws Exception {
        // Given
        Long challengeId = 123L;
        Long userId = 456L;

        // Expected error message in English
        String expectedErrorMessage = messageSource.getMessage(
                "like.not.found",
                new Object[]{challengeId, userId},
                Locale.ENGLISH
        );

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/challenges/{challengeId}/likes", challengeId)
                        .header("X-User-Id", userId)
                        .header("Accept-Language", "en") // 영어 로케일 설정
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(expectedErrorMessage));
    }
}
