package server.model.football;


import lombok.Data;
import javax.persistence.*;

@Data
@Entity
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
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

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Standing standing;
}
