package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"season","score","competition"})
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Season season;

    @Temporal(TemporalType.TIMESTAMP)
    private Date utcDate;

    @Column
    private String status;

    @Column
    private Integer matchday;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StandingStage stage;

    @Column(name = "GROUPE")
    private String group;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    private Team awayTeam;

    @OneToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private Score score;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToOne(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Competition competition;
}
