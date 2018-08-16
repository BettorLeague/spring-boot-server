package server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import server.dto.contest.ContestRequest;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.service.ContestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Component
public class ContestResourceDelegate {

    private final ContestService contestService;

    public ContestResourceDelegate(ContestService contestService){
        this.contestService = contestService;
    }

    public ResponseEntity<Set<Contest>> getAllContest(ContestType type) {
        return new ResponseEntity<>(this.contestService.getAllContest(type), HttpStatus.OK);
    }

    public ResponseEntity<Contest> addContest(ContestRequest contest, HttpServletRequest request) {
        if(contest.getCaption() == null || contest.getType() == null || contest.getCompetitionId() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(this.contestService.addContest(contest,request), HttpStatus.OK);
    }

    public ResponseEntity<Contest> getContestById(Long contestId) {
        Contest result = this.contestService.getContestById(contestId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(this.contestService.getContestById(contestId), HttpStatus.OK);
    }

    public void deleteContest(Long contestId) {
        this.contestService.deleteContest(contestId);
    }

    public ResponseEntity<Set<Player>> getPlayersByContestId( Long contestId) {
        Set<Player> result = this.contestService.getPlayersByContestId(contestId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.contestService.getPlayersByContestId(contestId), HttpStatus.OK);
    }

    public ResponseEntity<Set<Message>> getMessagesByContestId(Long contestId) {
        Set<Message> result = this.contestService.getMessagesByContestId(contestId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(this.contestService.getMessagesByContestId(contestId), HttpStatus.OK);
    }


}
