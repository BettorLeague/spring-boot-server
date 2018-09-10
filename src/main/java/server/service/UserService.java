package server.service;

import org.springframework.security.core.userdetails.UserDetails;
import server.dto.authentification.SignupRequest;
import server.dto.user.UpdateUserInfoRequest;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Player;
import server.model.user.Authority;
import server.model.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAllUser();

    User getUser(Long id);
    User getUser(HttpServletRequest request);
    User addUser(User user);
    User updateUserInfo(User user, UpdateUserInfoRequest userInfoRequest);
    User deleteUser(Long id);
    User addAdminAccount();

    boolean existUser(User user);
    boolean existUserById(Long userId);
    boolean existUserByUsername(String username);
    boolean existUserByEmail(String email);

    User getUserByEmail(String email);
    User getUserByUsername(String username);
    User getUserByUsernameOrEmail(String usernameOrEmail);

    List<Authority> getAllAuthority();

    UserDetails loadUserByUsername(String username);
    User signUp(SignupRequest jwtSignupRequest);

}
