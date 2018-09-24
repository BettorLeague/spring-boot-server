package server.model.football;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "STANDING_TABLE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StandingTable implements Comparable<StandingTable>{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne
    private Team team;

    @Column(name = "POSITION")
    private int position;

    @Column(name = "PLAYED_GAMES")
    private int playedGames;

    @Column(name = "WON")
    private int won;

    @Column(name = "DRAW")
    private int draw;

    @Column(name = "LOST")
    private int lost;

    @Column(name = "POINTS")
    private int points;

    @Column(name = "GOALS_FOR")
    private int goalsFor;

    @Column(name = "GOALS_AGAINST")
    private int goalsAgainst;

    @Column(name = "GOAL_DIFFERENCE")
    private int goalDifference;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "STANDING_ID", nullable = false)
    @JsonIgnore
    private Standing standing;


    @Override
    public int compareTo(StandingTable other) {
        return Integer.compare(this.position, other.position);
    }
}
