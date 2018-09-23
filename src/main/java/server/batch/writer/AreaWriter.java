package server.batch.writer;

import org.springframework.batch.item.ItemWriter;
import server.model.football.Area;
import server.model.football.Team;
import server.repository.football.AreaRepository;
import server.repository.football.TeamRepository;

import java.util.List;

public class AreaWriter implements ItemWriter<Area> {
    private AreaRepository areaRepository;

    public AreaWriter(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    @Override
    public void write(List<? extends Area> areas) throws Exception {
        areas.forEach(area -> {
            if(!areaRepository.existsByName(area.getName())){
                area.setId(null);
                areaRepository.save(area);
            }
        });
    }
}
