package server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.model.football.Competition;
import server.model.football.Match;
import server.repository.football.CompetitionRepository;
import server.repository.football.MatchRepository;
import server.service.CompetitionService;
import server.service.MatchService;

import java.util.List;

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

}
