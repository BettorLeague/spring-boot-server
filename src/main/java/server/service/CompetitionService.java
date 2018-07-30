package server.service;

import server.model.football.*;

import java.util.List;
import java.util.Set;

public interface CompetitionService {

    List<Competition> getAllCompetition();

    Competition getCompetitionById(Long competitionId);

    Set<Team> getAllTeamOfCompetition(Long competitionId);

    Set<Match> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group);

    Set<Standing> getAllStandingsOfCompetition(Long competitionId,StandingType type, StandingGroup group);


}
