package onyx.challenge.framework.adapter.inbound.web;

import lombok.Data;
import onyx.challenge.domain.model.ChallengeImage;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageData {
    private MultipartFile file;
    private int order; // 이미지의 순서
    private ChallengeImage.ImageType type;
}
