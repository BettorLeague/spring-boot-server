package server.service;

import server.dto.contest.ContestRequest;
import server.model.bettor.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface ContestService {

    Contest addContest(ContestRequest contest, HttpServletRequest request);
    Contest deleteContest(Long contestId);
    Contest getContestById(Long contestId);

    List<Contest> getAllContest(Long userId,ContestType type);
    List<Contest> getAllContest(ContestType type);
    List<Player>  getPlayersInContest(Long contestId);
    List<StandingPlayer> getStandingContest(Long contestId);
    List<Message> getMessagesInContest(Long contestId);
}
