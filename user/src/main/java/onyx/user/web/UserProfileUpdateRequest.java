package onyx.user.web;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserProfileUpdateRequest {
    @NotNull
    private String nickName;
    @NotEmpty
    @Length(max = 150)
    private String bio;
    private MultipartFile profileImage;
}
