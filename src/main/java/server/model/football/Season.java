package server.model.football;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@EqualsAndHashCode(exclude={"competition"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Season{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @JsonIgnore
    private Long fbdId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column
    private Long currentMatchday;

    @Column
    private Long availableMatchDay;

    @Column
    private Long availableMatchPerDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Competition competition;
}
