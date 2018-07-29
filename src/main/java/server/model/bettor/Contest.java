package server.model.bettor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import server.model.football.Competition;
import server.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CONTEST")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contest {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CAPTION")
    @NotNull
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Column(name = "TYPE", length = 50)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContestType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Competition competition;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "CONTEST_USER",
            joinColumns = {@JoinColumn(name = "CONTEST_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "APP_USER_ID", referencedColumnName = "ID")})
    @JsonIgnore
    private Set<Player> players = new HashSet<>();

    @OneToMany(mappedBy="contest",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Message> messages = new HashSet<>();



}
