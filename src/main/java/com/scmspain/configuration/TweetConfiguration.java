package com.scmspain.configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.persistence.TweetPersistence;
import com.scmspain.services.TweetService;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class TweetConfiguration {
    @Bean
    public TweetPersistence getTweetDao(EntityManager entityManager) {
        return new TweetPersistence(entityManager);
    }

    @Bean
    public TweetService getTweetService(MetricWriter metricWriter,TweetPersistence tweetDao) {
        return new TweetService(metricWriter,tweetDao );
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
        return new TweetController(tweetService);
    }
}
