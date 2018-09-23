package server.batch.processor;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;
import server.dto.football.TeamDto;
import server.model.football.Team;

public class CompetitionTeamProcessor implements ItemProcessor<TeamDto,Team> {

    private ModelMapper modelMapper;

    public CompetitionTeamProcessor(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public Team process(TeamDto teamDto) throws Exception {
        Team team = modelMapper.map(teamDto,Team.class);
        team.setId(null);
        return team;
    }
}