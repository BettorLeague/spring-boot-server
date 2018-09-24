package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "SEASON")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Season{

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FBD_ID")
    @JsonIgnore
    private Long fbdId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATE")
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "CURRENT_MATCHDAY")
    private Long currentMatchday;

    @Column(name = "AVAILABLE_MATCHDAY")
    private Long availableMatchday;

    @Column(name = "AVAILABLE_MATCH_PER_DAY")
    private Long availableMatchPerDay;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COMPETITION_ID", nullable = false)
    @JsonIgnore
    private Competition competition;
}
