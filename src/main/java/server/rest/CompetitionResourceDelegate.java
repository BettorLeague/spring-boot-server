package server.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.model.football.*;
import server.service.CompetitionService;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class CompetitionResourceDelegate {

    private final CompetitionService competitionService;

    public CompetitionResourceDelegate(CompetitionService competitionService){
        this.competitionService = competitionService;
    }

    public ResponseEntity<List<Competition>> getAllCompetition() {
        return new ResponseEntity<>(this.competitionService.getAllCompetition(), HttpStatus.OK);
    }

    public ResponseEntity<Competition> getCompetitionById(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getCompetitionById(competitionId),HttpStatus.OK);
    }

    public ResponseEntity<Set<Team>> getAllTeamOfCompetition(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getAllTeamOfCompetition(competitionId),HttpStatus.OK);
    }

    public ResponseEntity<Set<Match>> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group){
        return new ResponseEntity<>(this.competitionService.getAllMatchesOfCompetition(competitionId,matchDay,stage,group),HttpStatus.OK);
    }

    public ResponseEntity<Set<Standing>> getAllStandingsOfCompetition(Long competitionId,StandingType type, StandingGroup group){
        return new ResponseEntity<>(this.competitionService.getAllStandingsOfCompetition(competitionId,type,group),HttpStatus.OK);
    }
}
