package server.repository.football;


import org.springframework.data.jpa.repository.JpaRepository;
import server.model.football.Area;

public interface AreaRepository extends JpaRepository<Area, Long> {
    Area findByName(String name);
    boolean existsByName(String name);
}
