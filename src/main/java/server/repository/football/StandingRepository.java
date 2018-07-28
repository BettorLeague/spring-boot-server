package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Standing;
import server.model.football.StandingType;
import server.model.football.Team;

import java.util.List;
import java.util.Set;


public interface StandingRepository extends JpaRepository<Standing, Long> {
    Standing findByCompetitionAndType(Competition competition, StandingType type);
    Set<Standing> findAllByCompetition(Competition competition);
}
