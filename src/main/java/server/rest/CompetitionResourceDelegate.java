package server.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.Standing;
import server.model.football.Team;
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

    public ResponseEntity<Set<Team>> getAllTeamOfCompetition(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getAllTeamOfCompetition(competitionId),HttpStatus.OK);
    }

    public ResponseEntity<Set<Match>> getAllMatchesOfCompetition(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getAllMatchesOfCompetition(competitionId),HttpStatus.OK);
    }

    public ResponseEntity<Set<Standing>> getAllStandingsOfCompetition(Long competitionId){
        return new ResponseEntity<>(this.competitionService.getAllStandingsOfCompetition(competitionId),HttpStatus.OK);
    }
}
