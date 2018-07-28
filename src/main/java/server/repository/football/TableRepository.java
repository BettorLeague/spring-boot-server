package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Standing;
import server.model.football.Table;

import java.util.List;
import java.util.Set;

public interface TableRepository extends JpaRepository<Table, Long> {
    Set<Table> findAllByStanding(Standing standing);
}
