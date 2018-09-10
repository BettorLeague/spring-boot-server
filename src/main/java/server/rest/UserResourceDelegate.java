package server.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.dto.user.PrivateContestRequest;
import server.dto.user.UpdateUserInfoRequest;
import server.dto.user.UserStatsResponse;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Player;
import server.model.user.User;
import server.security.JwtTokenUtil;
import server.service.ContestService;
import server.service.PlayerService;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Set;

import static java.util.Objects.nonNull;

@Component
public class UserResourceDelegate {


    private final UserService userService;
    private final PlayerService playerService;
    private final ContestService contestService;

    public UserResourceDelegate(UserService userService,
                                ContestService contestService,
                                PlayerService playerService){
        this.userService = userService;
        this.playerService = playerService;
        this.contestService = contestService;
    }


    public ResponseEntity<User> getUser(HttpServletRequest request){
        return new ResponseEntity<>(this.userService.getUser(request),HttpStatus.OK);
    }

    public ResponseEntity<User> deleteUser(HttpServletRequest request){
        User user = userService.getUser(request);
        user = this.userService.deleteUser(user.getId());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    public ResponseEntity<List<Player>> getPlayers(HttpServletRequest request){
        User user = userService.getUser(request);
        return new ResponseEntity<>(playerService.getAllByUser(user.getId()), HttpStatus.OK);
    }
    public ResponseEntity<List<Contest>> getContests(ContestType type,HttpServletRequest request){
        User user = userService.getUser(request);
        return new ResponseEntity<>(contestService.getAllContest(user.getId(),type), HttpStatus.OK);
    }

    public ResponseEntity<Player> subscribeContest(Long contestId,HttpServletRequest request) {
        User user = userService.getUser(request);
        Contest contest = contestService.getContestById(contestId);

        if(playerService.isUserPlayingContest(user.getId(),contest.getId())){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }else{
            return new ResponseEntity<>(playerService.subscribeToContest(user.getId(),contest.getId()),HttpStatus.OK);
        }
    }

    public ResponseEntity<Player> unSubscribeContest(Long contestId,HttpServletRequest request) {
        User user = userService.getUser(request);
        Contest contest = contestService.getContestById(contestId);

        if(playerService.isUserPlayingContest(user.getId(),contest.getId())){
            return new ResponseEntity<>(playerService.unSubscribe(user.getId(),contest.getId()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
