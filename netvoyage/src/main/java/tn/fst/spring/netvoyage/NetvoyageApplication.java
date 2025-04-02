package tn.fst.spring.netvoyage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class NetvoyageApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetvoyageApplication.class, args);
    }

}
