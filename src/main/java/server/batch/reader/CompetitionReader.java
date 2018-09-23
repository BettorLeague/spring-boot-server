package server.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;
import server.dto.football.CompetitionDto;

public class CompetitionReader implements ItemReader<CompetitionDto> {

    private String competitionId;
    private RestTemplate restTemplate;

    private CompetitionDto competitionData;

    public CompetitionReader(String competitionId, RestTemplate restTemplate) {
        this.competitionId = competitionId;
        this.restTemplate = restTemplate;
    }

    @Override
    public CompetitionDto read() throws Exception {
        if (competitionDataIsNotInitialized()) {
            competitionData = fetchTeamDataFromAPI();
        }

        return competitionData;
    }

    private boolean competitionDataIsNotInitialized() {
        return this.competitionData == null;
    }


    private CompetitionDto fetchTeamDataFromAPI() {
        return restTemplate.getForObject("http://api.football-data.org/v2/competitions/"+competitionId, CompetitionDto.class);
    }

}