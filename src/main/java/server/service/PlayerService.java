package server.service;

import server.dto.contest.MessageRequest;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;

import java.util.List;
import java.util.Set;

public interface PlayerService {
    List<Player> getAllByUser(Long userId);
    List<Player> getAllByContestId(Long contestId);
    Player addPlayer(Player player);
    Player getPlayerByUserIdAndContestId(Long userId,Long contestId);
    List<Pronostic> getPronostics(Long playerId);

    void deletePlayer(Long playerId);

    Message postMessage(MessageRequest message, Long contestId, Long playerId);

    boolean isUserPlayingContest(Long userId,Long contestId);

    Player subscribeToContest(Long userId,Long contestId);
    Player unSubscribe(Long userId,Long contestId);

    Pronostic savePronostic(Pronostic pronostic);
}
