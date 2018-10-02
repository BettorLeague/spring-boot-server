package server.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.football.Match;
import server.repository.bettor.PlayerRepository;
import server.repository.bettor.PronosticRepository;

import java.util.List;

@Component
@Slf4j
@Transactional
public class PronosticBatch {

    private final PronosticRepository pronosticRepository;
    private final PlayerRepository playerRepository;

    public PronosticBatch(PronosticRepository pronosticRepository,
                          PlayerRepository playerRepository){
        this.pronosticRepository = pronosticRepository;
        this.playerRepository = playerRepository;
    }

    @Scheduled(fixedRate = 1000 * 60 )// 1 minute
    public void updatePlayerScore(){
        log.warn("Update pronostic");
        List<Pronostic> pronostics = pronosticRepository.findAllByAssigned(false);

        for(Pronostic pronostic: pronostics){
            Match match = pronostic.getMatch();
            if(match.getStatus().equals("FINISHED")){
                Player player = pronostic.getPlayer();
                // check exact pronostic
                if(pronostic.getGoalsHomeTeam() == pronostic.getMatch().getScore().getFullTime().getHomeTeam() && pronostic.getGoalsAwayTeam() == pronostic.getMatch().getScore().getFullTime().getAwayTeam()){
                    player.setExactPronostic(player.getExactPronostic()+1);
                    player.setPoints(player.getPoints()+5);
                }else if(pronostic.getGoalsHomeTeam() > pronostic.getGoalsAwayTeam() && match.getScore().getWinner().equals("HOME_TEAM")){
                    player.setGoodPronostic(player.getGoodPronostic()+1);
                    player.setPoints(player.getPoints()+3);
                }else if (pronostic.getGoalsAwayTeam() > pronostic.getGoalsHomeTeam() && match.getScore().getWinner().equals("AWAY_TEAM")){
                    player.setGoodPronostic(player.getGoodPronostic()+1);
                    player.setPoints(player.getPoints()+3);
                }else if(pronostic.getGoalsHomeTeam() == pronostic.getGoalsAwayTeam() && match.getScore().getWinner().equals("DRAW")){
                    player.setGoodPronostic(player.getGoodPronostic()+1);
                    player.setPoints(player.getPoints()+3);
                }

                player.setTotalPronostic(player.getTotalPronostic()+1);
                playerRepository.save(player);
            }
        }
    }
}
