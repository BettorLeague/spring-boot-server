package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Match;

import java.util.Set;

public interface MatchRepository extends JpaRepository<Match, Long> {
    Set<Match> findAllByCompetition(Competition competition);
}
