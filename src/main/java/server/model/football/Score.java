package server.model.football;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "SCORE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Score {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "WINNER")
    private ScoreResult winner;

    @Enumerated(EnumType.STRING)
    @Column(name = "DURATION")
    private ScoreDuration duration;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name="FIXTURE_ID")
    private Match match;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "FULL_TIME_HOME_TEAM")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "FULL_TIME_AWAY_TEAM"))
    })
    private FullTime fullTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "HALF_TIME_HOME_TEAM")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "HALF_TIME_AWAY_TEAM"))
    })
    private HalfTime halfTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "EXTRA_TIME_HOME_TEAM")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "EXTRA_TIME_AWAY_TEAM"))
    })
    private ExtraTime extraTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "PENALTIES_HOME_TEAM")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "PENALTIES_AWAY_TEAM"))
    })
    private Penalties penalties;

}
