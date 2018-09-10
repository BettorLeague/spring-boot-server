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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
    private String logo;

    @Column
    private Long founded;

    @Column
    private String clubColors;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "teams")
    @JsonIgnore
    private List<Competition> competition = new ArrayList<>();



}
