package server.service;

import server.model.football.Competition;
import server.model.football.Match;

import java.util.List;

public interface MatchService {
    List<Match> getAllMatch();
}
