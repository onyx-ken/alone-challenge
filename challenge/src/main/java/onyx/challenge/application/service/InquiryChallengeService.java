package onyx.challenge.application.service;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.port.inbound.InquiryChallengeUseCase;
import onyx.challenge.application.port.outbound.InquiryChallengeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InquiryChallengeService implements InquiryChallengeUseCase {

    private final InquiryChallengeRepository inquiryChallengeRepository;

    @Override
    public List<ChallengeViewDTO> getChallengeList() {
        return inquiryChallengeRepository.getChallengeList();
    }

    @Override
    public ChallengeViewDTO getChallengeDetail(Long challengeId) {
        return inquiryChallengeRepository.getChallengeDetail(challengeId);
    }
}
