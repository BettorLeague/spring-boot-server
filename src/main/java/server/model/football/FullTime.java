package server.model.football;


import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class FullTime {

    @Column(name="HOME_TEAM")
    private Long homeTeam;

    @Column(name="AWAY_TEAM")
    private Long awayTeam;
}
