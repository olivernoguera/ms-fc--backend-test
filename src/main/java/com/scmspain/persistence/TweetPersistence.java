package com.scmspain.persistence;

import com.scmspain.entities.Tweet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by olivernoguera on 22/05/2017.
 */
@Repository
public class TweetPersistence {


    private static final Logger LOGGER = LoggerFactory.getLogger(TweetPersistence.class);
    private static final String GET_PUBLISH_TWEETS = "SELECT t FROM Tweet AS t " +
            " WHERE pre2015MigrationStatus <> 99 AND discarded = false ORDER BY id DESC";

    private static final String GET_DISCARD_TWEETS = "SELECT t FROM Tweet AS t " +
            " WHERE pre2015MigrationStatus <> 99 AND discarded = true ORDER BY id DESC";

    private EntityManager entityManager;

    public TweetPersistence(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     Push tweet to repository
     Parameter - tweet - entity to upsert
     */
    @Transactional
    public void upsert(Tweet tweet) {
        this.entityManager.persist(tweet);
    }


    /**
     Recover tweet from repository
     Result - retrieved List of Tweet's
     */
    public List<Tweet> findPublishTweets() {

        return getTweetsByQuery(GET_PUBLISH_TWEETS);
    }


    /**
     * Recover list of tweets for a query String
     * @param - Quert  of set elements to list
     * @return
     */
    private List<Tweet> getTweetsByQuery(String queryString) {
        List<Tweet> tweets = null;
        try{
            final Query query = this.entityManager.createQuery(queryString);
            tweets = query.getResultList();
        }catch (PersistenceException ex){
            LOGGER.error("Bad query ", ex);
            throw new IllegalArgumentException(ex.getMessage(),ex);
        }

        LOGGER.debug("Get  "+ tweets.size() + "!!");
        return tweets;
    }

    /**
     Recover tweet from repository
     Result - retrieved  Tweet
     */
    public Tweet findById(Long tweetId) {
        return this.entityManager.find(Tweet.class,tweetId);
    }
}
