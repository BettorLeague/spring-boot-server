package server.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.dto.user.UpdateUserInfoRequest;
import server.model.bettor.Player;
import server.model.user.AuthorityName;
import server.model.user.User;
import server.repository.user.AuthorityRepository;
import server.repository.user.UserRepository;
import server.model.user.Authority;
import server.dto.authentification.SignupRequest;
import server.repository.football.TeamRepository;
import server.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final TeamRepository teamRepository;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           TeamRepository teamRepository,
                           AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
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
    public User deleteUser(Long id) {
        User user = userRepository.findOne(id);
        userRepository.delete(id);
        return user;
    }
    public User addUser(User user) {
        return userRepository.save(user);
    }
    public User updateUserInfo(User user, UpdateUserInfoRequest userInfoRequest){
        User current = userRepository.findOne(user.getId());
        current.setUsername(userInfoRequest.getUsername());
        current.setBirthDate(userInfoRequest.getBirthDate());
        current.setCountry(userInfoRequest.getCountry());
        current.setFavoriteTeam(teamRepository.findOne(userInfoRequest.getFavoriteTeamId()));
        current.setSex(userInfoRequest.getSex());
        return this.userRepository.save(current);
    }


    public List<Authority> getAllAuthority() {
        return this.authorityRepository.findAll();
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

    public Set<Player> getPlayers(Long userId){
        if(userRepository.exists(userId)){
            return userRepository.findOne(userId).getPlayers();
        }else return null;
    }






}
