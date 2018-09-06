package server.repository.bettor;



import org.springframework.data.jpa.repository.JpaRepository;
import server.model.bettor.Player;

import java.util.List;
import java.util.Set;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllByContestId(Long contestId);


    Player findByUserId(Long userId);
    boolean existsByUserId(Long userId);

    Player findByUserIdAndContestId(Long userId,Long contestId);
    boolean existsByUserIdAndContestId(Long userId,Long contestId);
}
