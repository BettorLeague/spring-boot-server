package server.model.football;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StandingTable implements Comparable<StandingTable>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @OneToOne
    private Team team;

    @Column()
    private int position;

    @Column()
    private int playedGames;

    @Column()
    private int won;

    @Column()
    private int draw;

    @Column()
    private int lost;

    @Column()
    private int points;

    @Column()
    private int goalsFor;

    @Column()
    private int goalsAgainst;

    @Column()
    private int goalDifference;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "standing_id", nullable = false)
    @JsonIgnore
    private Standing standing;


    @Override
    public int compareTo(StandingTable other) {
        return Integer.compare(this.position, other.position);
    }
}
