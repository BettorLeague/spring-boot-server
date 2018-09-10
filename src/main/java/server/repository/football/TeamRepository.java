package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Team;

import java.util.List;
import java.util.Set;


public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
    List<Team> findAllByCompetition(Competition competition);
    boolean existsByName(String name);
}
