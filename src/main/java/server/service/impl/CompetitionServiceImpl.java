package server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.model.football.*;
import server.repository.football.CompetitionRepository;
import server.repository.football.MatchRepository;
import server.repository.football.StandingRepository;
import server.service.CompetitionService;

import java.util.*;

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
        List<Competition> result = this.competitionRepository.findAll();
        result.forEach(el -> {
            el.setAvailableGroup(new TreeSet<>(el.getAvailableGroup()));
        });
        return result;
    }


    public Competition getCompetitionById(Long competitionId){
        Competition result = this.competitionRepository.findOne(competitionId);
        result.setAvailableGroup(new TreeSet<>(result.getAvailableGroup()));
        return result;
    }

    public Set<Team> getAllTeamOfCompetition(Long competitionId){
        return this.competitionRepository.findOne(competitionId).getTeams();
    }

    public Set<Match> getAllMatchesOfCompetition(Long competitionId,
                                                 Integer matchDay,
                                                 StandingStage stage,
                                                 StandingGroup group){
        if(matchDay == null && stage == null && group == null){
            return sortMatches(this.matchRepository.findAllByCompetitionId(competitionId));
        }else {
            if(matchDay != null){
                if(stage == null && group == null){
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndMatchday(competitionId,matchDay));
                }else if (stage == null){
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndMatchdayAndGroup(competitionId,matchDay,group));
                }else if( group == null){
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndMatchdayAndStage(competitionId,matchDay,stage));
                }else {
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndMatchdayAndStageAndGroup(competitionId,matchDay,stage,group));
                }
            }else {
                if(stage == null){
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndGroup(competitionId,group));
                }else if( group == null){
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndStage(competitionId,stage));
                }else {
                    return sortMatches(this.matchRepository.findAllByCompetitionIdAndStageAndGroup(competitionId,stage,group));
                }
            }
        }
    }
    public Set<Standing> getAllStandingsOfCompetition(Long competitionId,
                                                      StandingType type,
                                                      StandingGroup group){
        Set<Standing> result = null;
        if(type == null && group == null){
            result = this.standingRepository.findAllByCompetitionId(competitionId);
            result.forEach(el -> {
                el.setTable(sortStanding(el.getTable()));
            });
            return result;
        }else {
            if(type == null){
                result = this.standingRepository.findAllByCompetitionIdAndGroup(competitionId,group);
                result.forEach(el -> {
                    el.setTable(sortStanding(el.getTable()));
                });
                return result;
            }else if( group == null){
                result = this.standingRepository.findAllByCompetitionIdAndType(competitionId,type);
                result.forEach(el -> {
                    el.setTable(sortStanding(el.getTable()));
                });
                return result;
            }else {
                result = this.standingRepository.findAllByCompetitionIdAndTypeAndGroup(competitionId,type,group);
                result.forEach(el -> {
                    el.setTable(sortStanding(el.getTable()));
                });
                return result;
            }

        }
    }

    private Set<StandingTable> sortStanding(Set<StandingTable> standingTables){
        return new TreeSet<StandingTable>(standingTables);
    }

    private Set<Match> sortMatches(Set<Match> matches){
        return new TreeSet<Match>(matches);
    }

}
