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

    Contest addContest(ContestRequest contest, HttpServletRequest request);
    Contest deleteContest(Long contestId);
    Contest getContestById(Long contestId);

    List<Contest> getAllContest(Long userId,ContestType type);
    List<Contest> getAllContest(ContestType type);
    List<Player>  getPlayersInContest(Long contestId);
    List<Message> getMessagesInContest(Long contestId);
}
