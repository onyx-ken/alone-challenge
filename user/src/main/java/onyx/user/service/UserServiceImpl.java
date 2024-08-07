package onyx.user.service;


import lombok.RequiredArgsConstructor;
import onyx.user.domain.entity.UserEntity;
import onyx.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserEntity register(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<UserEntity> findUserByOauthProviderId(String providerId) {
        return userRepository.findByOauthInfoProviderId(providerId);
    }

    @Transactional
    public void updateNickName(Long userId, String newNickName) {
        UserEntity user = getUserById(userId);
        user.updateNickName(newNickName);
    }

    @Transactional
    public void updateBio(Long userId, String newBio) {
        UserEntity user = getUserById(userId);
        user.updateBio(newBio);
    }

    /*
    public void updateProfileImage(Long userId, String imageUrl, String imageId) {
        UserEntity user = getUserById(userId);
        user.updateProfileImage(imageUrl, imageId);
        userRepository.save(user);
    }
    */

    @Transactional
    public void updateProfile(Long userId, String newNickName, String newBio, String imageUrl, String imageId) {
        UserEntity user = getUserById(userId);
        user.updateNickName(newNickName);
        user.updateBio(newBio);
        // user.updateProfileImage(imageUrl, imageId);
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자정보를 찾을 수 없습니다. : " + userId));
    }

}
