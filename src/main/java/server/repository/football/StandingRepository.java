package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.*;

import java.util.List;
import java.util.List;


public interface StandingRepository extends JpaRepository<Standing, Long> {

    boolean existsByCompetitionId(Long competitionId);

    List<Standing> findAllByCompetitionId(Long competitionId);

    List<Standing> findAllByCompetitionIdAndType(Long competitionId,StandingType type);
    List<Standing> findAllByCompetitionIdAndTypeAndStage(Long competitionId,StandingType type,StandingStage stage);
    List<Standing> findAllByCompetitionIdAndTypeAndGroup(Long competitionId,StandingType type,StandingGroup group);
    List<Standing> findAllByCompetitionIdAndTypeAndStageAndGroup(Long competitionId, StandingType type, StandingStage stage, StandingGroup group);


    List<Standing> findAllByCompetitionIdAndStage(Long competitionId,StandingStage stage);
    List<Standing> findAllByCompetitionIdAndGroup(Long competitionId,StandingGroup group);
    List<Standing> findAllByCompetitionIdAndStageAndGroup(Long competitionId, StandingStage stage, StandingGroup group);

    void deleteAllByCompetitionId(Long competitionId);
}
