package tn.fst.spring.netvoyage.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Enables automatic auditing (e.g., createdAt, updatedAt)
public class JpaConfig {
}
