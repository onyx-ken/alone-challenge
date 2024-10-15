package onyx.challenge.framework.adapter.inbound.web;

import lombok.Data;
import onyx.challenge.domain.vo.ImageType;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageData {
    private MultipartFile file;
    private int order; // 이미지의 순서
    private ImageType type;
}
