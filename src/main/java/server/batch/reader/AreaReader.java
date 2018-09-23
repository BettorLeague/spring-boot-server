package server.batch.reader;

import org.springframework.batch.item.ItemReader;
import org.springframework.web.client.RestTemplate;
import server.dto.football.AreaDto;
import server.dto.football.AreasDto;

import java.util.List;

public class AreaReader implements ItemReader<AreaDto> {

    private String apiUrl;
    private RestTemplate restTemplate;

    private int nexAreaIndex;
    private List<AreaDto> areaData;

    public AreaReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.nexAreaIndex = 0;
    }

    @Override
    public AreaDto read() throws Exception {
        if (areaDataIsNotInitialized()) {
            areaData = fetchTeamDataFromAPI();
        }

        AreaDto nextArea = null;

        if (nexAreaIndex < areaData.size()) {
            nextArea = areaData.get(nexAreaIndex);
            nexAreaIndex++;
        }

        return nextArea;
    }

    private boolean areaDataIsNotInitialized() {
        return this.areaData == null;
    }

    private List<AreaDto> fetchTeamDataFromAPI() {
        return restTemplate.getForObject(apiUrl, AreasDto.class).getAreas();
    }
}