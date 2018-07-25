package server.model.football;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Data
@Entity
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Season season;

    @Temporal(TemporalType.TIMESTAMP)
    private Date utcDate;

    @Column
    private String status;

    @Column
    private Integer matchday;

    @Column
    private String stage;

    @Column
    private String groupe;

    @OneToOne(fetch = FetchType.LAZY)
    private Team homeTeam;

    @OneToOne(fetch = FetchType.LAZY)
    private Team awayTeam;

    @OneToOne(fetch = FetchType.LAZY)
    private Score score;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Referee> referees;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToOne(fetch = FetchType.LAZY)
    private Competition competition;
}
