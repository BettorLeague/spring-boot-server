package server.service;

import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.Standing;
import server.model.football.Team;

import java.util.List;
import java.util.Set;

public interface CompetitionService {
    List<Competition> getAllCompetition();
    Set<Team> getAllTeamOfCompetition(Long competitionId);
    Set<Match> getAllMatchesOfCompetition(Long competitionId);
    Set<Standing> getAllStandingsOfCompetition(Long competitionId);
}
