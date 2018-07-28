package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Standing;
import server.model.football.StandingTable;

import java.util.Set;

public interface TableRepository extends JpaRepository<StandingTable, Long> {
    Set<StandingTable> findAllByStanding(Standing standing);
}
