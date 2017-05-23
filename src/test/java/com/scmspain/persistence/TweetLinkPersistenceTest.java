package com.scmspain.persistence;

import com.scmspain.entities.Tweet;
import com.scmspain.entities.TweetLink;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Arrays;
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
public class TweetLinkPersistenceTest {

    private EntityManager entityManager;
    private TweetLinkPersistence tweeLinkPersistence;


    @Before
    public void setUp() throws Exception {
        this.entityManager = mock(EntityManager.class);
        this.tweeLinkPersistence = new TweetLinkPersistence(entityManager);
    }

    @Test
    public void shouldInsertANewTweetLinks() throws Exception {
        TweetLink tweetLink = new TweetLink(1L, 0 ,"http://www.schibsted.es/ ");
        tweeLinkPersistence.saveLinks(Arrays.asList(tweetLink));
        verify(entityManager).persist(any(Tweet.class));
    }


    @Test(expected = IllegalArgumentException.class)
    public void shouldGetTweetsPersistenceException() throws Exception {

        when(entityManager.createQuery(anyString())).thenThrow(PersistenceException.class);
        tweeLinkPersistence.findLinksOfTweetId(1L);
    }

    @Test
    public void findTweetLinks() throws Exception {

        Query queryMock = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(queryMock);

        List<TweetLink> tweetLinkList = new ArrayList<>();
        TweetLink tweetLink1 = new TweetLink(1L, 0 ,"http://www.schibsted.es/ ");
        tweetLinkList.add(tweetLink1);

        TweetLink tweetLink2 = new TweetLink(2L, 0 ,"http://www.schibsted.es/ ");
        tweetLinkList.add(tweetLink2);

        when(queryMock.getResultList()).thenReturn(tweetLinkList);

        List<TweetLink> tweetsLinksResult =
                tweeLinkPersistence.findLinksOfTweetId(tweetLink1.getTweetId());
        Assert.assertThat(tweetsLinksResult.size(), is(2));
        Assert.assertThat(tweetsLinksResult, is(tweetLinkList));
    }





}
