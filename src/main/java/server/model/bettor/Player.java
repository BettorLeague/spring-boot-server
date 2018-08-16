package server.model.bettor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"user","pronostics"})
public class Player {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Contest contest;

    @OneToMany(mappedBy="player",cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Pronostic> pronostics = new HashSet<>();

    @Column(name = "POINTS")
    private int points;

    @Column(name = "GOOD_PRONOSTIC")
    private int goodPronostic;

    @Column(name = "EXACT_PRONOSTIC")
    private int exactPronostic;


}
