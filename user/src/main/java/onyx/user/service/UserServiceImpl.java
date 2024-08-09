package onyx.user.service;


import lombok.RequiredArgsConstructor;
import onyx.user.domain.entity.UserEntity;
import onyx.user.domain.valueobject.Profile;
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
    public void updateProfile(Long userId, Profile newProfile) {
        UserEntity user = getUserById(userId);
        user.updateProfile(newProfile);
    }

    private UserEntity getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("사용자정보를 찾을 수 없습니다. : " + userId));
    }

}
