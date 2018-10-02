package server.service.impl;

import org.springframework.stereotype.Service;
import server.dto.contest.ContestRequest;
import server.model.bettor.*;
import server.model.football.Competition;
import server.model.user.User;
import server.repository.bettor.ContestRepository;
import server.repository.bettor.MessageRepository;
import server.repository.bettor.PlayerRepository;
import server.repository.football.CompetitionRepository;
import server.service.CompetitionService;
import server.service.ContestService;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class ContestServiceImpl implements ContestService {

    private final ContestRepository contestRepository;
    private final PlayerRepository playerRepository;
    private final MessageRepository messagesRepository;
    private final UserService userService;
    private final CompetitionService competitionService;

    public ContestServiceImpl(ContestRepository contestRepository,
                              PlayerRepository playerRepository,
                              MessageRepository messageRepository,
                              UserService userService,
                              CompetitionService competitionService){
        this.contestRepository = contestRepository;
        this.playerRepository = playerRepository;
        this.messagesRepository = messageRepository;
        this.userService = userService;
        this.competitionService = competitionService;
    }

    public List<Contest> getAllContest(ContestType type){
        if(type == null) return contestRepository.findAll();
        else return this.contestRepository.findAllByType(type);
    }

    public Contest addContest(ContestRequest contest, HttpServletRequest request){
        Competition competition = competitionService.getCompetitionById(contest.getCompetitionId());
        User user = userService.getUser(request);
        if(competition != null){
            Contest result = new Contest();
            result.setCaption(contest.getCaption());
            result.setCompetition(competition);
            result.setType(contest.getType());
            result.setOwner(user);
            result = contestRepository.save(result);
            Player player = new Player();
            player.setContest(result);
            player.setUser(user);
            player = playerRepository.save(player);
            return contestRepository.save(result);
        }else return null;
    }

    public List<Player> getPlayersInContest(Long contestId){
        if (contestRepository.exists(contestId)) return playerRepository.findAllByContestId(contestId);
        else return new ArrayList<>();
    }


    public Contest getContestById(Long contestId){
        return contestRepository.findOne(contestId);
    }

    public Contest deleteContest(Long contestId){
        Contest contest = contestRepository.findOne(contestId);
        if(contest != null) { contestRepository.delete(contestId); }
        return contest;
    }

    public List<Message> getMessagesInContest(Long contestId){
        if (contestRepository.exists(contestId)) return new ArrayList<>(this.messagesRepository.findAllByContestId(contestId));
        else return new ArrayList<>();
    }

    public List<Contest> getAllContest(Long userId,ContestType type){

        List<Player> players = playerRepository.findAllByUserId(userId);
        List<Contest> contests = new ArrayList<>();

        if(type == null){
            players.forEach(player -> {
                contests.add(contestRepository.findOne(player.getContest().getId()));
            });
        }
        else {
            players.forEach(player -> {
                if(player.getContest().getType().equals(type)){
                    contests.add(contestRepository.findOne(player.getContest().getId()));
                }
            });
        }
        return contests;
    }


    public List<StandingPlayer> getStandingContest(Long contestId){
        List<Player> players = this.playerRepository.findAllByContestIdOrderByPointsDesc(contestId);
        List<StandingPlayer> result = new ArrayList<>();

        for(int i = 1 ; i < players.size()+1; i++){
            StandingPlayer standingPlayer = new StandingPlayer();
            standingPlayer.setPosition(i);
            standingPlayer.setPlayer(players.get(i -1));
            result.add(standingPlayer);
        }

        return result;
    }



}
