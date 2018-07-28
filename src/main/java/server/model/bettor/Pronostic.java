package server.model.bettor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.football.Match;
import server.model.football.Score;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "PRONOSTIC")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pronostic {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Match match;

    @Column(name = "GOALS_AWAY_TEAM")
    private int goalsAwayTeam;

    @Column(name = "GOALS_HOME_TEAM")
    private int goalsHomeTeam;


}
