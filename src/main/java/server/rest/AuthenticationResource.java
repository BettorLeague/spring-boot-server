package server.rest;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import server.dto.authentification.AuthenticationRequest;
import server.model.user.User;
import server.security.JwtTokenUtil;
import server.dto.authentification.AuthenticationResponse;
import server.dto.authentification.SignupRequest;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;

import static java.util.Objects.isNull;

@RestController
public class AuthenticationResource {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = jwtTokenUtil.generateToken(authentication);

        // Return the token
        return new ResponseEntity<>(new AuthenticationResponse(token),HttpStatus.OK);
    }


    @RequestMapping(value = "${jwt.route.authentication.current.user}", method = RequestMethod.GET)
    public ResponseEntity<?> getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsernameOrEmail(username);

        if (isNull(user)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }else {
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
    }

    @ApiResponses(value = {@ApiResponse(code = 409, message = "Username / Email is already in use")})
    @RequestMapping(value = "${jwt.route.authentication.signup}", method = RequestMethod.POST)
    public ResponseEntity<User> createUser(@RequestBody SignupRequest jwtSignupRequest){
        if(userService.existUserByUsername(jwtSignupRequest.getUsername()) || userService.existUserByEmail(jwtSignupRequest.getEmail())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else {
            return new ResponseEntity<>(userService.signUp(jwtSignupRequest),HttpStatus.OK);
        }
    }



}
