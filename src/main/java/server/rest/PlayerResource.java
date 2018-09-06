package server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.dto.contest.MessageRequest;
import server.model.bettor.Message;
import server.model.bettor.Pronostic;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@RestController
public class PlayerResource {
    private final PlayerResourceDelegate playerResourceDelegate;

    public PlayerResource(PlayerResourceDelegate playerResourceDelegate){
        this.playerResourceDelegate = playerResourceDelegate;
    }


    @RequestMapping(path = "/api/player/contest/{contestId}/pronostic", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Set<Pronostic>> getPronostics(@PathVariable("contestId") Long contestId , HttpServletRequest request) {
        return playerResourceDelegate.getPronostics(contestId,request);
    }

    @RequestMapping(path = "/api/player/contest/{contestId}/pronostic", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<Pronostic>> upadtePronostic(@RequestBody List<Pronostic> pronostics, @PathVariable("contestId") Long contestId ,HttpServletRequest request) {
        return null;
    }

    @RequestMapping(path = "/api/player/contest/{contestId}/message", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Message> postMessage(@RequestBody MessageRequest messageRequest, @PathVariable("contestId") Long contestId , HttpServletRequest request) {
        return playerResourceDelegate.postMessage(messageRequest,contestId,request);
    }
}
