package server.model.bettor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.football.Match;

import javax.persistence.*;

@Entity
@Data
@Table(name = "PRONOSTIC")
@NoArgsConstructor
@AllArgsConstructor
public class Pronostic {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="MATCH_ID")
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    private Player player;

    @Column(name = "GOALS_AWAY_TEAM")
    private Long goalsAwayTeam;

    @Column(name = "GOALS_HOME_TEAM")
    private Long goalsHomeTeam;

    @Column(name = "ASSIGNED")
    @JsonIgnore
    private boolean assigned = false;

}
