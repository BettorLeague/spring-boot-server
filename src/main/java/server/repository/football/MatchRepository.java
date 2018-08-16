package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.StandingGroup;
import server.model.football.StandingStage;

import java.util.Set;

public interface MatchRepository extends JpaRepository<Match, Long> {
    boolean existsByFbdId(Long fbdId);
    Match findByFbdId(Long fbdId);

    Set<Match> findAllByCompetitionId(Long  competitionId);
    Set<Match> findAllByCompetitionIdAndStage(Long competitionId, StandingStage stage);
    Set<Match> findAllByCompetitionIdAndGroup(Long competitionId, StandingGroup group);
    Set<Match> findAllByCompetitionIdAndStageAndGroup(Long competitionId, StandingStage stage, StandingGroup group);


    Set<Match> findAllByCompetitionIdAndMatchday(Long competitionId, Integer matchDay);
    Set<Match> findAllByCompetitionIdAndMatchdayAndStage(Long competitionId, Integer matchDay, StandingStage stage);
    Set<Match> findAllByCompetitionIdAndMatchdayAndGroup(Long competitionId, Integer matchDay, StandingGroup group);
    Set<Match> findAllByCompetitionIdAndMatchdayAndStageAndGroup(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group);

}
