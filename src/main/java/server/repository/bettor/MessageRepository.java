package server.repository.bettor;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.bettor.Message;

import java.util.List;
import java.util.Set;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByContestId(Long contestId);
    List<Message> findAllByContestIdAndPlayer_Id(Long contestId, Long playerId);
}
