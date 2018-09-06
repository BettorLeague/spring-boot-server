package server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import server.dto.contest.ContestRequest;
import server.model.bettor.Message;
import server.model.football.Competition;
import server.model.user.User;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;
import server.model.bettor.Player;
import server.repository.football.CompetitionRepository;
import server.repository.user.UserRepository;
import server.repository.bettor.ContestRepository;
import server.repository.bettor.PlayerRepository;
import server.security.JwtTokenUtil;
import server.service.ContestService;
import server.service.PlayerService;
import server.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Objects.nonNull;

@Service
public class ContestServiceImpl implements ContestService {
    @Value("${jwt.header}")
    private String tokenHeader;

    private final ContestRepository contestRepository;
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PlayerService playerService;
    private final CompetitionRepository competitionRepository;

    public ContestServiceImpl(ContestRepository contestRepository,
                              UserRepository userRepository,
                              JwtTokenUtil jwtTokenUtil,
                              PlayerService playerService,
                              PlayerRepository playerRepository,
                              CompetitionRepository competitionRepository){
        this.playerRepository = playerRepository;
        this.contestRepository = contestRepository;
        this.playerService = playerService;
        this.userRepository = userRepository;
        this.competitionRepository = competitionRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Set<Contest> getAllContest(ContestType type){
        if(type == null) return new HashSet<>(this.contestRepository.findAll());
        return this.contestRepository.findAllByType(type);

    }

    public Contest addContest(ContestRequest contest, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        Competition competition = competitionRepository.findOne(contest.getCompetitionId());
        User user = userRepository.findByEmailOrUsername(username,username);
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
            player = playerService.addPlayer(player);
            result.getPlayers().add(player);
            user.getPlayers().add(player);
            return contestRepository.save(result);
            //result.getPlayers().add()
        }else return  null;
    }

    public Set<Player> getPlayersByContestId(Long contestId){
        if (contestRepository.exists(contestId)) return new TreeSet<>(playerRepository.findAllByContestId(contestId));
        else return new HashSet<>();
    }


    public Contest getContestById(Long contestId){
        if(contestRepository.exists(contestId)) return contestRepository.findOne(contestId);
        else return null;
    }

    public void deleteContest(Long contestId){
        if(contestRepository.exists(contestId)) contestRepository.delete(contestId);
    }

    public Set<Message> getMessagesByContestId(Long contestId){
        if (contestRepository.exists(contestId)) return new HashSet<>(this.contestRepository.findOne(contestId).getMessages());
        else return new HashSet<>();
    }

    public boolean existContestUser(Long contestId,Long userId ){
        if (contestRepository.exists(contestId) && userRepository.exists(userId)){
            Contest contest = contestRepository.findOne(contestId);
            return contest.getPlayers().stream().anyMatch(obj -> obj.getUser().getId().equals(userId));
        }
        return false;
    }

    public Player addUserToContest(Long contestId,Long userId ){
        if (contestRepository.exists(contestId) && userRepository.exists(userId) && !existContestUser(contestId,userId)){
            Contest contest = contestRepository.findOne(contestId);
            User user = userRepository.findOne(userId);
            Player player = new Player();
            player.setUser(user);
            player.setContest(contest);
            contest.setNumberOfPlayers(contest.getNumberOfPlayers() + 1 );
            player = playerService.addPlayer(player);
            contest.getPlayers().add(player);
            contestRepository.save(contest);
            return player;
        }
        return null;
    }

    public void deleteUserFromContest(Long contestId, Long userId){
        if (contestRepository.exists(contestId) && userRepository.exists(userId) && existContestUser(contestId,userId)){
            Contest contest = contestRepository.findOne(contestId);
            User user = userRepository.findOne(userId);
            Player player = playerService.getPlayerByUserIdAndContestId(user.getId(),contest.getId());
            contest.getPlayers().remove(player);
            if (contest.getNumberOfPlayers() > 0) contest.setNumberOfPlayers(contest.getNumberOfPlayers() - 1 );
            contestRepository.save(contest);
            playerService.deletePlayer(player.getId());
        }
    }



    private Set<Player> sortPlayer(Set<Player> players){
        return new TreeSet<Player>(players);
    }


}
