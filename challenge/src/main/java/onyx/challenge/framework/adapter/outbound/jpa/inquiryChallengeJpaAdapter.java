package onyx.challenge.framework.adapter.outbound.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.port.outbound.InquiryChallengeRepository;

import java.util.List;

@RequiredArgsConstructor
public class inquiryChallengeJpaAdapter implements InquiryChallengeRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChallengeViewDTO> getChallengeList() {
        return null;
    }

    @Override
    public ChallengeViewDTO getChallengeDetail(Long challengeId) {
        return null;
    }
}
