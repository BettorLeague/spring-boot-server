package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.*;

import java.util.List;
import java.util.Set;


public interface StandingRepository extends JpaRepository<Standing, Long> {

    Set<Standing> findAllByCompetitionId(Long competitionId);

    Set<Standing> findAllByCompetitionIdAndType(Long competitionId,StandingType type);
    Set<Standing> findAllByCompetitionIdAndTypeAndStage(Long competitionId,StandingType type,StandingStage stage);
    Set<Standing> findAllByCompetitionIdAndTypeAndGroup(Long competitionId,StandingType type,StandingGroup group);
    Set<Standing> findAllByCompetitionIdAndTypeAndStageAndGroup(Long competitionId, StandingType type, StandingStage stage, StandingGroup group);


    Set<Standing> findAllByCompetitionIdAndStage(Long competitionId,StandingStage stage);
    Set<Standing> findAllByCompetitionIdAndGroup(Long competitionId,StandingGroup group);
    Set<Standing> findAllByCompetitionIdAndStageAndGroup(Long competitionId, StandingStage stage, StandingGroup group);

}
