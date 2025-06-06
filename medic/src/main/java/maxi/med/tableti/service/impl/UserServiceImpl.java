package maxi.med.tableti.service.impl;

import lombok.RequiredArgsConstructor;
import maxi.med.tableti.entity.User;
import maxi.med.tableti.repository.UserRepository;
import maxi.med.tableti.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public void createUser(Long id) {
        User user = new User();
        user.id(id);
        userRepository.save(user);
    }
}
