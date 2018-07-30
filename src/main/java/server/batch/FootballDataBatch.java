package server.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
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

    //@Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris")// à minuit
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)// 24 heures
    public void feedingJob(){

        log.warn("Feeding job start 2000");
        updateCompetitionByFootballDataId("2000");
        /*
        this.pause(1);
        log.warn("Feeding job start 2001");
        updateCompetitionByFootballDataId("2001");
        this.pause(1);
        log.warn("Feeding job start 2002");
        updateCompetitionByFootballDataId("2002");
        this.pause(1);
        log.warn("Feeding job start 2014");
        updateCompetitionByFootballDataId("2014");
        this.pause(1);
        log.warn("Feeding job start 2019");
        updateCompetitionByFootballDataId("2019");
        this.pause(1);
        log.warn("Feeding job start 2015");
        updateCompetitionByFootballDataId("2015");
        this.pause(1);
        log.warn("Feeding job start 2021");
        updateCompetitionByFootballDataId("2021");
        this.pause(1);*/
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
        Set<MatchDto> matchesdto = this.getMatchessByFootballDataId(idCompetitionFBD);

        Competition savedCompetition = new Competition();
        savedCompetition.setName(competitiondto.getName());
        savedCompetition.setCode(competitiondto.getCode());
        savedCompetition.setLastUpdated(competitiondto.getLastUpdated());
        savedCompetition.setLogo(getCompetitionlogo(idCompetitionFBD));

        if(areaRepository.existsByName(areadto.getName())){
            savedCompetition.setArea(areaRepository.findByName(areadto.getName()));
        }else{
            if(areadto.getName() != null){
                areadto.setId(null);
                areadto = areaRepository.save(areadto);
                savedCompetition.setArea(areadto);
            }
        }


        for(Season season: seasonsdto){
            season.setId(null);
            season.setCompetition(savedCompetition);
            if (season.getStartDate() != null) savedCompetition.getSeasons().add(season);
        }

        try {
            if (savedCompetition.getSeasons().size() > 0 ) savedCompetition.setCurrentSeason(getTheLastSeason(savedCompetition));
        }catch (ParseException e){ log.warn(e.getMessage()); }

        for(Team team: teamsdto){
            if (! teamRepository.existsByName(team.getName()) ){
                team.setLogo(this.getLogoFromTeamId(team.getId()));
                Area savedArea = team.getArea();
                if(areaRepository.existsByName(savedArea.getName())){
                    team.setArea(areaRepository.findByName(savedArea.getName()));
                }else{
                    if(savedArea.getName() != null){
                        savedArea.setId(null);
                        savedArea = areaRepository.save(savedArea);
                        team.setArea(savedArea);
                    }else {
                        team.setArea(null);
                    }
                }

                team.setId(null);
                team.setCompetition(new HashSet<>());
                team.getCompetition().add(savedCompetition);
                savedCompetition.getTeams().add(team);
            }
        }


        for(Standing standing: standingsdto){
            Set<StandingTable> tablesbuffer = standing.getTable();
            standing.setId(null);
            standing.setTable(new HashSet<>());
            standing.setCompetition(savedCompetition);
            if(standing.getGroup() != null && this.parseGroupe(standing.getGroup().toString()) != null ) standing.setGroup(parseGroupe(standing.getGroup().toString()));
            standing.setStage(parseStage(standing.getStage().toString()));
            for(StandingTable table: tablesbuffer){
                table.setTeam(getTeamFromName(savedCompetition.getTeams(),table.getTeam().getName()));
                table.setId(null);
                table.setStanding(standing);
                standing.getTable().add(table);
            }
            savedCompetition.getStandings().add(standing);
            savedCompetition.getAvailableGroup().add(standing.getGroup());
        }


        for (MatchDto matchesDto: matchesdto){
            matchesDto.getScore().setId(null);
            Match match = new Match();
            match.setStage(parseStage(matchesDto.getStage()));
            match.setScore(matchesDto.getScore());
            match.setSeason(savedCompetition.getCurrentSeason());
            match.setUtcDate(matchesDto.getUtcDate());
            match.setStatus(matchesDto.getStatus());
            match.setMatchday(matchesDto.getMatchday());
            if(matchesDto.getGroup() != null && this.parseGroupe(matchesDto.getGroup()) != null ) match.setGroup(parseGroupe(matchesDto.getGroup()));
            match.setHomeTeam(getTeamFromName(savedCompetition.getTeams(),matchesDto.getHomeTeam().getName()));
            match.setAwayTeam(getTeamFromName(savedCompetition.getTeams(),matchesDto.getAwayTeam().getName()));
            match.getScore().setMatch(match);
            match.setLastUpdated(matchesDto.getLastUpdated());
            match.setCompetition(savedCompetition);

            savedCompetition.getAvailableStage().add(match.getStage());
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

    private Set<MatchDto> getMatchessByFootballDataId(String idCompetitionFBD){
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
            case "2001": return "https://upload.wikimedia.org/wikipedia/fr/b/bf/UEFA_Champions_League_logo_2.svg";
            case "2021": return "https://upload.wikimedia.org/wikipedia/fr/f/f2/Premier_League_Logo.svg";
            case "2002": return "https://upload.wikimedia.org/wikipedia/en/d/df/Bundesliga_logo_%282017%29.svg";
            case "2014": return "https://upload.wikimedia.org/wikipedia/commons/archive/9/92/20171221112945%21LaLiga_Santander.svg";
            case "2019": return "https://upload.wikimedia.org/wikipedia/en/f/f7/LegaSerieAlogoTIM.png";
            case "2000": return "https://upload.wikimedia.org/wikipedia/en/6/67/2018_FIFA_World_Cup.svg";
            default: return null;
        }
    }

    private String getLogoFromTeamId(Long teamId){
        String logo = null;
        try{
            TeamsLogoDto teamsLogoDto = restTemplate.getForObject(BASE_API_V1_URL + "teams/"+ teamId , TeamsLogoDto.class);
            logo = teamsLogoDto.getCrestUrl();
        }catch (RestClientException e){
            log.warn(e.getMessage());
        }
        return logo;

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

    private void pause(int minutes){
        try{
            Thread.sleep(1000 * 60 * minutes);
        }catch (InterruptedException e){ log.warn(e.getMessage());}
    }


    private StandingStage parseStage(String stage){
        switch (stage){
            case "REGULAR_SEASON" :
                return StandingStage.REGULAR_SEASON;
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

}