package server.repository.bettor;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.bettor.Player;
import server.model.bettor.Pronostic;
import server.model.football.Match;

import java.util.List;

public interface PronosticRepository extends JpaRepository<Pronostic, Long> {
    List<Pronostic> findAllByMatchId(Long matchId);
    List<Pronostic> findAllByAssigned(Boolean assigned);
    List<Pronostic> findAllByPlayerId(Long playerId);
    List<Pronostic> findAllByPlayerIdAndAssigned(Long playerId, Boolean assigned);

    Pronostic findByMatchIdAndPlayerId(Long matchId,Long playerId);
    boolean existsByMatchIdAndPlayerId(Long matchId,Long playerId);
}
