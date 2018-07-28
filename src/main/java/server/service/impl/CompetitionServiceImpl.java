package server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.Standing;
import server.model.football.Team;
import server.repository.football.CompetitionRepository;
import server.service.CompetitionService;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CompetitionServiceImpl implements CompetitionService{

    private final CompetitionRepository competitionRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository){
        this.competitionRepository = competitionRepository;
    }

    public List<Competition> getAllCompetition(){
        return this.competitionRepository.findAll();
    }

    public Set<Team> getAllTeamOfCompetition(Long competitionId){
        return this.competitionRepository.findOne(competitionId).getTeams();
    }

    public Set<Match> getAllMatchesOfCompetition(Long competitionId){
        return this.competitionRepository.findOne(competitionId).getMatches();
    }
    public Set<Standing> getAllStandingsOfCompetition(Long competitionId){
        return this.competitionRepository.findOne(competitionId).getStandings();
    }

}
