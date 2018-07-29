package server.dto.footballdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.football.Score;
import server.model.football.Season;
import server.model.football.Team;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDto {
    private Long id;
    private Season season;
    private Date utcDate;
    private String status;
    private Integer matchday;
    private String stage;
    private String group;
    private Team homeTeam;
    private Team awayTeam;
    private Score score;
    private Date lastUpdated;
}
