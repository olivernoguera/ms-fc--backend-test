package com.scmspain.persistence;

import com.scmspain.entities.TweetLink;
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
 *  This class is responsible for persistence of links of tweets.
 *
 */
@Repository
public class TweetLinkPersistence {


    private static final Logger LOGGER = LoggerFactory.getLogger(TweetLinkPersistence.class);
    private static final String GET_LINKS_OF_TWEET = "SELECT l FROM TweetLink AS l " +
            "  WHERE l.tweetId = %s ORDER BY position ASC";


    private EntityManager entityManager;

    public TweetLinkPersistence(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }




    /**
     Parameter tweet id
     Recover tweets links from repository
     Result - retrieved List of Tweet's
     */
    public List<TweetLink> findLinksOfTweetId(Long tweetId) {
        String qurery = String.format(GET_LINKS_OF_TWEET, tweetId);
        List<TweetLink> tweetLinks = null;
        try{
            final Query query = this.entityManager.createQuery(qurery);
            tweetLinks = query.getResultList();
        }catch (PersistenceException ex){
            LOGGER.error(ex.getMessage(), ex);
            throw new IllegalArgumentException(ex.getMessage(),ex);
        }

        LOGGER.debug("Get  tweetLinks"+ tweetLinks.size() + "!!");
        return tweetLinks;
    }


    /**
     Push TweetLinks to repository
     Parameter - tweetLinks - list of entity to save
     */
    @Transactional
    public void saveLinks(List<TweetLink> links) {

        links.stream().forEach(l->  this.entityManager.persist(l));
    }

}
