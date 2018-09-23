package server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.model.football.*;
import server.repository.football.CompetitionRepository;
import server.repository.football.MatchRepository;
import server.repository.football.StandingRepository;
import server.repository.football.TeamRepository;
import server.service.CompetitionService;

import java.util.*;

@Service
@Slf4j
public class CompetitionServiceImpl implements CompetitionService{

    private final CompetitionRepository competitionRepository;
    private final MatchRepository matchRepository;
    private final StandingRepository standingRepository;
    private final TeamRepository teamRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository,
                                  MatchRepository matchRepository,
                                  TeamRepository teamRepository,
                                  StandingRepository standingRepository){
        this.competitionRepository = competitionRepository;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.standingRepository = standingRepository;
    }

    public List<Competition> getAllCompetition(){
        return this.competitionRepository.findAll();
    }
    public Competition getCompetitionById(Long competitionId){
        return this.competitionRepository.findOne(competitionId);
    }
    public List<Team> getAllTeamOfCompetition(Long competitionId){
        return this.teamRepository.findAllByCompetitions(this.competitionRepository.findOne(competitionId));
    }
    public List<Match> getAllMatchesOfCompetition(Long competitionId, Integer matchDay, StandingStage stage, StandingGroup group){
        List<Match> matches = new ArrayList<>();
        if(matchDay == null && stage == null && group == null){
            matches = this.matchRepository.findAllByCompetitionId(competitionId);
        }else {
            if(matchDay != null){
                if(stage == null && group == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndMatchday(competitionId,matchDay);
                }else if (stage == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndMatchdayAndGroup(competitionId,matchDay,group);
                }else if( group == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndMatchdayAndStage(competitionId,matchDay,stage);
                }else {
                    matches =  this.matchRepository.findAllByCompetitionIdAndMatchdayAndStageAndGroup(competitionId,matchDay,stage,group);
                }
            }else {
                if(stage == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndGroup(competitionId,group);
                }else if( group == null){
                    matches =  this.matchRepository.findAllByCompetitionIdAndStage(competitionId,stage);
                }else {
                    matches =  this.matchRepository.findAllByCompetitionIdAndStageAndGroup(competitionId,stage,group);
                }
            }
        }
        Collections.sort(matches);
        return matches;
    }
    public List<Standing> getAllStandingsOfCompetition(Long competitionId, StandingType type, StandingGroup group){
        List<Standing> standings = new ArrayList<>();
        if(type == null && group == null){
            standings = this.standingRepository.findAllByCompetitionId(competitionId);
        }else {
            if(type == null){
                standings =  this.standingRepository.findAllByCompetitionIdAndGroup(competitionId,group);
            }else if( group == null){
                standings = this.standingRepository.findAllByCompetitionIdAndType(competitionId,type);
            }else {
                standings = this.standingRepository.findAllByCompetitionIdAndTypeAndGroup(competitionId,type,group);
            }
        }
        Collections.sort(standings);
        return standings;
    }


}
