package ch.elca.candidateassess.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author akn
 */
@Configuration
@ComponentScan({"ch.elca.candidateassess"})
@EnableJpaRepositories("ch.elca.candidateassess.persistence.repository")
@EntityScan("ch.elca.candidateassess.persistence.entity")
@EnableJpaAuditing
public class CandidateAssessConfig {


}