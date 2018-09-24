package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@Entity
@Table(name = "TEAM")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Area area;

    @NotNull
    @Column(unique = true,name = "NAME")
    private String name;

    @Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "TLA")
    private String tla;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE")
    private String phone;

    @Column(name = "WEBSITE")
    private String website;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LOGO")
    private String logo;

    @Column(name = "FOUNDED")
    private Long founded;

    @Column(name = "CLUB_COLORS")
    private String clubColors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TEAM_COMPETITION",
            joinColumns = { @JoinColumn(name = "TEAM_ID") },
            inverseJoinColumns = { @JoinColumn(name = "COMPETITION_ID") })
    @JsonIgnore
    private List<Competition> competitions = new ArrayList<>();



}
