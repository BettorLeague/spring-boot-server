package server.model.football;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"match"})
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ScoreResult winner;

    @Enumerated(EnumType.STRING)
    private ScoreDuration duration;

    @OneToOne
    @JsonIgnore
    private Match match;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "full_time_home_team")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "full_time_away_team"))
    })
    private FullTime fullTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "half_time_home_team")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "half_time_away_team"))
    })
    private HalfTime halfTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "extra_time_home_team")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "extra_time_away_team"))
    })
    private ExtraTime extraTime;

    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "homeTeam", column = @Column(name = "penalties_home_team")),
            @AttributeOverride(name = "awayTeam", column = @Column(name = "penalties_away_team"))
    })
    private Penalties penalties;

}
