package com.scmspain.entities;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by olivernoguera on 22/05/2017.
 */
public class TweetLinkTest {


    @Test
    public void equalsTest() throws Exception {

        TweetLink tweetLink = new TweetLink(1L,0,"http://www.schibsted.es/ ");
        TweetLink tweetLink2 = new TweetLink(1L,0,"http://www.schibsted.es/ ");

        Assert.assertEquals(tweetLink,tweetLink);
        Assert.assertEquals(tweetLink,tweetLink2);

        tweetLink.setTweetId(2L);
        Assert.assertNotEquals(tweetLink,tweetLink2);

        tweetLink.setTweetId(tweetLink2.getTweetId());
        Assert.assertEquals(tweetLink,tweetLink2);

        tweetLink.setLink("http://www.schibsted.com/ " );
        Assert.assertNotEquals(tweetLink,tweetLink2);

        tweetLink.setLink(tweetLink2.getLink());
        Assert.assertEquals(tweetLink,tweetLink2);

        tweetLink.setPosition(2);
        Assert.assertNotEquals(tweetLink,tweetLink2);

        tweetLink.setPosition(tweetLink2.getPosition());
        Assert.assertEquals(tweetLink,tweetLink2);
    }

    @Test
    public void hashCodeTest() throws Exception {

        TweetLink tweetLink = new TweetLink(1L,0,"http://www.schibsted.es/ ");
        TweetLink tweetLink2 = new TweetLink(1L,0,"http://www.schibsted.es/ ");


        Assert.assertEquals(tweetLink.hashCode(),tweetLink2.hashCode());

        tweetLink.setTweetId(2L);
        Assert.assertNotEquals(tweetLink.hashCode(),tweetLink2.hashCode());

        tweetLink.setTweetId(tweetLink2.getTweetId());
        Assert.assertEquals(tweetLink.hashCode(),tweetLink2.hashCode());

        tweetLink.setLink("http://www.schibsted.com/ " );
        Assert.assertNotEquals(tweetLink.hashCode(),tweetLink2.hashCode());

        tweetLink.setLink(tweetLink2.getLink());
        Assert.assertEquals(tweetLink.hashCode(),tweetLink2.hashCode());

        tweetLink.setPosition(2);
        Assert.assertNotEquals(tweetLink.hashCode(),tweetLink2.hashCode());

        tweetLink.setPosition(tweetLink2.getPosition());
        Assert.assertEquals(tweetLink.hashCode(),tweetLink2.hashCode());

    }
}
