package server.batch.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import server.dto.football.AreaDto;
import server.model.football.Area;

public class AreaProcessor implements ItemProcessor<AreaDto,Area> {
    private ModelMapper modelMapper;

    public AreaProcessor(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    @Override
    public Area process(AreaDto areaDto) throws Exception {
        Area area = modelMapper.map(areaDto,Area.class);
        area.setId(null);
        return area;
    }
}