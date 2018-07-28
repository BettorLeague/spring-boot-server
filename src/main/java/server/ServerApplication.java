package server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import server.repository.user.AuthorityRepository;
import server.repository.user.UserRepository;
import server.service.UserService;

@EnableScheduling
@SpringBootApplication
@ComponentScan
public class ServerApplication implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	UserService userService;


	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		userService.addAdminAccount();
	}
}
