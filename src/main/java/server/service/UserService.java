package server.service;

import org.springframework.security.core.userdetails.UserDetails;
import server.dto.authentification.SignupRequest;
import server.model.user.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    User getUser(Long id);
    User getUser(HttpServletRequest request);
    User deleteUser(Long id);
    User addAdminAccount();

    boolean existUser(User user);
    boolean existUserById(Long userId);
    boolean existUserByUsername(String username);
    boolean existUserByEmail(String email);

    User getUserByEmail(String email);
    User getUserByUsername(String username);
    User getUserByUsernameOrEmail(String usernameOrEmail);

    UserDetails loadUserByUsername(String username);
    User signUp(SignupRequest jwtSignupRequest);

}
