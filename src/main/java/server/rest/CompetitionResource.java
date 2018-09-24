package server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.model.football.*;

import java.util.List;

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

    @RequestMapping(path = "/api/competition/{competitionId}", method = RequestMethod.GET)
    public ResponseEntity<Competition> getCompetitionById(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getCompetitionById(competitionId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/teams", method = RequestMethod.GET)
    public ResponseEntity<List<Team>> getAllTeamOfCompetition(@PathVariable("competitionId") Long competitionId) {
        return this.competitionResourceDelegate.getAllTeamOfCompetition(competitionId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/teams/{teamId}", method = RequestMethod.GET)
    public ResponseEntity<List<Match>> getlast5TeamMatch(@PathVariable("competitionId") Long competitionId,@PathVariable("teamId") Long teamId) {
        return this.competitionResourceDelegate.getlast5TeamMatch(competitionId,teamId);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/matches", method = RequestMethod.GET)
    public ResponseEntity<List<Match>> getAllMatchesOfCompetition(@PathVariable("competitionId") Long competitionId,
                                                                 @RequestParam(value = "matchday", required=false) Integer matchday,
                                                                 @RequestParam(value = "stage", required=false) StandingStage stage,
                                                                 @RequestParam(value = "group", required=false) StandingGroup group) {
        return this.competitionResourceDelegate.getAllMatchesOfCompetition(competitionId,matchday,stage,group);
    }

    @RequestMapping(path = "/api/competition/{competitionId}/standings", method = RequestMethod.GET)
    public ResponseEntity<List<Standing>> getAllStandingsOfCompetition(@PathVariable("competitionId") Long competitionId,
                                                                      @RequestParam(value = "type", required=false)  StandingType type,
                                                                      @RequestParam(value = "group", required=false) StandingGroup group) {
        return this.competitionResourceDelegate.getAllStandingsOfCompetition(competitionId,type,group);
    }



}
