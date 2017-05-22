package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.persistence.TweetPersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetServiceTest {

    private TweetPersistence tweetPersistence;
    private MetricWriter metricWriter;
    private TweetService tweetService;

    @Before
    public void setUp() throws Exception {

        this.tweetPersistence = mock(TweetPersistence.class);
        this.metricWriter = mock(MetricWriter.class);
        this.tweetService = new TweetService(metricWriter,tweetPersistence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenMaxLength() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. Then things really got ugly.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenEmptyTweet() throws Exception {
        tweetService.publishTweet("Pirate", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenNullTweet() throws Exception {
        tweetService.publishTweet("Pirate", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenEmptyPublisher() throws Exception {
        tweetService.publishTweet("", "Correct tweet");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenNullPublisher() throws Exception {
        tweetService.publishTweet(null, "Correct tweet");
    }

    @Test
    public void publishTweetTest(){

        Tweet tweetExpected = new Tweet("MockPublisher", "MockTweet");
        ArgumentCaptor<Tweet> argument = ArgumentCaptor.forClass(Tweet.class);

        doNothing().when(this.tweetPersistence).upsert(tweetExpected);

        tweetService.publishTweet(tweetExpected.getPublisher(), tweetExpected.getTweet());
        verify(this.tweetPersistence).upsert(argument.capture());
        assertEquals(false, argument.getValue().isDiscarded());

    }


    @Test
    public void discardTweetTest(){

        Tweet tweetExpected = new Tweet("MockPublisher", "MockTweet");
        ArgumentCaptor<Tweet> argument = ArgumentCaptor.forClass(Tweet.class);
        ;
        doNothing().when(this.tweetPersistence).upsert(tweetExpected);
        when(this.tweetPersistence.findById(anyLong())).thenReturn(tweetExpected);

        tweetService.discardTweet(tweetExpected.getId());
        verify(this.tweetPersistence).upsert(argument.capture());
        assertEquals(true, argument.getValue().isDiscarded());

    }

    @Test
    public void discardTweetNotExistTweetTest(){

        when(this.tweetPersistence.findById(anyLong())).thenReturn(null);
        tweetService.discardTweet(1L);


    }

}
