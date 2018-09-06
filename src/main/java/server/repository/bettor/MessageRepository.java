package server.repository.bettor;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.bettor.Message;

import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Set<Message> findAllByContestIdAndPlayer_Id(Long contestId, Long playerId);
}
