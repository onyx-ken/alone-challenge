package onyx.user.service;

import lombok.RequiredArgsConstructor;
import onyx.file.FileUtil;
import onyx.file.domain.FileInfo;
import onyx.file.service.FileService;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Profile;
import onyx.user.domain.valueobject.ProfileImage;
import onyx.user.repository.UserRepository;
import onyx.user.web.UserProfileUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInfoServiceImpl implements UserInfoService {

    private final UserRepository userRepository;

    private final FileService fileService;

    @Value("${storage.path.profile}")
    private String uploadDir;

    @Transactional
    @Override
    public void updateUserInfo(Long userId, UserProfileUpdateRequest request) throws IOException {
        UserEntity user = getUserById(userId);

        handleExistingProfileImage(user);  // 기존 프로필 이미지가 있는 경우 삭제
        Optional<FileInfo> optionalFileInfo = handleProfileImageUploadIfPresent(request);
        updateUserProfile(user, request.getNickName(), request.getBio(), optionalFileInfo.orElse(null));
    }

    @Override
    public Optional<UserEntity> getUserInfo(Long userId) {
        return userRepository.findById(userId);
    }

    private void handleExistingProfileImage(UserEntity user) {
        Profile currentProfile = user.getProfile();
        if (currentProfile != null && currentProfile.getProfileImage() != null) {
            FileInfo currentFileInfo = currentProfile.getProfileImage().getFileInfo();

            if (currentFileInfo.getId() != 0) { // 기본값이 아닌 경우에만 처리
                fileService.deleteFileByFileId(currentFileInfo.getId()); // 서버에서 파일 삭제
            }
        }
    }

    private Optional<FileInfo> handleProfileImageUploadIfPresent(UserProfileUpdateRequest request) throws IOException {
        if (!isProfileImagePresent(request)) {
            return Optional.empty(); // 이미지가 없으면 빈 Optional 반환
        }

        String tempName = UUID.randomUUID().toString();
        File file = FileUtil.convertMultipartFileToFile(request.getProfileImage());

        try {
            FileInfo fileInfo = fileService.saveFile(file, uploadDir, tempName);
            return Optional.of(fileInfo);
        } finally {
            deleteTempFile(file);
        }
    }

    private void updateUserProfile(UserEntity user, String nickName, String bio, FileInfo fileInfo) {
        user.updateNickName(nickName);

        if (fileInfo != null) {
            user.updateProfile(Profile.create(ProfileImage.create(fileInfo), bio));
        } else {
            Profile currentProfile = user.getProfile();
            currentProfile.updateBio(bio);
            user.updateProfile(currentProfile);
        }
    }

    private boolean isProfileImagePresent(UserProfileUpdateRequest request) {
        return request.getProfileImage() != null && !request.getProfileImage().isEmpty();
    }

    private void deleteTempFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자정보를 찾을 수 없습니다. : " + userId));
    }

}
