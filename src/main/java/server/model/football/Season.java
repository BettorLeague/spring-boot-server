package server.model.football;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Season{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String startDate;

    @Column
    private String endDate;

    @Column
    private Long currentMatchday;

    @OneToOne(fetch = FetchType.LAZY)
    private Competition competition;
}
