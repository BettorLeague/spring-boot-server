package server.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import server.dto.footballdata.*;
import server.model.football.*;
import server.repository.football.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class FootballDataBatch {

    private final static String BASE_API_V2_URL = "http://api.football-data.org/v2/";
    private final static String BASE_API_V1_URL = "http://api.football-data.org/v1/";

    private final CompetitionRepository competitionRepository;
    private final AreaRepository areaRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final StandingRepository standingRepository;
    private final MatchRepository matchRepository;
    private final TableRepository tableRepository;
    private final ScoreRepository scoreRepository;

    private RestTemplate restTemplate;

    public FootballDataBatch(CompetitionRepository competitionRepository,
                             AreaRepository areaRepository,
                             StandingRepository standingRepository,
                             SeasonRepository seasonRepository,
                             TableRepository tableRepository,
                             MatchRepository matchRepository,
                             ScoreRepository scoreRepository,
                             TeamRepository teamRepository){
        this.competitionRepository = competitionRepository;
        this.areaRepository = areaRepository;
        this.teamRepository = teamRepository;
        this.seasonRepository = seasonRepository;
        this.standingRepository = standingRepository;
        this.matchRepository = matchRepository;
        this.tableRepository = tableRepository;
        this.scoreRepository = scoreRepository;
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add(new RestTemplateInterceptor());
    }

    //@Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)// 24 heures
    public void feedingJob(){
        log.warn("Feeding job start");
        updateCompetitionByFootballDataId("2015");
    }

    private void updateCompetitionByFootballDataId(String idCompetitionFBD){
        CompetitionDto competitionDto = restTemplate.getForObject(BASE_API_V2_URL + "competitions/"+ idCompetitionFBD, CompetitionDto.class);
        Competition competition = this.competitionRepository.findByName(competitionDto.getName());

        // Verifier si la competition existe ou est obsolète
        if (competition != null){
            log.info("Competition {} already here, update", competitionDto.getName());
            updateCompetition(competitionDto,idCompetitionFBD);

        // Sinon creer la competition en base à partir du dto
        }else {
            log.info("Competition {} is new, create", competitionDto.getName());
            createCompetition(competitionDto,idCompetitionFBD);
        }

        log.info("Feeding job end");
    }



    private void createCompetition(CompetitionDto competitiondto, String idCompetitionFBD) {

        Area areadto = competitiondto.getArea();
        Set<Season> seasonsdto = competitiondto.getSeasons();
        Set<Team> teamsdto = this.getTeamsByFootballDataId(idCompetitionFBD);
        Set<Standing> standingsdto = this.getStandingsByFootballDataId(idCompetitionFBD);
        Set<Match> matchesdto = this.getMatchessByFootballDataId(idCompetitionFBD);

        Competition savedCompetition = new Competition();
        savedCompetition.setName(competitiondto.getName());
        savedCompetition.setCode(competitiondto.getCode());
        savedCompetition.setLastUpdated(competitiondto.getLastUpdated());
        savedCompetition.setLogo(getCompetitionlogo(idCompetitionFBD));

        if (! areaRepository.existsByName(areadto.getName())){
            areadto.setId(null);
            areadto = areaRepository.save(areadto);
        }

        savedCompetition.setArea(areadto);

        for(Season season: seasonsdto){
            season.setId(null);
            season.setCompetition(savedCompetition);
            savedCompetition.getSeasons().add(season);
        }

        try {
            savedCompetition.setCurrentSeason(getTheLastSeason(savedCompetition));
        }catch (ParseException e){ log.warn(e.getMessage()); }

        for(Team team: teamsdto){
            if (! teamRepository.existsByName(team.getName()) ){
                //team.setLogo(this.getLogoFromTeamId(team.getId()));
                team.setId(null);
                team.setCompetition(new HashSet<>());
                team.getCompetition().add(savedCompetition);
                team.setArea(isNull(team.getArea().getName()) ? savedCompetition.getArea() : savedCompetition.getArea());
                savedCompetition.getTeams().add(team);
            }
        }


        for(Standing standing: standingsdto){
            Set<StandingTable> tablesbuffer = standing.getTable();
            standing.setId(null);
            standing.setTable(new HashSet<>());
            standing.setCompetition(savedCompetition);

            for(StandingTable table: tablesbuffer){
                table.setTeam(getTeamFromName(savedCompetition.getTeams(),table.getTeam().getName()));
                table.setId(null);
                table.setStanding(standing);
                standing.getTable().add(table);
            }
            savedCompetition.getStandings().add(standing);
        }


        for (Match match: matchesdto){
            // Sauvegarde du match
            match.setId(null);
            match.getScore().setId(null);
            match.setSeason(savedCompetition.getCurrentSeason());
            match.setAwayTeam(getTeamFromName(savedCompetition.getTeams(),match.getAwayTeam().getName()));
            match.setHomeTeam(getTeamFromName(savedCompetition.getTeams(),match.getHomeTeam().getName()));
            match.setSeason(savedCompetition.getCurrentSeason());
            match.getScore().setMatch(match);
            match.setCompetition(savedCompetition);
            savedCompetition.getMatches().add(match);
        }

        competitionRepository.save(savedCompetition);

    }

    private void updateCompetition(CompetitionDto competitionDto, String idCompetitionFBD){
        /*
        Competition competitionToUpdate = competitionRepository.findByName(competitiondto.getName());
        List<Season> seasonsdto = competitiondto.getSeasons();
        List<Standing> standingsdto = this.getStandingsByFootballDataId(idCompetitionFBD);
        List<Match> matchesdto = this.getMatchessByFootballDataId(idCompetitionFBD);
        log.info("Competition à update : {}",competitionToUpdate.getName());

        //Mise à jour de la saison si différente
        if(competitiondto.getCurrentSeason().getCurrentMatchday() != null && competitiondto.getCurrentSeason().getCurrentMatchday() > competitionToUpdate.getCurrentSeason().getCurrentMatchday()){
            competitionToUpdate.getCurrentSeason().setCurrentMatchday(competitiondto.getCurrentSeason().getCurrentMatchday());
        }
        //Mise à jour de la liste des match


        // Sauvegarde de la competition
        competitionRepository.save(competitionToUpdate);*/

    }


    private Season getTheLastSeason(Competition competition) throws ParseException{
        Set<Season> seasons = competition.getSeasons();
        if (seasons.size() > 0 ){
            Season lastSeason = seasons.iterator().next();
            log.info("Saison date : {}",lastSeason.getEndDate().toString());
            for(Season season: seasons){
                if (season.getEndDate().after(lastSeason.getEndDate())){
                    lastSeason = season;
                    log.info("Saison date : {}",lastSeason.getEndDate().toString());
                }
            }
            return lastSeason;
        }
        return null;
    }

    private Set<Standing> getStandingsByFootballDataId(String idCompetitionFBD){
        StandingsDto standingsDto = restTemplate.getForObject(BASE_API_V2_URL + "competitions/"+ idCompetitionFBD + "/standings", StandingsDto.class);
        return standingsDto.getStandings();
    }

    private Set<Team> getTeamsByFootballDataId(String idCompetitionFBD){
        TeamsDto teamsDto = restTemplate.getForObject(BASE_API_V2_URL + "competitions/"+ idCompetitionFBD + "/teams", TeamsDto.class);
        return teamsDto.getTeams();
    }

    private Set<Match> getMatchessByFootballDataId(String idCompetitionFBD){
        MatchesDto matchesDto = restTemplate.getForObject(BASE_API_V2_URL + "competitions/"+ idCompetitionFBD + "/matches", MatchesDto.class);
        return matchesDto.getMatches();
    }

    private Date getDateFromDtoString(String date)throws ParseException{
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date result = inputFormat.parse(date);
        return result;
    }

    private String getCompetitionlogo(String id){
        switch (id){
            case "2015": return "https://upload.wikimedia.org/wikipedia/fr/9/9b/Logo_de_la_Ligue_1_%282008%29.svg";
            case "445": return "https://upload.wikimedia.org/wikipedia/fr/f/f2/Premier_League_Logo.svg";
            case "452": return "https://upload.wikimedia.org/wikipedia/en/d/df/Bundesliga_logo_%282017%29.svg";
            case "455": return "https://upload.wikimedia.org/wikipedia/commons/archive/9/92/20171221112945%21LaLiga_Santander.svg";
            case "456": return "https://upload.wikimedia.org/wikipedia/en/f/f7/LegaSerieAlogoTIM.png";
            case "467": return "https://upload.wikimedia.org/wikipedia/fr/f/f7/FIFA_World_Cup_2018_Logo.png";
            default: return null;
        }
    }

    private String getLogoFromTeamId(Long teamId){
        TeamsLogoDto teamsLogoDto = restTemplate.getForObject(BASE_API_V1_URL + "teams/"+ teamId , TeamsLogoDto.class);
        return teamsLogoDto.getCrestUrl();

    }


    private Team getTeamFromName(Set<Team> teams,String teamName){
        Team result = null;
        for (Team team: teams){
            if(team.getName().equals(teamName)){
                result = team;
            }
        }
        return result;
    }

}
