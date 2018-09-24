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

    @OneToOne(fetch = FetchType.EAGER)
    private Season currentSeason;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "competition")
    @JsonIgnore
    private List<Season> seasons = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY,mappedBy = "competitions")
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
    private List<StandingStage> availableStage = new ArrayList<>();

    @ElementCollection(targetClass=StandingGroup.class)
    @Enumerated(EnumType.STRING)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<StandingGroup> availableGroup = new ArrayList<>();

}
