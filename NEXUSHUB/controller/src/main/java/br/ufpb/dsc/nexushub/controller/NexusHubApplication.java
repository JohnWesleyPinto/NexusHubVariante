package br.ufpb.dsc.nexushub.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "br.ufpb.dsc.nexushub")
@EntityScan("br.ufpb.dsc.nexushub.model.entity")
@EnableJpaRepositories("br.ufpb.dsc.nexushub.model.repository")
public class NexusHubApplication {

    public static void main(String[] args) {
        SpringApplication.run(NexusHubApplication.class, args);
    }
}
