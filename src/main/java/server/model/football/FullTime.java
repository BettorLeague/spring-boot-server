package server.model.football;



import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Embeddable
public class FullTime {
    private Long homeTeam;
    private Long awayTeam;
}
