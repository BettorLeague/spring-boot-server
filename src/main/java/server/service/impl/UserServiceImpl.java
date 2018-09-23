package server.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.dto.user.UpdateUserInfoRequest;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Player;
import server.model.user.AuthorityName;
import server.model.user.User;
import server.repository.user.AuthorityRepository;
import server.repository.user.UserRepository;
import server.model.user.Authority;
import server.dto.authentification.SignupRequest;
import server.repository.football.TeamRepository;
import server.security.JwtTokenUtil;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    @Value("${jwt.header}")
    private String tokenHeader;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final TeamRepository teamRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TeamRepository teamRepository,
                           JwtTokenUtil jwtTokenUtil,
                           AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
    }

    public boolean existUser(User user){ return this.userRepository.exists(user.getId()); }
    public boolean existUserById(Long userId){
        return this.userRepository.exists(userId);
    }
    public boolean existUserByUsername(String username){
        return this.userRepository.existsByUsername(username);
    }
    public boolean existUserByEmail(String email){
        return this.userRepository.existsByEmail(email);
    }

    public List<User> getAllUser() {
        return this.userRepository.findAll();
    }
    public User getUser(Long id) {
        return userRepository.findOne(id);
    }
    public User getUser(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return this.userRepository.findByEmailOrUsername(username,username);
    }
    public User deleteUser(Long id) {
        if(userRepository.exists(id)){
            User user = userRepository.findOne(id);
            userRepository.delete(id);
            return user;
        }else {
            return null;
        }
    }


    public User getUserByEmail(String email){
        return this.userRepository.findByEmail(email);
    }
    public User getUserByUsername(String username){return this.userRepository.findByUsername(username);}
    public User getUserByUsernameOrEmail(String usernameOrEmail){return this.userRepository.findByEmailOrUsername(usernameOrEmail,usernameOrEmail);}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return org.springframework.security.core.userdetails.User
                    .withUsername(username)
                    .password(user.getPassword())
                    .authorities(user.getAuthorities())
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(false)
                    .build();
        }
    }

    public User signUp(SignupRequest jwtSignupRequest){
        User user = new User();
        user.setUsername(jwtSignupRequest.getUsername());
        user.setPassword(this.passwordEncoder.encode(jwtSignupRequest.getPassword()));
        user.setEmail(jwtSignupRequest.getEmail());
        user.setEnabled(true);
        List<Authority> authorities = new ArrayList<>();
        authorities.add(authorityRepository.findByName(AuthorityName.ROLE_USER));
        user.setAuthorities(authorities);
        return userRepository.save(user);
    }

    public User addAdminAccount(){
        User admin = new User();
        admin.setEmail("c.nadjim@gmail.com");
        admin.setUsername("admin");
        admin.setEnabled(true);
        admin.setAuthorities(new ArrayList<>());
        admin.setPassword(this.passwordEncoder.encode("admin"));

        Authority adminAuthority = new Authority();
        Authority userAuthority = new Authority();

        if(! authorityRepository.existsByName(AuthorityName.ROLE_ADMIN)){
            adminAuthority.setName(AuthorityName.ROLE_ADMIN);
            adminAuthority.setUsers(new ArrayList<>());
            adminAuthority.getUsers().add(admin);
        }

        if(! authorityRepository.existsByName(AuthorityName.ROLE_USER)){
            userAuthority.setName(AuthorityName.ROLE_USER);
            userAuthority.setUsers(new ArrayList<>());
            userAuthority.getUsers().add(admin);
        }

        if(! userRepository.existsByUsername(admin.getUsername())){
            admin.getAuthorities().add(userAuthority);
            admin.getAuthorities().add(adminAuthority);
            admin = userRepository.save(admin);
            return admin;
        }

        return null;



    }


}
