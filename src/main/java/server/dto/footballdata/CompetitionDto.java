package server.dto.footballdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.football.*;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionDto {
    private Long id;
    private String name;
    private String code;
    private String logo;
    private Date lastUpdated;
    private Area area;
    private Season currentSeason;
    private Set<Season> seasons;
    private Set<Team> teams;
    private Set<Standing> standings;
    private Set<Match> matches;
}
