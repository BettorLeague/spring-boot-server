package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import server.model.bettor.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Standing implements Comparable<Standing>{

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

    @OneToMany(mappedBy="standing",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StandingTable> table = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "competition_id", nullable = false)
    @JsonIgnore
    private Competition competition;

    @Override
    public int compareTo(Standing other) {
        if(this.getGroup() != null && other.getGroup() != null){
            int group = this.group.compareTo(other.getGroup());
            if (group != 0) {
                return group;
            }
        }
        return 1;
    }

}
