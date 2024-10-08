package onyx.challenge.framework.adapter.inbound.web.comment;

import lombok.RequiredArgsConstructor;
import onyx.challenge.application.port.inbound.comment.CreateCommentUseCase;
import onyx.challenge.application.port.inbound.comment.UpdateCommentUseCase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenges/{challengeId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CreateCommentUseCase createCommentUseCase;

    private final UpdateCommentUseCase updateCommentUseCase;


}


