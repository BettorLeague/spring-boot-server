package server.repository.bettor;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.bettor.Contest;
import server.model.bettor.ContestType;

import java.util.List;
import java.util.Set;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    Set<Contest> findAllByType(ContestType type);

}
