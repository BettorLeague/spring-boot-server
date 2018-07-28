package server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.Standing;
import server.model.football.Team;
import server.service.impl.CompetitionServiceImpl;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

@RestController
public class CompetitionResource {

    private final CompetitionResourceDelegate competitionResourceDelegate;

    public CompetitionResource(CompetitionResourceDelegate competitionResourceDelegate){
        this.competitionResourceDelegate = competitionResourceDelegate;
    }

    @RequestMapping(path = "/api/competition", method = RequestMethod.GET)
    public ResponseEntity<List<Competition>> getAllCompetition() {
        return this.competitionResourceDelegate.getAllCompetition();
    }

    @RequestMapping(path = "/api/competition/{competitionId}/teams", method = RequestMethod.GET)
    public ResponseEntity<Set<Team>> getAllTeamOfCompetition(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getAllTeamOfCompetition(competitionId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/matches", method = RequestMethod.GET)
    public ResponseEntity<Set<Match>> getAllMatchesOfCompetition(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getAllMatchesOfCompetition(competitionId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/standings", method = RequestMethod.GET)
    public ResponseEntity<Set<Standing>> getAllStandingsOfCompetition(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getAllStandingsOfCompetition(competitionId);
    }

}
