package server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.model.football.*;
import server.repository.football.CompetitionRepository;
import server.repository.football.MatchRepository;
import server.repository.football.StandingRepository;
import server.service.CompetitionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompetitionServiceImpl implements CompetitionService{

    private final CompetitionRepository competitionRepository;
    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository,
                                  MatchRepository matchRepository,
                                  StandingRepository standingRepository){
        this.competitionRepository = competitionRepository;
        this.matchRepository = matchRepository;
        this.standingRepository = standingRepository;
    }

    public List<Competition> getAllCompetition(){
        return this.competitionRepository.findAll();
    }


    public Competition getCompetitionById(Long competitionId){
        return this.competitionRepository.findOne(competitionId);
    }

    public Set<Team> getAllTeamOfCompetition(Long competitionId){
        return this.competitionRepository.findOne(competitionId).getTeams();
    }

    public Set<Match> getAllMatchesOfCompetition(Long competitionId,
                                                 Integer matchDay,
                                                 StandingStage stage,
                                                 StandingGroup group){

        if(matchDay == null && stage == null && group == null){
            return this.matchRepository.findAllByCompetitionId(competitionId);
        }else {
            if(matchDay != null){
                if(stage == null && group == null){
                    return this.matchRepository.findAllByCompetitionIdAndMatchday(competitionId,matchDay);
                }else if (stage == null){
                    return this.matchRepository.findAllByCompetitionIdAndMatchdayAndGroup(competitionId,matchDay,group);
                }else if( group == null){
                    return this.matchRepository.findAllByCompetitionIdAndMatchdayAndStage(competitionId,matchDay,stage);
                }else {
                    return this.matchRepository.findAllByCompetitionIdAndMatchdayAndStageAndGroup(competitionId,matchDay,stage,group);
                }
            }else {
                if(stage == null){
                    return this.matchRepository.findAllByCompetitionIdAndGroup(competitionId,group);
                }else if( group == null){
                    return this.matchRepository.findAllByCompetitionIdAndStage(competitionId,stage);
                }else {
                    return this.matchRepository.findAllByCompetitionIdAndStageAndGroup(competitionId,stage,group);
                }
            }
        }
    }
    public Set<Standing> getAllStandingsOfCompetition(Long competitionId,
                                                      StandingType type,
                                                      StandingGroup group){
        if(type == null && group == null){
            return this.standingRepository.findAllByCompetitionId(competitionId);
        }else {
            if(type == null){
                return this.standingRepository.findAllByCompetitionIdAndGroup(competitionId,group);
            }else if( group == null){
                return this.standingRepository.findAllByCompetitionIdAndType(competitionId,type);
            }else {
                return this.standingRepository.findAllByCompetitionIdAndTypeAndGroup(competitionId,type,group);
            }

        }
    }

}
