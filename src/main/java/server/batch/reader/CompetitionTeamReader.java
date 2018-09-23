package server.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;
import server.dto.football.TeamDto;
import server.dto.football.TeamsDto;

import java.util.List;

public class CompetitionTeamReader implements ItemReader<TeamDto> {

    private String competitionId;
    private RestTemplate restTemplate;

    private int nexTeamIndex;
    private List<TeamDto> teamDtoList;

    public CompetitionTeamReader(String competitionId, RestTemplate restTemplate) {
        this.competitionId = competitionId;
        this.restTemplate = restTemplate;
    }

    @Override
    public TeamDto read() throws Exception {
        if (competitionDataIsNotInitialized()) {
            teamDtoList = fetchTeamDataFromAPI();
        }

        TeamDto nextTeam = null;

        if (nexTeamIndex < teamDtoList.size()) {
            nextTeam = teamDtoList.get(nexTeamIndex);
            nexTeamIndex++;
        }

        return nextTeam;
    }

    private boolean competitionDataIsNotInitialized() {
        return this.teamDtoList == null;
    }


    private List<TeamDto> fetchTeamDataFromAPI() {
        return restTemplate.getForObject("http://api.football-data.org/v2/competitions/"+competitionId+"/teams", TeamsDto.class).getTeams();
    }

}