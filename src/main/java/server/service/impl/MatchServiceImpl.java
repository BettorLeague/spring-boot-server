package server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.model.football.Competition;
import server.model.football.Match;
import server.model.football.Team;
import server.repository.football.CompetitionRepository;
import server.repository.football.MatchRepository;
import server.repository.football.TeamRepository;
import server.service.CompetitionService;
import server.service.MatchService;

import java.util.*;

@Service
@Slf4j
public class MatchServiceImpl implements MatchService{

    private final MatchRepository matchRepository;

    public MatchServiceImpl(MatchRepository matchRepository){
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatch(){
        return this.matchRepository.findAll();
    }

    public List<Match> getLast5match(Long competitionId,Long teamId){
        List<Match> result = new ArrayList<>();
        List<Match> matches = this.matchRepository.findAllByHomeTeamIdOrAwayTeamIdAndCompetitionIdAndStatus(teamId,teamId,competitionId,"FINISHED");
        for(int i = matches.size()-1; i >= 0 && result.size() < 5 ; i--){
            result.add(matches.get(i));
        }
        Collections.sort(result);
        return result;
    }

}
