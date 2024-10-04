package onyx.challenge.application.port.inbound;

import onyx.challenge.application.dto.FileData;

public interface InquiryChallengeImageUseCase {
    FileData getImage(Long imageId);
}
