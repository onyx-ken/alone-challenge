package onyx.user.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserInfoUpdateRequest {
    @NotNull
    private String nickName;
    @NotEmpty
    private String bio;
    private MultipartFile profileImage;
}
