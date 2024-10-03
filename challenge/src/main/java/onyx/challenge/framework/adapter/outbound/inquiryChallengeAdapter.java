package onyx.challenge.framework.adapter.outbound;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.port.outbound.InquiryChallengeRepository;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static onyx.jooq.tables.Challenge.*;

@Repository
@RequiredArgsConstructor
public class inquiryChallengeAdapter implements InquiryChallengeRepository {

    private final DSLContext dsl;

    @Override
    public List<ChallengeViewDTO> getChallengeList() {
        return dsl.select(
                        CHALLENGE.CHALLENGE_ID,
                        CHALLENGE.NICK_NAME
                )
                .from(CHALLENGE)
                .fetchInto(ChallengeViewDTO.class);
    }

    @Override
    public ChallengeViewDTO getChallengeDetail(Long challengeId) {
        return null;
    }
}
