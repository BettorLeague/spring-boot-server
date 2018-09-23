package server.batch;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import server.config.RestTemplateInterceptor;
import server.dto.football.*;
import server.model.football.*;
import server.repository.football.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static server.model.football.StandingStage.REGULAR_SEASON;

@Component
@Slf4j
@Transactional
public class FootballBatch {

    private final List<String> competitions = Arrays.asList("2015","2001"/*,"2000","2002","2014","2019","2021"*/);
    private RestTemplate restTemplate;
    private ModelMapper modelMapper;


    private final CompetitionRepository competitionRepository;
    private final AreaRepository areaRepository;
    private final SeasonRepository seasonRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final ScoreRepository scoreRepository;
    private final StandingRepository standingRepository;
    private final TableRepository tableRepository;

    public FootballBatch(CompetitionRepository competitionRepository,
                         AreaRepository areaRepository,
                         TeamRepository teamRepository,
                         MatchRepository matchRepository,
                         ScoreRepository scoreRepository,
                         TableRepository tableRepository,
                         StandingRepository standingRepository,
                         SeasonRepository seasonRepository){

        this.competitionRepository = competitionRepository;
        this.areaRepository = areaRepository;
        this.standingRepository = standingRepository;
        this.seasonRepository = seasonRepository;
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.scoreRepository = scoreRepository;
        this.tableRepository = tableRepository;

        this.modelMapper = new ModelMapper();
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add(new RestTemplateInterceptor());

    }



    //@Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")// à minuit
    @Scheduled(fixedRate = 1000 * 60 * 60 )// 24 heures
    public void fetchFootballData(){

        log.warn("Début du batch de récupération");

        for(String competition:competitions){
            //update la compétition avec l'id football data org
            log.warn("Début de récupération de la compétition :"+competition);
            updateCompetition(competition);
            log.warn("Fin de la récupération de la compétition "+competition);

            //Pause d'une minute entre chaque compétition afin d'éviter une 429
            pause(1);
        }

        log.warn("Batch de récupération terminé");

    }

    private void updateCompetition(String competitionId){

        CompetitionDto competitionDto = readCompetition(competitionId);
        Competition competition = processCompetition(competitionDto);
        competition = writeCompetition(competition,competitionId);

        List<TeamDto> teamsDto = readCompetitionTeams(competitionId);
        List<Team> teams = processTeamCompetition(teamsDto);
        teams = writeTeamCompetition(teams,competition,competition.getCurrentSeason());

        List<MatchDto> matchesDto = readCompetitionMatches(competitionId);
        List<Match> matches = processMatchCompetition(matchesDto);
        matches = writeMatchCompetition(matches,competition);

        List<StandingDto> standingsDto = readCompetitionStandings(competitionId);
        List<Standing> standings = processStandingCompetition(standingsDto);
        standings = writeStandingCompetition(standings,competition);

        addCompetitionInformation(competition,teams,matches,standings);

    }

    private CompetitionDto readCompetition(String competitionId){
        return restTemplate.getForObject("http://api.football-data.org/v2/competitions/"+competitionId, CompetitionDto.class);
    }
    private List<TeamDto> readCompetitionTeams(String competitionId){
        return restTemplate.getForObject("http://api.football-data.org/v2/competitions/"+competitionId+"/teams", TeamsDto.class).getTeams();
    }
    private List<MatchDto> readCompetitionMatches(String competitionId){
        return restTemplate.getForObject("http://api.football-data.org/v2/competitions/"+competitionId+"/matches", MatchesDto.class).getMatches();
    }
    private List<StandingDto> readCompetitionStandings(String competitionId){
        return restTemplate.getForObject("http://api.football-data.org/v2/competitions/"+competitionId+"/standings", StandingsDto.class).getStandings();
    }

