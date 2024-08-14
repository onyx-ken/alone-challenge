package onyx.user.service;

import lombok.RequiredArgsConstructor;
import onyx.file.FileStorageStrategy;
import onyx.file.FileUtil;
import onyx.file.domain.FileInfo;
import onyx.file.repository.FileRepository;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Profile;
import onyx.user.domain.valueobject.ProfileImage;
import onyx.user.repository.UserRepository;
import onyx.user.web.UserInfoUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    private final FileStorageStrategy fileStorageStrategy;

    private final FileRepository fileRepository;

    @Value("${storage.path.profile}")
    private String uploadDir;

    @Transactional
    @Override
    public void updateUserInfo(Long userId, UserInfoUpdateRequest request) throws IOException {

        UserEntity user = getUserById(userId);

        String tempName = UUID.randomUUID().toString();

        File file = FileUtil.convertMultipartFileToFile(request.getProfileImage());

        try {
            FileInfo fileInfo = fileStorageStrategy.saveFile(file, uploadDir, tempName);

            Profile profile =
                    Profile.create(ProfileImage.create(fileInfo), request.getBio());

            fileRepository.save(fileInfo);
            user.updateNickName(request.getNickName());
            user.updateProfile(profile);

        } finally {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자정보를 찾을 수 없습니다. : " + userId));
    }

}