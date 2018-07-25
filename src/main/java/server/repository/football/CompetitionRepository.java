package server.repository.football;

import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Season;


public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Competition findByName(String name);
    Competition findByCode(String code);
    Competition findByCurrentSeason(Season season);
}
