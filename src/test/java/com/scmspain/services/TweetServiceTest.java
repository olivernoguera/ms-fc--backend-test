package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.entities.TweetLink;
import com.scmspain.entities.TypeTweet;
import com.scmspain.persistence.TweetLinkPersistence;
import com.scmspain.persistence.TweetPersistence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TweetServiceTest {

    private TweetPersistence tweetPersistence;
    private TweetLinkPersistence tweetLinkPersistence;
    private MetricWriter metricWriter;
    private TweetService tweetService;

    @Before
    public void setUp() throws Exception {

        this.tweetPersistence = mock(TweetPersistence.class);
        this.metricWriter = mock(MetricWriter.class);
        this.tweetLinkPersistence = mock(TweetLinkPersistence.class);
        this.tweetService = new TweetService(metricWriter,tweetPersistence,tweetLinkPersistence);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowAnExceptionWhenMaxLength() throws Exception {
        tweetService.publishTweet("Pirate", "LeChuck? He's the guy that went to the Governor's for dinner and never " +
                "wanted to leave. He fell for her in a big way, but she told him to drop dead. So he did. " +
                "Then things really got ugly.");
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
        tweetExpected.setId(1L);
        ArgumentCaptor<Tweet> argument = ArgumentCaptor.forClass(Tweet.class);

        when(this.tweetPersistence.upsert(tweetExpected)).thenReturn(tweetExpected.getId());
        when(this.tweetLinkPersistence.findLinksOfTweetId(anyLong())).thenReturn(null);

        tweetService.publishTweet(tweetExpected.getPublisher(), tweetExpected.getTweet());
        verify(this.tweetPersistence).upsert(argument.capture());
        assertEquals(TypeTweet.PUBLISHED, argument.getValue().getType());

    }


    @Test
    public void discardTweetTest(){

        Tweet tweetExpected = new Tweet("MockPublisher", "MockTweet");
        tweetExpected.setId(1L);
        ArgumentCaptor<Tweet> argument = ArgumentCaptor.forClass(Tweet.class);

        when(this.tweetPersistence.upsert(tweetExpected)).thenReturn(tweetExpected.getId());
        when(this.tweetPersistence.findById(anyLong())).thenReturn(tweetExpected);

        tweetService.discardTweet(tweetExpected.getId());
        verify(this.tweetPersistence).upsert(argument.capture());
        assertEquals(TypeTweet.DISCARDED, argument.getValue().getType());

    }

    @Test(expected = IllegalArgumentException.class)
    public void discardTweetNotExistTweetTest(){

        when(this.tweetPersistence.findById(anyLong())).thenReturn(null);
        tweetService.discardTweet(1L);

    }


    @Test
    public void publishTweetTestWithMaxLenghAndNoLinks(){

        StringBuffer link =  new StringBuffer("http://www.schibsted.es/ ");
        StringBuffer text =  new StringBuffer("Tweet 14 size");
        StringBuffer textTweet = new StringBuffer();
        //10*14 = 140
        addTextTimes(textTweet, text, 10);
        addTextTimes(textTweet,link, 4);
        Tweet tweetExpected = new Tweet("MockPublisher",textTweet.toString());
        tweetExpected.setId(1L);
        ArgumentCaptor<Tweet> argument = ArgumentCaptor.forClass(Tweet.class);

        when(this.tweetPersistence.upsert(tweetExpected)).thenReturn(tweetExpected.getId());
        when(this.tweetLinkPersistence.findLinksOfTweetId(anyLong())).thenReturn(null);

        tweetService.publishTweet(tweetExpected.getPublisher(), tweetExpected.getTweet());
        verify(this.tweetPersistence).upsert(argument.capture());
        assertEquals(TypeTweet.PUBLISHED, argument.getValue().getType());
    }

    @Test
    public void getPublishTweetsWithLinks(){

        StringBuffer link =  new StringBuffer("http://www.schibsted.es/ ");
        StringBuffer text =  new StringBuffer("Tweet 14  size");
        StringBuffer textTweet = new StringBuffer();
        //10*14 = 140
        addTextTimes(textTweet, text, 10);

        Tweet mockTweet = new Tweet("MockPublisher",textTweet.toString());
        mockTweet.setId(1L);

        //Add links on tweet expected
        StringBuffer expectedValueTweet = new StringBuffer(textTweet);
        addTextTimes(expectedValueTweet,link, 2);
        Tweet expectedTweet = new Tweet("MockPublisher",expectedValueTweet.toString());
        expectedTweet.setId(1L);

        //verify tweet expected has size with links
        assertEquals(expectedTweet.getTweet().length(),140+link.length()*2);

        TweetLink tweetLink1 = new TweetLink(mockTweet.getId(),140,link.toString());
        TweetLink tweetLink2 = new TweetLink(mockTweet.getId(),140+link.length(),link.toString());

        //verify tweet is on limit size
        assertEquals(mockTweet.getTweet().length(),140);
        when(this.tweetPersistence.findTweetsByType(TypeTweet.PUBLISHED)).thenReturn(Arrays.asList(mockTweet));
        when(this.tweetLinkPersistence.findLinksOfTweetId(anyLong())).thenReturn(Arrays.asList(tweetLink1,tweetLink2));

        List<Tweet> publicshedTweets = tweetService.listPusblishTweets();

        //Only one tweet mock
        assertEquals(publicshedTweets.size(),1);
        //tweet is identical
        assertEquals(publicshedTweets.get(0),expectedTweet);
        //verify size of tweet
        assertEquals(publicshedTweets.get(0).getTweet().length(),140+link.length()*2);

    }



    @Test(expected = IllegalArgumentException.class)
    public void publishTweetTestWitMaxLenghAndBadLink(){

        StringBuffer link =  new StringBuffer("http://www.schibsted.es/");
        StringBuffer text =  new StringBuffer("Tweet  14 size");
        StringBuffer textTweet = new StringBuffer();
        //10*14 = 140
        addTextTimes(textTweet, text, 10);
        addTextTimes(textTweet,link, 1);
        Tweet tweetExpected = new Tweet("MockPublisher",textTweet.toString()+"1");
        tweetExpected.setId(1L);
        tweetService.publishTweet(tweetExpected.getPublisher(), tweetExpected.getTweet());
    }


    @Test(expected = IllegalArgumentException.class)
    public void publishTweetTestWithMoreMaxLenghAndLinks(){

        StringBuffer link =  new StringBuffer("http://www.schibsted.es/ ");
        StringBuffer text =  new StringBuffer("Tweet  14 size");
        StringBuffer textTweet = new StringBuffer();
        //10*14 = 140
        addTextTimes(textTweet, text, 10);
        addTextTimes(textTweet,link, 4);
        Tweet tweetExpected = new Tweet("MockPublisher",textTweet.toString()+"1");
        tweetExpected.setId(1L);
        tweetService.publishTweet(tweetExpected.getPublisher(), tweetExpected.getTweet());
    }

    private static  void addTextTimes(StringBuffer buffer, StringBuffer patternToAdd, int times){
        for(int i = 0; i < times; i++) {
            buffer.append(patternToAdd);
        }
    }

}
