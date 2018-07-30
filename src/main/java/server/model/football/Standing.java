package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@EqualsAndHashCode(exclude={"table"})
public class Standing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StandingStage stage;

    @Enumerated(EnumType.STRING)
    private StandingType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "GROUPE")
    private StandingGroup group;

    @OneToMany(mappedBy="standing",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<StandingTable> table = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Competition competition;
}
