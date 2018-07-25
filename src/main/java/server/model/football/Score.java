package server.model.football;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScoreResult winner;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScoreDuration duration;

    @Embedded
    private FullTime fullTime;

    @OneToOne(fetch = FetchType.LAZY)
    private Match match;
/*
    @Embedded
    private HalfTime halfTime;

    @Embedded
    private ExtraTime extraTime;

    @Embedded
    private Penalties penalties;*/
}
