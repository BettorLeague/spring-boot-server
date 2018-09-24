package server.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import server.model.bettor.Contest;
import server.model.bettor.Player;
import server.model.football.Team;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "APP_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"contests","favoriteTeam","sex","players","contests"})
public class User  {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USERNAME", length = 50, unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 100)
    @Size(min = 4, max = 100)
    private String password;

    @Column(name = "FIRSTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String firstname;

    @Column(name = "LASTNAME", length = 50)
    @Size(min = 4, max = 50)
    private String lastname;

    @Column(name = "EMAIL", length = 50,unique = true)
    @NotNull
    @Size(min = 4, max = 50)
    private String email;

    @Column(name = "ENABLED")
    @NotNull
    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "APP_USER_AUTHORITIES",
            joinColumns = { @JoinColumn(name = "APP_USER_ID") },
            inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
    private List<Authority> authorities;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "BIRTH_DATE")
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Column(name = "SEX")
    @Enumerated(EnumType.STRING)
    private UserSex sex;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="FAVORITE_TEAM_ID")
    private Team favoriteTeam;

    @Column(name = "QUOTE")
    private String quote;

    @Min(0)
    @Max(10)
    @Column(name = "LEVEL")
    private Long level;

    @OneToMany(mappedBy="user",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy="owner",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Contest> contests = new HashSet<>();

}