    private Competition processCompetition(CompetitionDto competitionDto){
        Competition competition = modelMapper.map(competitionDto,Competition.class);
        competition.setId(null);
        competition.getArea().setId(null);
        competition.getCurrentSeason().setId(null);
        for (Season season: competition.getSeasons()){
            season.setFbdId(season.getId());
            season.setId(null);
        }
        return competition;
    }
    private List<Team> processTeamCompetition(List<TeamDto> teamsDto){
        List<Team> result = new ArrayList<>();
        for(TeamDto teamDto : teamsDto){
            Team team = modelMapper.map(teamDto,Team.class);
            team.setLogo(teamDto.getCrestUrl());
            team.setId(null);
            if(team.getArea() != null){
                team.getArea().setId(null);
            }
            result.add(team);
        }
        return result;
    }
    private List<Match> processMatchCompetition(List<MatchDto> matchesDto){
        List<Match> matches = new ArrayList<>();
        for(MatchDto matchDto : matchesDto){
            Match match = modelMapper.map(matchDto,Match.class);
            match.setFbdId(match.getId());
            match.setId(null);
            match.setSeason(null);
            match.getScore().setId(null);
            match.setStage(parseStage(matchDto.getStage()));
            match.setGroup(parseGroupe(matchDto.getGroup()));
            matches.add(match);
        }
        return matches;
    }
    private List<Standing> processStandingCompetition(List<StandingDto> standingsDto){
        List<Standing> standings = new ArrayList<>();
        for(StandingDto standingDto : standingsDto){
            Standing standing = modelMapper.map(standingDto,Standing.class);
            standing.setStage(parseStage(standingDto.getStage()));
            standing.setGroup(parseGroupe(standingDto.getGroup()));
            for(StandingTable standingTable : standing.getTable()){
                standingTable.getTeam().setId(null);
            }
            standings.add(standing);
        }
        return standings;
    }

