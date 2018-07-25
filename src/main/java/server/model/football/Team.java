package server.model.football;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Area area;

    @NotNull
    @Column(unique = true)
    private String name;

    @Column
    private String shortName;

    @Column
    private String tla;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String website;

    @Column
    private String email;

    @Column
    private Long founded;

    @Column
    private String clubColors;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Competition> competition;
}
