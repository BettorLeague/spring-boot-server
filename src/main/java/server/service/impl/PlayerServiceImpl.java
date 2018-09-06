package server.service.impl;

import org.springframework.stereotype.Service;
import server.dto.contest.MessageRequest;
import server.model.bettor.Message;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.football.Standing;
import server.model.football.StandingTable;
import server.repository.bettor.ContestRepository;
import server.repository.bettor.MessageRepository;
import server.repository.bettor.PlayerRepository;
import server.service.PlayerService;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService{
     private final PlayerRepository playerRepository;
     private final MessageRepository messageRepository;
     private final ContestRepository contestRepository;

     public PlayerServiceImpl(PlayerRepository playerRepository,
                              ContestRepository contestRepository,
                              MessageRepository messageRepository){
         this.playerRepository = playerRepository;
         this.contestRepository = contestRepository;
         this.messageRepository = messageRepository;
     }


    public List<Player> getAllByContestId(Long contestId){
        return this.playerRepository.findAllByContestId(contestId);
    }
    public Player addPlayer(Player player){
         return this.playerRepository.save(player);
    }

    public Set<Pronostic> getPronostics(Long playerId){
         return new HashSet<>(this.playerRepository.findOne(playerId).getPronostics());
    }

    public Player getPlayerByUserIdAndContestId(Long userId,Long contestId){
         return this.playerRepository.findByUserIdAndContestId(userId,contestId);
    }

    public void deletePlayer(Long playerId){
         this.playerRepository.delete(playerId);
    }

    public Message postMessage(MessageRequest message, Long contestId, Long playerId){
         Message newMessage = new Message();
         newMessage.setContent(message.getContent());
         newMessage.setPlayer(playerRepository.findOne(playerId));
         newMessage.setDate(new Date());
         newMessage.setContest(contestRepository.findOne(contestId));
         return this.messageRepository.save(newMessage);
    }

}
