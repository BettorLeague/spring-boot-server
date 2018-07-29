package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"seasons","currentSeason","standings","teams","matches"})
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column
    private String code;

    @Column
    private String logo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    private Area area;

    @OneToOne(fetch = FetchType.LAZY)
    private Season currentSeason;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "competition")
    private Set<Season> seasons = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "competition_teams",
            joinColumns = { @JoinColumn(name = "competition_id") },
            inverseJoinColumns = { @JoinColumn(name = "team_id") })
    @JsonIgnore
    private Set<Team> teams = new HashSet<>();

    @OneToMany(mappedBy="competition",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Standing> standings = new HashSet<>();

    @OneToMany(mappedBy="competition",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Match> matches = new HashSet<>();

    @ElementCollection(targetClass=StandingStage.class)
    @Enumerated(EnumType.STRING) // Possibly optional (I'm not sure) but defaults to ORDINAL.
    private Set<StandingStage> availableStage = new HashSet<>();

}
