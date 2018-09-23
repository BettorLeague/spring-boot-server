package server.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import server.model.football.*;
import server.service.CompetitionService;
import server.service.MatchService;

import java.util.List;
import java.util.List;

@Component
@Slf4j
public class CompetitionResourceDelegate {

    private final CompetitionService competitionService;
    private final MatchService matchService;

    public CompetitionResourceDelegate(CompetitionService competitionService,
                                       MatchService matchService){
        this.competitionService = competitionService;
        this.matchService = matchService;
    }

    public ResponseEntity<List<Competition>> getAllCompetition() {
        return new ResponseEntity<>(this.competitionService.getAllCompetition(), HttpStatus.OK);
    }

    public ResponseEntity<Competition> getCompetitionById(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getCompetitionById(competitionId),HttpStatus.OK);
    }

    public ResponseEntity<List<Team>> getAllTeamOfCompetition(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getAllTeamOfCompetition(competitionId),HttpStatus.OK);
    }

    public ResponseEntity<List<Match>> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group){
        return new ResponseEntity<>(this.competitionService.getAllMatchesOfCompetition(competitionId,matchDay,stage,group),HttpStatus.OK);
    }

    public ResponseEntity<List<Standing>> getAllStandingsOfCompetition(Long competitionId,StandingType type, StandingGroup group){
        return new ResponseEntity<>(this.competitionService.getAllStandingsOfCompetition(competitionId,type,group),HttpStatus.OK);
    }

    public ResponseEntity<List<Match>> getlast5TeamMatch(Long competitionId,Long teamId) {
        return new ResponseEntity<>(this.matchService.getLast5match(competitionId,teamId),HttpStatus.OK);

    }
}
