package server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.dto.user.*;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Player;
import server.model.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
public class UserResource {

    private final UserResourceDelegate userResourceDelegate;

    public UserResource(UserResourceDelegate userResourceDelegate){
        this.userResourceDelegate = userResourceDelegate;
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<User> deleteAccount(HttpServletRequest request) {
        return userResourceDelegate.deleteUser(request);
    }

    @RequestMapping(path = "/api/user/players", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Player>> getPlayers(HttpServletRequest request) {
        return userResourceDelegate.getPlayers(request);
    }

    @RequestMapping(path = "/api/user/contest", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Contest>> getContests(@RequestParam(value = "type", required=false) ContestType type,
                                                    HttpServletRequest request) {
        return userResourceDelegate.getContests(type,request);
    }

    @RequestMapping(path = "/api/user/contest/{contestId}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Player> subscribeContest(@PathVariable("contestId") Long contestId,HttpServletRequest request) {
        return userResourceDelegate.subscribeContest(contestId,request);
    }

    @RequestMapping(path = "/api/user/contest/{contestId}", method = RequestMethod.PATCH)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Player> unSubscribeContest(@PathVariable("contestId") Long contestId,HttpServletRequest request) {
        return userResourceDelegate.unSubscribeContest(contestId,request);
    }




}
