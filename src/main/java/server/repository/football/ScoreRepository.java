package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.Score;
import server.model.football.Team;

import java.util.List;


public interface ScoreRepository extends JpaRepository<Score, Long> {
}
