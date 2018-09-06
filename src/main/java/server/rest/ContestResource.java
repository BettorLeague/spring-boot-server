package server.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.dto.contest.ContestRequest;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.model.user.User;
import server.security.JwtTokenUtil;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController

@Slf4j
public class ContestResource {

    private final ContestResourceDelegate contestResourceDelegate;

    public ContestResource(ContestResourceDelegate contestResourceDelegate){
        this.contestResourceDelegate = contestResourceDelegate;
    }

    @RequestMapping(path = "/api/contest", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<Contest>> getAllContest(
            @RequestParam(value = "type", required=false) ContestType type) {
        return this.contestResourceDelegate.getAllContest(type);
    }

    @RequestMapping(path = "/api/contest", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Contest> addContest(ContestRequest contestRequest, HttpServletRequest request) {
        return this.contestResourceDelegate.addContest(contestRequest,request);
    }

    @RequestMapping(path = "/api/contest/{contestId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Contest> getContestById(@PathVariable("contestId") Long contestId) {
        return this.contestResourceDelegate.getContestById(contestId);
    }

    @RequestMapping(path = "/api/contest/{contestId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteContest(@PathVariable("contestId") Long contestId) {
        this.contestResourceDelegate.deleteContest(contestId);
    }

    @RequestMapping(path = "/api/contest/{contestId}/players", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Player>> getPlayersByContestId(@PathVariable("contestId") Long contestId) {
        return this.contestResourceDelegate.getPlayersByContestId(contestId);
    }

    @RequestMapping(path = "/api/contest/{contestId}/messages", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<Message>> getMessagesByContestId(@PathVariable("contestId") Long contestId) {
        return this.contestResourceDelegate.getMessagesByContestId(contestId);
    }




/*
    @RequestMapping(path = "/api/contest/{contestId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Contest> getContestById(@PathVariable("contestId") Long contestId) {
        return this.contestResourceDelegate.getContestById(contestId);
    }

    @RequestMapping(path = "/api/contest/{contestId}/players", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Player>> getPlayersByContestId(@PathVariable("contestId") Long contestId) {
        return this.contestResourceDelegate.getPlayersByContestId(contestId);
    }

    @RequestMapping(path = "/api/contest/{contestId}/nbPlayers", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Long> getNbPlayerInContest(@PathVariable("contestId") Long contestId) {
        return this.contestResourceDelegate.getNbPlayerInContest(contestId);
    }


    @RequestMapping(path = "/api/contest/{contestId}/players/{playerId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Player> getPlayerByContestIdAndPlayerId(@PathVariable("contestId") Long contestId,@PathVariable("playerId") Long playerId) {
        return this.contestResourceDelegate.getPlayerByContestIdAndPlayerId(contestId,playerId);
    }

    @RequestMapping(path = "/api/user/{userId}/contest", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contest>> getContestPlayedByUserId(@PathVariable("userId") Long userId) {
        return this.contestResourceDelegate.getContestPlayedByUserId(userId);
    }*/

}
