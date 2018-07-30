package server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import server.batch.FootballDataBatch;
import server.model.football.*;

import java.util.List;
import java.util.Set;

@RestController
public class CompetitionResource {

    private final CompetitionResourceDelegate competitionResourceDelegate;
    private final FootballDataBatch footballDataBatch;

    public CompetitionResource(CompetitionResourceDelegate competitionResourceDelegate, FootballDataBatch footballDataBatch){
        this.footballDataBatch = footballDataBatch;
        this.competitionResourceDelegate = competitionResourceDelegate;
    }

    @RequestMapping(path = "/api/competition", method = RequestMethod.GET)
    public ResponseEntity<List<Competition>> getAllCompetition() {
        return this.competitionResourceDelegate.getAllCompetition();
    }

    @RequestMapping(path = "/api/competition/{competitionId}", method = RequestMethod.GET)
    public ResponseEntity<Set<Team>> getCompetitionById(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getAllTeamOfCompetition(competitionId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/teams", method = RequestMethod.GET)
    public ResponseEntity<Set<Team>> getAllTeamOfCompetition(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getAllTeamOfCompetition(competitionId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/matches", method = RequestMethod.GET)
    public ResponseEntity<Set<Match>> getAllMatchesOfCompetition(@PathVariable("competitionId") Long competitionId,
                                                                 @RequestParam(value = "matchday", required=false) Integer matchday,
                                                                 @RequestParam(value = "stage", required=false) StandingStage stage,
                                                                 @RequestParam(value = "group", required=false) StandingGroup group) {
        return this.competitionResourceDelegate.getAllMatchesOfCompetition(competitionId,matchday,stage,group);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/standings", method = RequestMethod.GET)
    public ResponseEntity<Set<Standing>> getAllStandingsOfCompetition(@PathVariable("competitionId") Long competitionId,
                                                                      @RequestParam(value = "type", required=false)  StandingType type,
                                                                      @RequestParam(value = "group", required=false) StandingGroup group) {
        return this.competitionResourceDelegate.getAllStandingsOfCompetition(competitionId,type,group);
    }

    @RequestMapping(path = "/api/competition", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateAllCompetition() {
        this.footballDataBatch.feedingJob();
    }

}
