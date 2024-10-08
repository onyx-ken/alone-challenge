package onyx.challenge.framework.adapter.inbound.web;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.outbound.CommentRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenges/{challengeId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentRepository commentRepository;
}
