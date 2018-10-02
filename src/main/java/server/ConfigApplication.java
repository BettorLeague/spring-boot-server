package server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import server.service.UserService;

@Component
@Slf4j
public class ConfigApplication implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public ConfigApplication(@Lazy UserService userService){
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.warn("Loading data...");
        userService.addAdminAccount();
        log.warn("Admin account added");
    }

}