    private Competition writeCompetition(Competition competition,String competitionId){

        if(!competitionRepository.existsByName(competition.getName())){

            log.warn("Nouvelle Competition : "+competition.getName());

            if(!areaRepository.existsByName(competition.getArea().getName())){
                competition.setArea(areaRepository.save(competition.getArea()));
            }else{
                competition.setArea(areaRepository.findByName(competition.getArea().getName()));
            }

            List<Season> seasons = competition.getSeasons();

            competition.setSeasons(null);
            competition.setCurrentSeason(null);
            competition = competitionRepository.save(competition);

            for(Season season: seasons){
                season.setCompetition(competition);
                season = seasonRepository.save(season);
            }

            competition.setLogo(this.getCompetitionlogo(competitionId));
            competition.setSeasons(seasons);
            competition.setCurrentSeason(seasons.get(0));
            competition = competitionRepository.save(competition);

        }else{

            log.warn("Mise à jour de la competition : "+competition.getName());

            Competition current = competitionRepository.findByName(competition.getName());
            Season currentSeason = current.getCurrentSeason();
            currentSeason.setCurrentMatchday(competition.getCurrentSeason().getCurrentMatchday());
            seasonRepository.save(currentSeason);

            current.setLastUpdated(competition.getLastUpdated());
            competition = competitionRepository.save(current);
        }
        return competition;
    }
    private List<Team> writeTeamCompetition(List<Team> teams,Competition currentCompetition,Season season){
        for(Team team : teams){
            if(!teamRepository.existsByName(team.getName())){

                if(team.getArea() != null && !areaRepository.existsByName(team.getArea().getName())){
                    team.setArea(areaRepository.save(team.getArea()));
                }else if (team.getArea() != null && areaRepository.existsByName(team.getArea().getName())){
                    team.setArea(areaRepository.findByName(team.getArea().getName()));
                }
                team.getCompetitions().add(currentCompetition);
                currentCompetition.getTeams().add(team);
                team = teamRepository.save(team);


            }else{
                String logo = team.getLogo();
                team = teamRepository.findByName(team.getName());
                team.setLogo(logo);
                final String name = currentCompetition.getName();
                if(team.getCompetitions().stream().noneMatch(competition -> competition.getName().equals(name))){
                    team.getCompetitions().add(currentCompetition);
                    currentCompetition.getTeams().add(team);
                }
                team = teamRepository.save(team);
            }
        }
        return teams;
    }
    private List<Match> writeMatchCompetition(List<Match> matches,Competition currentCompetition){
        for(Match match: matches){
            if(!matchRepository.existsByFbdId(match.getFbdId())){
                Score score = match.getScore();
                match.setSeason(currentCompetition.getCurrentSeason());
                match.setHomeTeam(teamRepository.findByName(match.getHomeTeam().getName()));
                match.setAwayTeam(teamRepository.findByName(match.getAwayTeam().getName()));
                match.setScore(null);
                match.setCompetition(currentCompetition);
                match = matchRepository.save(match);

                score.setMatch(match);
                score = scoreRepository.save(score);

                match.setScore(score);
                match = matchRepository.save(match);

            }else {
                Match current = matchRepository.findByFbdId(match.getFbdId());
                Long scoreId = current.getScore().getId();
                current.setStatus(match.getStatus());
                current.setScore(match.getScore());
                current.getScore().setId(scoreId);
                current = matchRepository.save(current);
                match = current;
            }
        }
        return matches;
    }
    private List<Standing> writeStandingCompetition(List<Standing> standings,Competition currentCompetition){
        if(!standingRepository.existsByCompetitionId(currentCompetition.getId())){
            for(Standing standing : standings){

                List<StandingTable> standingTables = standing.getTable();

                standing.setCompetition(currentCompetition);
                standing.setTable(new ArrayList<>());
                standing = standingRepository.save(standing);

                for(StandingTable standingTable : standingTables){
                    standingTable.setId(null);
                    standingTable.setStanding(standing);
                    standingTable.setTeam(teamRepository.findByName(standingTable.getTeam().getName()));
                    standingTable = tableRepository.save(standingTable);
                }

                standing.setTable(standingTables);
                standing = standingRepository.save(standing);

            }
        }else{
            standingRepository.deleteAllByCompetitionId(currentCompetition.getId());
            return writeStandingCompetition(standings,currentCompetition);
        }
        return standings;
    }

    private void addCompetitionInformation(Competition competition,List<Team> teams,List<Match> matches,List<Standing> standings){
        for(Match match: matches){
            final StandingStage bufferStage = match.getStage();
            if(bufferStage != null && competition.getAvailableStage().stream().noneMatch(stage -> stage.equals(bufferStage))){
                competition.getAvailableStage().add(match.getStage());
            }
        }
        for(Standing standing: standings){
            final StandingGroup bufferGroup = standing.getGroup();
            if (bufferGroup != null && competition.getAvailableGroup().stream().noneMatch(group -> group.equals(bufferGroup))) {
                competition.getAvailableGroup().add(standing.getGroup());
            }
        }

        if (competition.getAvailableStage().contains(REGULAR_SEASON)){
            Season season = seasonRepository.findOne(competition.getCurrentSeason().getId());
            season.setAvailableMatchPerDay(getMatchPerDay(teams));
            season.setAvailableMatchDay(getMaxMatchDay(teams));
            season = seasonRepository.save(season);
        }
        competition = competitionRepository.save(competition);

    }

    private void pause(int minutes){
        try{
            Thread.sleep(1000 * 60 * minutes);
        }catch (InterruptedException e) {
            log.warn(e.getMessage());
        }
    }

