package server.dto.football;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.football.*;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompetitionDto {
    private Long id;
    private String name;
    private String code;
    private String plan;
    private Date lastUpdated;
    private AreaDto area;
    private Season currentSeason;
    private List<Season> seasons;
}
