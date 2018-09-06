package server.model.bettor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import server.model.football.StandingTable;
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
public class Player implements Comparable<Player> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "contest_id", nullable = false)
    @NotNull
    @JsonIgnore
    private Contest contest;

    @OneToMany(mappedBy="player",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pronostic> pronostics = new ArrayList<>();

    @Column(name = "POINTS")
    private int points;

    @Column(name = "GOOD_PRONOSTIC")
    private int goodPronostic;

    @Column(name = "EXACT_PRONOSTIC")
    private int exactPronostic;

    @OneToMany(mappedBy="player",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.points,this.points);
    }

}
