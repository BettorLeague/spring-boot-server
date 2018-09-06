package server.service;

import server.dto.contest.ContestRequest;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Message;
import server.model.bettor.Player;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface ContestService {

    Set<Contest> getAllContest(ContestType type);
    Contest addContest(ContestRequest contest, HttpServletRequest request);
    Contest getContestById(Long contestId);
    void deleteContest(Long contestId);

    Set<Player>  getPlayersByContestId(Long contestId);
    Set<Message> getMessagesByContestId(Long contestId);

    boolean existContestUser(Long contestId,Long userId);
    Player addUserToContest(Long contestId, Long userId);
    void deleteUserFromContest(Long contestId,Long userId);
}
