package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.persistence.TweetPersistence;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TweetService {

    private TweetPersistence tweetPersistence;
    private MetricWriter metricWriter;

    public TweetService(final MetricWriter metricWriter, final TweetPersistence tweetPersistence) {
        this.metricWriter = metricWriter;
        this.tweetPersistence = tweetPersistence;
    }

    /**
      Push tweet to repository
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    public void publishTweet(String publisher, String text) {

        if (publisher != null && publisher.length() > 0 && text != null && text.length() > 0 && text.length() < 140) {
            Tweet tweet = new Tweet();
            tweet.setTweet(text);
            tweet.setPublisher(publisher);

            this.metricWriter.increment(new Delta<Number>("published-tweets", 1));
            this.tweetPersistence.save(tweet);
        } else {
            throw new IllegalArgumentException("Tweet must not be greater than 140 characters");
        }
    }


    /**
     Recover tweets from repository
     Result - retrieved Tweets
     */
    public List<Tweet> listAllTweets() {

        this.metricWriter.increment(new Delta<Number>("times-queried-tweets", 1));
        List<Tweet> tweets = this.tweetPersistence.findTweets();
        return tweets;
    }
}
