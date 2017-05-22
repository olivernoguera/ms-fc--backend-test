package com.scmspain.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;


/**
 + A link is any set of non-whitespace consecutive characters starting with http:// or https:// and finishing with a space.
 For example, the tweet Hey http://foogle.co is 4 characters long, instead of 20.
 So this must not affect to 140 characters limitation
 */
@Entity
public class TweetLink implements Serializable{

    @Id
    private Long tweetId;

    @Id
    private Integer position;

    @Column(nullable = false)
    private String link;

    public TweetLink() {
    }

    public TweetLink(Long tweetId, Integer position, String link) {
        this.tweetId = tweetId;
        this.position = position;
        this.link = link;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TweetLink tweetLink = (TweetLink) o;

        if (tweetId != null ? !tweetId.equals(tweetLink.tweetId) : tweetLink.tweetId != null) return false;
        if (position != null ? !position.equals(tweetLink.position) : tweetLink.position != null) return false;
        return link != null ? link.equals(tweetLink.link) : tweetLink.link == null;

    }

    @Override
    public int hashCode() {
        int result = tweetId != null ? tweetId.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (link != null ? link.hashCode() : 0);
        return result;
    }
}
