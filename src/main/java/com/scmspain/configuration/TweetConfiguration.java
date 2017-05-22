package com.scmspain.configuration;

import com.scmspain.controller.TweetController;
import com.scmspain.persistence.TweetLinkPersistence;
import com.scmspain.persistence.TweetPersistence;
import com.scmspain.services.TweetService;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class TweetConfiguration {


    @Bean
    public TweetPersistence getTweetPersistence(EntityManager entityManager) {
        return new TweetPersistence(entityManager);
    }

    @Bean
    public TweetLinkPersistence getTweetLinkPersistence(EntityManager entityManager) {
        return new TweetLinkPersistence(entityManager);
    }

    @Bean
    public TweetService getTweetService(MetricWriter metricWriter,
                                        TweetPersistence tweetPersistence,
                                        TweetLinkPersistence tweetLinkPersistence ) {
        return new TweetService(metricWriter,tweetPersistence ,tweetLinkPersistence);
    }

    @Bean
    public TweetController getTweetConfiguration(TweetService tweetService) {
        return new TweetController(tweetService);
    }
}
