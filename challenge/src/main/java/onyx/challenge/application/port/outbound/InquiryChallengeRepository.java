package onyx.challenge.application.port.outbound;

import onyx.challenge.application.dto.ChallengeViewDTO;

import java.util.List;

public interface InquiryChallengeRepository {
    List<ChallengeViewDTO> getChallengeList();
    ChallengeViewDTO getChallengeDetail(Long id);
}
