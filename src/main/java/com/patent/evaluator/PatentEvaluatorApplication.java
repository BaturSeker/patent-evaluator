package com.patent.evaluator;

import com.patent.evaluator.abstracts.BaseRepositoryCustomImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaRepositories(value = {"com.patent.evaluator"}, repositoryBaseClass = BaseRepositoryCustomImpl.class)
@EnableScheduling
@SpringBootApplication
public class PatentEvaluatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PatentEvaluatorApplication.class, args);
    }

}
