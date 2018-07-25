package server.model.football;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Area area;

    @Column(unique = true)
    private String name;

    @Column
    private String code;

    @OneToOne(fetch = FetchType.LAZY)
    private Season currentSeason;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Season> seasons;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Team> teams;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Standing> standings;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Match> matches;
}
