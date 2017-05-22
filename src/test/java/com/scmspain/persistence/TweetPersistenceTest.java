package com.scmspain.persistence;

import com.scmspain.entities.Tweet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by olivernoguera on 22/05/2017.
 */
public class TweetPersistenceTest {

    private EntityManager entityManager;
    private TweetPersistence tweetPersistence;


    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.tweetPersistence = new TweetPersistence(entityManager);
    }

    @Test
    public void shouldInsertANewTweet() throws Exception {
        Tweet tweet = new Tweet();
        tweet.setTweet("Guybrush Threepwood");
        tweet.setPublisher("I am Guybrush Threepwood, mighty pirate.");
        tweetPersistence.save(tweet);

        verify(entityManager).persist(any(Tweet.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldGetTweetsPersistenceException() throws Exception {

        when(entityManager.createQuery(anyString())).thenThrow(PersistenceException.class);
        tweetPersistence.findTweets();

    }

    @Test
    public void shouldGetTweets() throws Exception {

        Query queryMock = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(queryMock);

        List<Tweet> tweetList = new ArrayList<>();
        Tweet tweet1 = new Tweet("mockPublisher1","mockTweet1");
        tweet1.setId(1L);
        tweetList.add(tweet1);

        Tweet tweet2 = new Tweet("mockPublisher2","mockTweet2");
        tweet2.setId(2L);
        tweetList.add(tweet2);

        when(queryMock.getResultList()).thenReturn(tweetList);

        List<Tweet> tweetsResult = tweetPersistence.findTweets();
        Tweet firstTweet =
                tweetsResult.stream().filter(t->t.getId().equals(tweet1.getId())).findFirst().get();

        Assert.assertThat(firstTweet.getId(), is(tweet1.getId()));
        Assert.assertThat(firstTweet.getPublisher(), is(tweet1.getPublisher()));
        Assert.assertThat(firstTweet.getTweet(), is(tweet1.getTweet()));
        Assert.assertThat(firstTweet.getPre2015MigrationStatus(), is(tweet1.getPre2015MigrationStatus()));
        Assert.assertThat(tweetsResult.size(), is(2));


    }
}
