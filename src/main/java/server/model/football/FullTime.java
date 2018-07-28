package server.model.football;


import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class FullTime {
    private Long homeTeam;
    private Long awayTeam;
}
