package server.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.dto.contest.ContestRequest;
import server.model.bettor.*;
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

    public ResponseEntity<Contest> getContestById(Long contestId) {
        Contest result = this.contestService.getContestById(contestId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(this.contestService.getContestById(contestId), HttpStatus.OK);
    }

    public ResponseEntity<List<Contest>> getAllContest(ContestType type) {
        return new ResponseEntity<>(this.contestService.getAllContest(type), HttpStatus.OK);
    }

    public ResponseEntity<Contest> addContest(ContestRequest contest, HttpServletRequest request) {
        if(contest.getCaption() == null || contest.getType() == null || contest.getCompetitionId() == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else return new ResponseEntity<>(this.contestService.addContest(contest,request), HttpStatus.OK);
    }

    public ResponseEntity<Contest> deleteContest(Long contestId) {
        return new ResponseEntity<>(this.contestService.deleteContest(contestId),HttpStatus.OK);
    }

    public ResponseEntity<List<Player>> getPlayersByContestId( Long contestId) {
        List<Player> result = this.contestService.getPlayersInContest(contestId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<List<StandingPlayer>> getStandingContest(Long contestId){
        return new ResponseEntity<>(this.contestService.getStandingContest(contestId),HttpStatus.OK);
    }

    public ResponseEntity<List<Message>> getMessagesByContestId(Long contestId) {
        List<Message> result = this.contestService.getMessagesInContest(contestId);
        if (result == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
