package server.service;

import server.model.football.Match;

import java.util.List;

public interface MatchService {
    List<Match> getAllMatch();
    List<Match> getLast5match(Long competitionId,Long teamId);
}
