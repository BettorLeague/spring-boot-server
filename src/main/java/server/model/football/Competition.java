package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "COMPETITION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Competition {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CODE",unique = true)
    private String code;

    @Column(name = "LOGO")
    private String logo;

    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="AREA_ID")
    private Area area;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="CURRENT_SEASON_ID")
    private Season currentSeason;

    @OneToMany(mappedBy = "competition",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Season> seasons = new ArrayList<>();

    @ManyToMany(mappedBy = "competitions",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy="competition",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Standing> standings = new ArrayList<>();

    @OneToMany(mappedBy="competition",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Match> matches = new ArrayList<>();

    @ElementCollection(targetClass=StandingStage.class)
    @Enumerated(EnumType.STRING)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "COMPETITION_AVAILABLE_STAGE", joinColumns = @JoinColumn(name = "COMPETITION_ID"))
    @Column(name = "AVAILABLE_STAGE")
    private List<StandingStage> availableStage = new ArrayList<>();

    @ElementCollection(targetClass=StandingGroup.class)
    @Enumerated(EnumType.STRING)
    @LazyCollection(LazyCollectionOption.FALSE)
    @CollectionTable(name = "COMPETITION_AVAILABLE_GROUP", joinColumns = @JoinColumn(name = "COMPETITION_ID"))
    @Column(name = "AVAILABLE_GROUP")
    private List<StandingGroup> availableGroup = new ArrayList<>();

}
