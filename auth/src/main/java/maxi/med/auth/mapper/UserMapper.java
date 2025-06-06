package maxi.med.auth.mapper;

import maxi.med.auth.dto.request.RegisterBody;
import maxi.med.auth.dto.response.UserResponse;
import maxi.med.auth.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserMapper {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static User mapRegisterBodyToUser(RegisterBody body) {
        User user = new User();
        user.password(passwordEncoder.encode(body.password()));
        user.firstName(body.firstName());
        user.lastName(body.lastName());
        user.email(body.email());
        return user;
    }

    public static UserResponse mapUserToResponse(User user) {
        return new UserResponse(user.id().toString(), user.firstName(), user.lastName(), user.email());
    }
}
