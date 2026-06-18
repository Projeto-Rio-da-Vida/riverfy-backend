package br.com.riverfy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Startup {

    private static final Logger log = LoggerFactory.getLogger(Startup.class);

    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
        log.info("Application Riverfy Backend started successfully!");
    }

}
