package server.batch.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import server.dto.football.CompetitionDto;
import server.model.football.Competition;
import server.model.football.Season;

public class CompetitionProcessor implements ItemProcessor<CompetitionDto,Competition> {
    private ModelMapper modelMapper;

    public CompetitionProcessor(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public Competition process(CompetitionDto competitionDto) throws Exception {
        Competition competition = modelMapper.map(competitionDto,Competition.class);
        competition.setId(null);
        competition.getArea().setId(null);
        competition.getCurrentSeason().setId(null);
        for (Season season: competition.getSeasons()){
            season.setId(null);
        }
        return competition;
    }
}