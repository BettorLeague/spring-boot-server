package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"season","score","competition"})
@Table(name = "FIXTURE")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @JsonIgnore
    private Long fbdId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Season season;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    private Date utcDate;

    @Column
    private String status;

    @Column
    private Integer matchday;

    @Enumerated(EnumType.STRING)
    private StandingStage stage;

    @Column(name = "GROUPE")
    @Enumerated(EnumType.STRING)
    private StandingGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team awayTeam;

    @OneToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private Score score;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Competition competition;
}
