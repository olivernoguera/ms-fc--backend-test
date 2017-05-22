package com.scmspain.entities;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by olivernoguera on 22/05/2017.
 */
public class TweetTest {


    @Test
    public void equalsTest() throws Exception {
        Tweet tweet = new Tweet("publisher", "tweet");
        tweet.setId(1L);
        tweet.setDiscarded(false);
        Tweet tweet2 = new Tweet("publisher", "tweet");
        tweet2.setId(1L);
        tweet2.setDiscarded(false);

        Assert.assertEquals(tweet,tweet);
        Assert.assertEquals(tweet,tweet2);

        tweet2.setDiscarded(true);
        Assert.assertNotEquals(tweet,tweet2);

        tweet2.setDiscarded(false);
        Assert.assertEquals(tweet,tweet2);
        tweet2.setId(2L);
        Assert.assertNotEquals(tweet,tweet2);

        tweet2.setId(1L);
        Assert.assertEquals(tweet,tweet2);
        tweet2.setPre2015MigrationStatus(1L);
        Assert.assertNotEquals(tweet,tweet2);

        tweet2.setPre2015MigrationStatus(0L);
        Assert.assertEquals(tweet,tweet2);
        tweet2.setPublisher("publisher2");
        Assert.assertNotEquals(tweet,tweet2);

        tweet2.setPublisher(tweet.getPublisher());
        Assert.assertEquals(tweet,tweet2);
        tweet2.setTweet("tweet2");
        Assert.assertNotEquals(tweet,tweet2);

        tweet2.setTweet(tweet.getTweet());
        Assert.assertEquals(tweet,tweet2);

    }

    @Test
    public void hashCodeTest() throws Exception {

        Tweet tweet = new Tweet("publisher", "tweet");
        tweet.setId(1L);
        tweet.setDiscarded(false);
        Tweet tweet2 = new Tweet("publisher", "tweet");
        tweet2.setId(1L);
        tweet2.setDiscarded(false);


        Assert.assertEquals(tweet.hashCode(),tweet2.hashCode());

        tweet2.setDiscarded(true);
        Assert.assertNotEquals(tweet.hashCode(),tweet2.hashCode());

        tweet2.setDiscarded(false);
        Assert.assertEquals(tweet.hashCode(),tweet2.hashCode());
        tweet2.setId(2L);
        Assert.assertNotEquals(tweet.hashCode(),tweet2.hashCode());

        tweet2.setId(1L);
        Assert.assertEquals(tweet.hashCode(),tweet2.hashCode());
        tweet2.setPre2015MigrationStatus(1L);
        Assert.assertNotEquals(tweet.hashCode(),tweet2.hashCode());

        tweet2.setPre2015MigrationStatus(0L);
        Assert.assertEquals(tweet.hashCode(),tweet2.hashCode());
        tweet2.setPublisher("publisher2");
        Assert.assertNotEquals(tweet.hashCode(),tweet2.hashCode());

        tweet2.setPublisher(tweet.getPublisher());
        Assert.assertEquals(tweet.hashCode(),tweet2.hashCode());
        tweet2.setTweet("tweet2");
        Assert.assertNotEquals(tweet.hashCode(),tweet2.hashCode());

        tweet2.setTweet(tweet.getTweet());
        Assert.assertEquals(tweet.hashCode(),tweet2.hashCode());

    }
}
