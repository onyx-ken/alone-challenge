package onyx.user.service;


import lombok.RequiredArgsConstructor;
import onyx.user.domain.entity.UserEntity;
import onyx.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public UserEntity register(UserEntity user) {
        return userRepository.save(user);
    }

}
