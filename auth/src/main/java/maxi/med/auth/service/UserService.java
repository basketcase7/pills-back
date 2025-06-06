package maxi.med.auth.service;

import java.util.Date;
import lombok.RequiredArgsConstructor;
import maxi.med.auth.client.MedicClient;
import maxi.med.auth.dto.request.RegisterBody;
import maxi.med.auth.dto.response.SessionResponse;
import maxi.med.auth.dto.response.UserResponse;
import maxi.med.auth.entity.Session;
import maxi.med.auth.entity.User;
import maxi.med.auth.exception.BadTransactionException;
import maxi.med.auth.jwt.JwtTokenUtils;
import maxi.med.auth.mapper.SessionMapper;
import maxi.med.auth.mapper.UserMapper;
import maxi.med.auth.repository.SessionRepository;
import maxi.med.auth.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final MedicClient medicPedicClient;

    @Transactional
    public SessionResponse registerUser(RegisterBody body) {
        User user = UserMapper.mapRegisterBodyToUser(body);
        validateUser(user);

        userRepository.save(user);

        String token = jwtTokenUtils.generateToken(user);
        Date expiredDate = jwtTokenUtils.getExpirationDateFromToken(token);

        Session session = new Session();
        session.user(user);
        session.token(token);
        session.expirationTime(expiredDate);
        session.active(true);

        session = sessionRepository.save(session);

        medicPedicClient.createUser(user.id());

        return SessionMapper.mapSessionResponse(session);
    }

    private void validateUser(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }

    public UserResponse getUserResponseByAuthentication(Authentication authentication) {
        Long id = jwtTokenUtils.getUserIdFromAuthentication(authentication);
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));

        return UserMapper.mapUserToResponse(user);
    }

    public User getUserByAuthentication(Authentication authentication) {
        Long id = jwtTokenUtils.getUserIdFromAuthentication(authentication);

        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
    }

    public User findUserByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadTransactionException("User not found for the given phone number"));
    }

    public User getUserById(Long uuid) {
        return userRepository
                .findById(uuid)
                .orElseThrow(() -> new BadTransactionException("User not found for the given phone number"));
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        return UserMapper.mapUserToResponse(user);
    }

    public UserResponse findUserById(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with ID: " + id + " not found"));
        return UserMapper.mapUserToResponse(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + username + " not found"));
    }
}
