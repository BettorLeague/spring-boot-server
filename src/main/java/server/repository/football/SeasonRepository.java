package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Season;
import server.model.football.Team;

import java.util.List;
import java.util.Set;


public interface SeasonRepository extends JpaRepository<Season, Long> {
    Set<Season> findByCompetitionId(Long competitionId);
}
