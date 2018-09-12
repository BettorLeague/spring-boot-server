package server.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.dto.contest.MessageRequest;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.user.User;
import server.repository.bettor.PlayerRepository;
import server.repository.bettor.PronosticRepository;
import server.repository.football.MatchRepository;
import server.security.JwtTokenUtil;
import server.service.ContestService;
import server.service.PlayerService;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class PlayerResourceDelegate {

    @Value("${jwt.header}")
    private String tokenHeader;

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final PronosticRepository pronosticRepository;

    public PlayerResourceDelegate(UserService userService,
                                ContestService contestService,
                                PlayerService playerService,
                                PlayerRepository playerRepository,
                                PronosticRepository pronosticRepository,
                                MatchRepository matchRepository,
                                JwtTokenUtil jwtTokenUtil){
        this.userService = userService;
        this.playerService = playerService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.pronosticRepository = pronosticRepository;
        this.playerRepository = playerRepository;
        this.matchRepository = matchRepository;
    }

    public ResponseEntity<List<Pronostic>> getPronostics(Long contestId,HttpServletRequest request) {

        String token = request.getHeader(tokenHeader);
        User user = userService.getUserByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(playerRepository.existsByUserIdAndContestId(user.getId(),contestId)){
            Player player = playerRepository.findByUserIdAndContestId(user.getId(),contestId);
            return new ResponseEntity<>(this.playerService.getPronostics(player.getId()),HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Pronostic>> updatePronostics(Long contestId, List<Pronostic> pronostics, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userService.getUserByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(playerRepository.existsByUserIdAndContestId(user.getId(),contestId)){
            Player player = playerRepository.findByUserIdAndContestId(user.getId(),contestId);
            for(Pronostic pronostic: pronostics){
                pronostic.setPlayer(player);
                pronostic.setMatch(matchRepository.findOne(pronostic.getMatch().getId()));
                playerService.savePronostic(pronostic);
            }
            return new ResponseEntity<>(pronostics,HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<Message> postMessage(MessageRequest messageRequest, Long contestId, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userService.getUserByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(playerRepository.existsByUserIdAndContestId(user.getId(),contestId)){
            Player player = playerRepository.findByUserIdAndContestId(user.getId(),contestId);
            return new ResponseEntity<>(this.playerService.postMessage(messageRequest,contestId,player.getId()),HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Player> getPlayer(Long contestId, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userService.getUserByUsername(jwtTokenUtil.getUsernameFromToken(token));

        if(playerRepository.existsByUserIdAndContestId(user.getId(),contestId)){
            Player player = playerRepository.findByUserIdAndContestId(user.getId(),contestId);
            return new ResponseEntity<>(player,HttpStatus.OK);
        }else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
