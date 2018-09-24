package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Comparator;
import java.util.Date;


@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "FIXTURE")
public class Match implements Comparable<Match> {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FBD_ID")
    @JsonIgnore
    private Long fbdId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="SEASON_ID")
    private Season season;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    private Date utcDate;

    @Column(name="STATUS")
    private String status;

    @Column(name="MATCHDAY")
    private Integer matchday;

    @Enumerated(EnumType.STRING)
    @Column(name="STANDING_STAGE")
    private StandingStage stage;

    @Column(name = "GROUPE")
    @Enumerated(EnumType.STRING)
    private StandingGroup group;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="HOME_TEAM_ID")
    private Team homeTeam;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="AWAY_TEAM_ID")
    private Team awayTeam;

    @OneToOne(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name="SCORE_ID")
    private Score score;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_UPDATED")
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPETITION_ID", nullable = false)
    @JsonIgnore
    private Competition competition;

    @Override
    public int compareTo(Match other){
        if(this.matchday != null && other.getMatchday() != null){
            int matchday = this.matchday.compareTo(other.getMatchday());
            if (matchday != 0) {
                return matchday;
            }
        }
        int day = this.utcDate.compareTo(other.getUtcDate());
        if (day != 0) {
            return day;
        }
        int homeTeam = this.getHomeTeam().getName().compareTo(other.getHomeTeam().getName());
        if (homeTeam != 0) {
            return homeTeam;
        }
        return this.getAwayTeam().getName().compareTo(other.getAwayTeam().getName());
    }



}
