package server.repository.football;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Season;


public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    boolean existsByName(String name);
    boolean existsByCode(String code);

    Competition findByName(String name);
    Competition findByCode(String code);
    Competition findByCurrentSeason(Season season);
}
