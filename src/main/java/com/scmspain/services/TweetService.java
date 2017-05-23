package com.scmspain.services;

import com.scmspain.entities.Tweet;
import com.scmspain.entities.TweetLink;
import com.scmspain.entities.TypeTweet;
import com.scmspain.persistence.TweetLinkPersistence;
import com.scmspain.persistence.TweetPersistence;
import org.springframework.boot.actuate.metrics.writer.Delta;
import org.springframework.boot.actuate.metrics.writer.MetricWriter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  This class is responsible to control logical of tweets
 *  This service has all user cases of tweets
 *  1. Publish Tweet
 *  2. Get publisehd tweets.
 *  3. Discard tweet
 *  4. Get discarded tweets.
 *
 */

@Service
public class TweetService {

    private TweetPersistence tweetPersistence;
    private TweetLinkPersistence tweetLinkPersistence;
    private MetricWriter metricWriter;

    private final static String LINK_EXPRESSION = "(http|https)+[://][-a-zA-Z0-9+&@#/%?=~_|!¡:,.;]+[\\s]";
    private final static Pattern PATTERN_LINK = Pattern.compile(LINK_EXPRESSION);

    public TweetService(final MetricWriter metricWriter,
                        final TweetPersistence tweetPersistence,
                        final TweetLinkPersistence tweetLinkPersistence) {
        this.metricWriter = metricWriter;
        this.tweetPersistence = tweetPersistence;
        this.tweetLinkPersistence = tweetLinkPersistence;
    }

    /**
      Push tweet and links into repositories.It annoted with transational to encapsulate in one trasaction
       both repositories access
      Parameter - publisher - creator of the Tweet
      Parameter - text - Content of the Tweet
      Result - recovered Tweet
    */
    @Transactional
    public void publishTweet(String publisher, String text) {

        if (publisher != null && publisher.length() > 0 && text != null && text.length() > 0 ) {
            String textWithoutLinks = extractLinks(text);
            if( textWithoutLinks.length() > 140){
                throw new IllegalArgumentException("Tweet must not be greater than 140 characters");
            }
            Tweet tweet = new Tweet(publisher,textWithoutLinks);
            tweet.setType(TypeTweet.PUBLISHED);
            tweet.setLastUpdated(new Date());

            this.metricWriter.increment(new Delta<Number>("published-tweets", 1));

            Long tweetId = this.tweetPersistence.upsert(tweet);
            List<TweetLink> links  = extractLinks(tweetId, text);
            tweetLinkPersistence.saveLinks(links);

        } else {
            throw new IllegalArgumentException("Publisher and tweet must not be empty");
        }
    }



    /**
     Recover Published tweets from repository
     Result - retrieved publish Tweets
     */
    public List<Tweet> listPusblishTweets() {

        this.metricWriter.increment(new Delta<Number>("times-tweets-publish", 1));
        List<Tweet> tweets = this.tweetPersistence.findTweetsByType(TypeTweet.PUBLISHED);
        addLinksToTweets(tweets);
        return tweets;
    }

    /**
     * This mehtod add links to tweets
     * @param tweets to add Links
     */
    private void addLinksToTweets(List<Tweet> tweets) {
        tweets.stream().forEach(tweet -> addLinksToTweet(tweet));
    }

    /**
     Mark tweet how discard
     Parameter - tweetId - identifies tweet
     if tweetId not exist do nothing
     */
    @Transactional
    public void discardTweet(Long tweetId) {

        Tweet tweet = this.tweetPersistence.findById(tweetId);
        if( tweet != null){
            tweet.setType(TypeTweet.DISCARDED);
            tweet.setLastUpdated(new Date());
            this.metricWriter.increment(new Delta<Number>("discard-tweet", 1));
            this.tweetPersistence.upsert(tweet);
        }else{
            throw new IllegalArgumentException(String.format("Tweet %d not exists", tweetId));
        }
    }


    /**
     Recover Discarded tweets from repository
     Result - retrieved discard Tweets
     */
    public List<Tweet> dlistDiscardedTweets() {
        this.metricWriter.increment(new Delta<Number>("times-tweets-discarded", 1));
        List<Tweet> tweets = this.tweetPersistence.findTweetsByType(TypeTweet.DISCARDED);
        addLinksToTweets(tweets);
        return tweets;
    }


    /**
     * Add links to tweet
     * @param tweet tweet I/O parameter tweet destionations on links
     */
    private  void addLinksToTweet(Tweet tweet){

         if(tweet != null ) {
             List<TweetLink> tweetLinks = tweetLinkPersistence.findLinksOfTweetId(tweet.getId());
             if( tweetLinks != null && !tweetLinks.isEmpty()){
                 StringBuffer stringBuffer = new StringBuffer(tweet.getTweet());

                 for (TweetLink tweetLink : tweetLinks) {
                     stringBuffer.insert(tweetLink.getPosition(), tweetLink.getLink());
                 }
                 tweet.setTweet(stringBuffer.toString());
             }

         }
    }


    /**
     * @param  text to extract links
     * @return return text without links
     */
    public static String extractLinks(String text) {

        final Matcher matcher = PATTERN_LINK.matcher(text);
        final StringBuffer stringBuffer = new StringBuffer(text);

        while (matcher.find()) {
            String link = matcher.group();
            stringBuffer.delete(stringBuffer.indexOf(link), (stringBuffer.indexOf(link) + link.length()));
        }

        return stringBuffer.toString();
    }

    /**
     * @param  tweetId of  links
     * @param  text to extract links
     * @return return list of links without tweetId
     */
    public static List<TweetLink> extractLinks(Long tweetId, String text) {


        final Matcher matcher = PATTERN_LINK.matcher(text);
        final StringBuffer stringBuffer = new StringBuffer(text);
        List<TweetLink> tweetLinks = new ArrayList<>();

        while (matcher.find()) {

            int position = matcher.start();
            String link = matcher.group();
            final TweetLink tweetLink = new TweetLink(tweetId,position, link);
            tweetLinks.add(tweetLink);
            stringBuffer.delete(stringBuffer.indexOf(link), (stringBuffer.indexOf(link) + link.length()));
        }

        return tweetLinks;
    }

}
