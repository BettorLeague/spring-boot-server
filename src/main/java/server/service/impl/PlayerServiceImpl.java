package server.service.impl;

import org.springframework.stereotype.Service;
import server.dto.contest.MessageRequest;
import server.model.bettor.Contest;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.football.Standing;
import server.model.football.StandingTable;
import server.repository.bettor.ContestRepository;
import server.repository.bettor.MessageRepository;
import server.repository.bettor.PlayerRepository;
import server.repository.bettor.PronosticRepository;
import server.repository.user.UserRepository;
import server.service.PlayerService;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService{
     private final PlayerRepository playerRepository;
     private final MessageRepository messageRepository;
     private final ContestRepository contestRepository;
     private final UserRepository userRepository;
     private final PronosticRepository pronosticRepository;
     public PlayerServiceImpl(PlayerRepository playerRepository,
                              ContestRepository contestRepository,
                              UserRepository userRepository,
                              PronosticRepository pronosticRepository,
                              MessageRepository messageRepository){
         this.playerRepository = playerRepository;
         this.userRepository = userRepository;
         this.contestRepository = contestRepository;
         this.messageRepository = messageRepository;
         this.pronosticRepository = pronosticRepository;
     }


    public List<Pronostic> getPronostics(Long playerId){
         return this.pronosticRepository.findAllByPlayerId(playerId);
    }


    public Message postMessage(MessageRequest message, Long contestId, Long playerId){
         Message newMessage = new Message();
         newMessage.setContent(message.getContent());
         newMessage.setPlayer(playerRepository.findOne(playerId));
         newMessage.setDate(new Date());
         newMessage.setContest(contestRepository.findOne(contestId));
         return this.messageRepository.save(newMessage);
    }

    public List<Player> getAllByUser(Long userId){
         return this.playerRepository.findAllByUserId(userId);
    }


    public boolean isUserPlayingContest(Long userId,Long contestId){
         return this.playerRepository.existsByUserIdAndContestId(userId,contestId);
    }

    public Player subscribeToContest(Long userId,Long contestId){
         if(!isUserPlayingContest(userId,contestId)){
             Contest contest = contestRepository.findOne(contestId);
             contest.setNumberOfPlayers(contest.getNumberOfPlayers()+1);
             contestRepository.save(contest);
             Player player = new Player();
             player.setUser(userRepository.findOne(userId));
             player.setContest(contestRepository.findOne(contestId));
             player = playerRepository.save(player);
             return player;
         }else return null;
    }

    public Player unSubscribe(Long userId,Long contestId){
         if(isUserPlayingContest(userId,contestId)){
             Contest contest = contestRepository.findOne(contestId);
             contest.setNumberOfPlayers(contest.getNumberOfPlayers()-1);
             contestRepository.save(contest);
             Player player = playerRepository.findByUserIdAndContestId(userId,contestId);
             playerRepository.delete(player.getId());
             return player;
         }else return null;
    }


    public Pronostic savePronostic(Pronostic pronostic){
         if(this.pronosticRepository.existsByMatchIdAndPlayerId(pronostic.getMatch().getId(),pronostic.getPlayer().getId())){
             pronostic.setId(this.pronosticRepository.findByMatchIdAndPlayerId(pronostic.getMatch().getId(),pronostic.getPlayer().getId()).getId());
         }else pronostic.setId(null);
         return this.pronosticRepository.save(pronostic);
    }

}
