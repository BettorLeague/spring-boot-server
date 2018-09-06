package server.model.bettor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import server.model.football.Competition;
import server.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contest {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String caption;

    @ManyToOne(fetch = FetchType.LAZY)
    private User owner;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private ContestType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Competition competition;

    @Column
    private int numberOfPlayers = 1;

    @OneToMany(mappedBy="contest",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Player> players = new ArrayList<>();

    @OneToMany(mappedBy="contest",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();



}
