package server.model.football;



import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String nationality;
}
