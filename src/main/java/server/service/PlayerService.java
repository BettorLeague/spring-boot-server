package server.service;

import server.model.bettor.Player;
import server.model.bettor.Pronostic;

import java.util.List;
import java.util.Set;

public interface PlayerService {
    Set<Player> getAllByContestId(Long contestId);
    Player addPlayer(Player player);

    Set<Pronostic> getPronostics(Long playerId);
}
