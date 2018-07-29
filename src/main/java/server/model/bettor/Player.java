package server.model.bettor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private User user;

    @OneToOne
    @NotNull
    private Contest contest;

    @OneToMany(mappedBy="player",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Pronostic> pronostics = new HashSet<>();

    @Column(name = "POINTS")
    private int points;

    @Column(name = "GOOD_PRONOSTIC")
    private int goodPronostic;

    @Column(name = "EXACT_PRONOSTIC")
    private int exactPronostic;


}
