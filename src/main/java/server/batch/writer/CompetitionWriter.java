package server.batch.writer;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import server.model.football.Area;
import server.model.football.Competition;
import server.model.football.Season;
import server.repository.football.AreaRepository;
import server.repository.football.CompetitionRepository;
import server.repository.football.SeasonRepository;

import java.util.List;

public class CompetitionWriter implements ItemWriter<Competition> {
    private CompetitionRepository competitionRepository;
    private SeasonRepository seasonRepository;
    private AreaRepository areaRepository;

    public CompetitionWriter(CompetitionRepository competitionRepository,
                             SeasonRepository seasonRepository,
                             AreaRepository areaRepository) {
        this.competitionRepository = competitionRepository;
        this.seasonRepository = seasonRepository;
        this.areaRepository = areaRepository;
    }

    @Override
    public void write(List<? extends Competition> competitions) throws Exception {
        for(Competition competition: competitions){
            if(!competitionRepository.existsByName(competition.getName())){

                if(!areaRepository.existsByName(competition.getArea().getName())){
                    competition.setArea(areaRepository.save(competition.getArea()));
                }else{
                    competition.setArea(areaRepository.findByName(competition.getArea().getName()));
                }

                List<Season> seasons = competition.getSeasons();

                competition.setSeasons(null);
                competition.setCurrentSeason(null);
                competition = competitionRepository.save(competition);

                for(Season season: seasons){
                    season.setCompetition(competition);
                    season = seasonRepository.save(season);
                }

                competition.setSeasons(seasons);
                competition.setCurrentSeason(seasons.get(0));
                competition = competitionRepository.save(competition);

            }
        }
    }
}