    private StandingStage parseStage(String stage){
        if(stage == null ) return null;
        switch (stage){
            case "REGULAR_SEASON" :
                return REGULAR_SEASON;
            case "GROUP_STAGE" :
                return StandingStage.GROUP_STAGE;
            case "ROUND_OF_16" :
                return  StandingStage.ROUND_OF_16;
            case "QUARTER_FINALS" :
                return StandingStage.QUARTER_FINALS;
            case "SEMI_FINALS" :
                return StandingStage.SEMI_FINALS;
            case "3RD_PLACE" :
                return StandingStage.SMALL_FINAL;
            case "FINAL":
                return StandingStage.FINAL;
            case "PRELIMINARY_FINAL":
                return StandingStage.PRELIMINARY_FINAL;
            case "PRELIMINARY_SEMI_FINALS":
                return StandingStage.PRELIMINARY_SEMI_FINALS;
            case "1ST_QUALIFYING_ROUND":
                return StandingStage.QUALIFYING_ROUND_1ST;
            case "2ND_QUALIFYING_ROUND":
                return StandingStage.QUALIFYING_ROUND_2ND;
            case "3RD_QUALIFYING_ROUND":
                return StandingStage.QUALIFYING_ROUND_3RD;
            default:
                return null;


        }
    }

    private StandingGroup parseGroupe(String group){
        if(group == null ) return null;
        switch (group){
            case "Group A" :
                return StandingGroup.GROUP_A;
            case "Group B" :
                return StandingGroup.GROUP_B;
            case "Group C" :
                return StandingGroup.GROUP_C;
            case "Group D" :
                return StandingGroup.GROUP_D;
            case "Group E" :
                return StandingGroup.GROUP_E;
            case "Group F" :
                return StandingGroup.GROUP_F;
            case "Group G" :
                return StandingGroup.GROUP_G;
            case "Group H" :
                return StandingGroup.GROUP_H;
            case "Group I" :
                return StandingGroup.GROUP_I;
            case "Group J" :
                return StandingGroup.GROUP_J;
            case "Group K" :
                return StandingGroup.GROUP_K;
            case "Group L" :
                return StandingGroup.GROUP_L;
            case "GROUP_A" :
                return StandingGroup.GROUP_A;
            case "GROUP_B" :
                return StandingGroup.GROUP_B;
            case "GROUP_C" :
                return StandingGroup.GROUP_C;
            case "GROUP_D" :
                return StandingGroup.GROUP_D;
            case "GROUP_E" :
                return StandingGroup.GROUP_E;
            case "GROUP_F" :
                return StandingGroup.GROUP_F;
            case "GROUP_G" :
                return StandingGroup.GROUP_G;
            case "GROUP_H" :
                return StandingGroup.GROUP_H;
            case "GROUP_I" :
                return StandingGroup.GROUP_I;
            case "GROUP_J" :
                return StandingGroup.GROUP_J;
            case "GROUP_K" :
                return StandingGroup.GROUP_K;
            case "GROUP_L" :
                return StandingGroup.GROUP_L;
            default:
                return null;


        }
    }

    private String getCompetitionlogo(String id){
        switch (id){
            case "2015": return "https://upload.wikimedia.org/wikipedia/fr/9/9b/Logo_de_la_Ligue_1_%282008%29.svg";
            case "2001": return "https://upload.wikimedia.org/wikipedia/fr/b/bf/UEFA_Champions_League_logo_2.svg";
            case "2021": return "https://upload.wikimedia.org/wikipedia/fr/f/f2/Premier_League_Logo.svg";
            case "2002": return "https://upload.wikimedia.org/wikipedia/en/d/df/Bundesliga_logo_%282017%29.svg";
            case "2014": return "https://upload.wikimedia.org/wikipedia/fr/2/23/Logo_La_Liga.png";
            case "2019": return "https://upload.wikimedia.org/wikipedia/en/f/f7/LegaSerieAlogoTIM.png";
            case "2000": return "https://upload.wikimedia.org/wikipedia/en/6/67/2018_FIFA_World_Cup.svg";
            default: return null;
        }
    }

    private Long getMaxMatchDay(List<Team> teams){
        Number max = (teams.size() - 1) * 2;
        return max.longValue();
    }

    private Long getMatchPerDay(List<Team> teams){
        Number number = teams.size() / 2;
        return number.longValue();
    }

}
