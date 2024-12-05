package onyx.challenge.application.port.outbound;

import onyx.challenge.application.dto.ChallengeViewDTO;
import onyx.challenge.application.dto.ChallengeViewDetailDTO;

import java.util.List;

public interface InquiryChallengeRepository {
    List<ChallengeViewDTO> getChallengeList();
    ChallengeViewDetailDTO getChallengeDetail(Long id, int commentPage, int commentSize);
}
