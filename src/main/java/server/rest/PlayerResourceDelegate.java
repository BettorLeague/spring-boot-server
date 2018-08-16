package server.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.user.User;
import server.repository.bettor.PlayerRepository;
import server.security.JwtTokenUtil;
import server.service.ContestService;
import server.service.PlayerService;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@Component
public class PlayerResourceDelegate {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;

    public PlayerResourceDelegate(UserService userService,
                                ContestService contestService,
                                PlayerService playerService,
                                PlayerRepository playerRepository,
                                JwtTokenUtil jwtTokenUtil){
        this.userService = userService;
        this.playerService = playerService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.playerRepository = playerRepository;
    }

    public ResponseEntity<Set<Pronostic>> getPronostics(Long contestId,HttpServletRequest request) {

        String token = request.getHeader(tokenHeader);
        User user = userService.getUserByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(playerRepository.existsByUserIdAndContestId(user.getId(),contestId)){
            Player player = playerRepository.findByUserIdAndContestId(user.getId(),contestId);
            return new ResponseEntity<>(this.playerService.getPronostics(player.getId()),HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
