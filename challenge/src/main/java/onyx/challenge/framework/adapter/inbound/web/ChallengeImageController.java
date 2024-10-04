package onyx.challenge.framework.adapter.inbound.web;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.dto.FileData;
import onyx.challenge.application.port.inbound.InquiryChallengeImageUseCase;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenge-images")
@RequiredArgsConstructor
public class ChallengeImageController {

    private final InquiryChallengeImageUseCase inquiryChallengeImageUseCase;

    @GetMapping("/{imageId}")
    public ResponseEntity<Resource> getImage(@PathVariable Long imageId) {
        FileData imageData = inquiryChallengeImageUseCase.getImage(imageId);

        ByteArrayResource resource = new ByteArrayResource(imageData.getContent());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageData.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageData.getOriginalFilename() + "\"")
                .body(resource);
    }
}
