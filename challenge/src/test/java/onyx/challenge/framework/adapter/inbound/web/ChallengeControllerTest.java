package onyx.challenge.framework.adapter.inbound.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import onyx.challenge.application.port.inbound.CreateChallengeUseCase;
import onyx.challenge.application.dto.ChallengeInputDTO;
import onyx.challenge.application.dto.ChallengeOutputDTO;
import onyx.challenge.framework.adapter.inbound.web.api.ApiStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

@WebMvcTest(ChallengeController.class)
public class ChallengeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateChallengeUseCase createChallengeUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        // Given
        CreateRequest request = new CreateRequest();
        request.setUserId(1L);
        request.setNickName("testUser");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(7));
        request.setMainContent("Test Challenge");
        request.setAdditionalContent("Additional Info");
        request.setGoalType("POSITIVE");

        ChallengeOutputDTO outputDTO = new ChallengeOutputDTO(1L);
        Mockito.when(createChallengeUseCase.createChallenge(any(ChallengeInputDTO.class))).thenReturn(outputDTO);

        // When & Then
        mockMvc.perform(post("/challenges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(ApiStatus.SUCCESS.name()))
                .andExpect(jsonPath("$.message").value("챌린지가 정상적으로 생성되었습니다."))
                .andExpect(jsonPath("$.data.challengeId").value(1L));
    }

    @Test
    void whenInvalidInput_thenReturns400() throws Exception {
        // Given
        CreateRequest request = new CreateRequest();
        // 필수 필드를 설정하지 않음

        // When & Then
        mockMvc.perform(post("/challenges")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(ApiStatus.BAD_REQUEST.getStatusCode()))
                .andExpect(jsonPath("$.status").value(ApiStatus.BAD_REQUEST.name()))
                .andExpect(jsonPath("$.message").exists());
    }
}