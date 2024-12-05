package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.dto.ChallengeViewDetailDTO;

import java.util.List;

public interface InquiryChallengeUseCase {
    List<ChallengeViewDTO> getChallengeList();
    ChallengeViewDetailDTO getChallengeDetail(Long challengeId, int commentPage, int commentSize);
}
