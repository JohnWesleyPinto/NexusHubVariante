package br.ufpb.dsc.nexushub.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.ufpb.dsc.nexushub")
@EntityScan("br.ufpb.dsc.nexushub.model")
@EnableJpaRepositories("br.ufpb.dsc.nexushub.model")
public class NexusHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusHubApplication.class, args);
    }
}
