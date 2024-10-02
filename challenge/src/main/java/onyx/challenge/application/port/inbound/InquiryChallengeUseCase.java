package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.ChallengeViewDTO;

import java.util.List;

public interface InquiryChallengeUseCase {
    List<ChallengeViewDTO> getChallengeList();
    ChallengeViewDTO getChallengeDetail(Long challengeId);
}
