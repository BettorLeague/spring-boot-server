package server.batch;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import server.batch.processor.CompetitionProcessor;
import server.batch.reader.CompetitionReader;
import server.batch.writer.CompetitionWriter;
import server.config.RestTemplateInterceptor;
import server.dto.football.CompetitionDto;
import server.model.football.Competition;
import server.repository.football.AreaRepository;
import server.repository.football.CompetitionRepository;
import server.repository.football.SeasonRepository;
import server.repository.football.TeamRepository;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableMBeanExport
@Slf4j
@EnableScheduling
public class BatchConfig {

    private final List<String> competitions = Arrays.asList("2000", "2001", "2002","2014","2015","2019","2021");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobLauncher jobLauncher;

    private final TeamRepository teamRepository;
    private final AreaRepository areaRepository;
    private final CompetitionRepository competitionRepository;
    private final SeasonRepository seasonRepository;

    private RestTemplate restTemplate;
    private ModelMapper modelMapper;

    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       JobLauncher jobLauncher,
                       TeamRepository teamRepository,
                       SeasonRepository seasonRepository,
                       CompetitionRepository competitionRepository,
                       AreaRepository areaRepository){
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.jobLauncher = jobLauncher;
        this.areaRepository = areaRepository;
        this.teamRepository = teamRepository;
        this.competitionRepository = competitionRepository;
        this.seasonRepository = seasonRepository;
        this.modelMapper = new ModelMapper();
        this.restTemplate = new RestTemplate();
        this.restTemplate.getInterceptors().add(new RestTemplateInterceptor());
    }




    private Step competitionStep(String compeititonId){

        return stepBuilderFactory.get("competition-step")
                .<CompetitionDto, Competition>chunk(1)
                .reader(new CompetitionReader(compeititonId,this.restTemplate))
                .processor(new CompetitionProcessor(this.modelMapper))
                .writer(new CompetitionWriter(this.competitionRepository,this.seasonRepository,this.areaRepository))
                .build();

    }
/*
    private Step competitionTeamStep(String competitionId,String competitionName){
        return stepBuilderFactory.get("competition-step")
                .<TeamDto, Team>chunk(1)
                .reader(new CompetitionTeamReader(competitionId,this.restTemplate))
                .processor()
                .writer()
                .build();
    }*/


    private Job competitionJob(String competitionId){

        return jobBuilderFactory.get("CompetitionJob")
                .incrementer(new RunIdIncrementer())
                .flow(this.competitionStep(competitionId))
                //.next(this.competitionTeamStep(competitionId))
                .end()
                .build();
    }


    //@Scheduled(cron = "0 0 0 * * *", zone = "Europe/Paris") // à minuit
    //@Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void getAllArea() throws Exception {
        JobParameters param = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution execution = this.jobLauncher.run(competitionJob("2015"),param);
    }


}