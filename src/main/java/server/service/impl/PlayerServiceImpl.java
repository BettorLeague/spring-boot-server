package server.service.impl;

import org.springframework.stereotype.Service;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.football.Standing;
import server.model.football.StandingTable;
import server.repository.bettor.PlayerRepository;
import server.service.PlayerService;

import java.util.Set;
import java.util.TreeSet;

@Service
public class PlayerServiceImpl implements PlayerService{
     private final PlayerRepository playerRepository;

     public PlayerServiceImpl(PlayerRepository playerRepository){
         this.playerRepository = playerRepository;
     }


    public Set<Player> getAllByContestId(Long contestId){
        return sortPlayer(this.playerRepository.getAllByContestId(contestId));
    }
    public Player addPlayer(Player player){
         return this.playerRepository.save(player);
    }

    public Set<Pronostic> getPronostics(Long playerId){
         return this.playerRepository.findOne(playerId).getPronostics();
    }

    public Player getPlayerByUserIdAndContestId(Long userId,Long contestId){
         return this.playerRepository.findByUserIdAndContestId(userId,contestId);
    }

    public void deletePlayer(Long playerId){
         this.playerRepository.delete(playerId);
    }

    private Set<Player> sortPlayer(Set<Player> players){
        return new TreeSet<Player>(players);
    }

}
