package com.scmspain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.util.Date;
import java.util.List;

@Entity
@JsonPropertyOrder({"id", "publisher", "tweet", "pre2015MigrationStatus"})
public class Tweet {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false, length = 140)
    private String tweet;

    @Column (nullable=true)
    private Long pre2015MigrationStatus = 0L;

    @Column (nullable = false)
    @JsonIgnore
    private boolean discarded;

    @Column (nullable = false)
    @JsonIgnore
    private Date lastUpdated;


    public Tweet() {
    }

    public Tweet(String publisher, String tweet) {
        this.publisher = publisher;
        this.tweet = tweet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Long getPre2015MigrationStatus() {
        return pre2015MigrationStatus;
    }

    public void setPre2015MigrationStatus(Long pre2015MigrationStatus) {
        this.pre2015MigrationStatus = pre2015MigrationStatus;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }


    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet1 = (Tweet) o;

        if (discarded != tweet1.discarded) return false;
        if (id != null ? !id.equals(tweet1.id) : tweet1.id != null) return false;
        if (publisher != null ? !publisher.equals(tweet1.publisher) : tweet1.publisher != null) return false;
        if (tweet != null ? !tweet.equals(tweet1.tweet) : tweet1.tweet != null) return false;
        if (lastUpdated != null ? !lastUpdated.equals(tweet1.lastUpdated) : tweet1.lastUpdated != null) return false;
        return pre2015MigrationStatus != null ? pre2015MigrationStatus.equals(tweet1.pre2015MigrationStatus) : tweet1.pre2015MigrationStatus == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publisher != null ? publisher.hashCode() : 0);
        result = 31 * result + (tweet != null ? tweet.hashCode() : 0);
        result = 31 * result + (pre2015MigrationStatus != null ? pre2015MigrationStatus.hashCode() : 0);
        result = 31 * result + (discarded ? 1 : 0);
        result = 31 * result + (lastUpdated != null  ? lastUpdated.hashCode(): 0);
        return result;
    }
}
