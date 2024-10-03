package onyx.challenge.framework.adapter.outbound;

import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.port.outbound.InquiryChallengeRepository;
import org.jooq.DSLContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static onyx.jooq.tables.Challenge.CHALLENGE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class InquiryChallengeAdapterIntegrationTest {

    @Autowired
    private InquiryChallengeRepository inquiryChallengeAdapter;

    @Autowired
    private DSLContext dsl;

    @Test
    @DisplayName("getChallengeList() 메서드가 올바르게 챌린지 목록을 반환한다")
    public void testGetChallengeList() {
        // Given
        // 테스트용 데이터 삽입
        dsl.insertInto(CHALLENGE)
                .set(CHALLENGE.USER_ID, 1L)
                .set(CHALLENGE.NICK_NAME, "JohnDoe")
                .execute();

        dsl.insertInto(CHALLENGE)
                .set(CHALLENGE.USER_ID, 2L)
                .set(CHALLENGE.NICK_NAME, "JaneDoe")
                .execute();

        // When
        List<ChallengeViewDTO> result = inquiryChallengeAdapter.getChallengeList();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        // 결과 검증
        ChallengeViewDTO dto1 = result.stream()
                .filter(dto -> dto.getNickName().equals("JohnDoe"))
                .findFirst()
                .orElse(null);
        assertThat(dto1).isNotNull();
        assertThat(dto1.getChallengeId()).isNotNull();

        ChallengeViewDTO dto2 = result.stream()
                .filter(dto -> dto.getNickName().equals("JaneDoe"))
                .findFirst()
                .orElse(null);
        assertThat(dto2).isNotNull();
        assertThat(dto2.getChallengeId()).isNotNull();
    }
}
