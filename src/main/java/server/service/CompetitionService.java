package server.service;

import server.model.football.*;

import java.util.List;
import java.util.Set;

public interface CompetitionService {

    Competition getCompetitionById(Long competitionId);

    List<Competition> getAllCompetition();

    List<Team> getAllTeamOfCompetition(Long competitionId);

    List<Match> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group);

    List<Standing> getAllStandingsOfCompetition(Long competitionId,StandingType type, StandingGroup group);


}
