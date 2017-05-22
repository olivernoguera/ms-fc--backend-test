package com.scmspain.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by olivernoguera on 22/05/2017.
 */
public class DiscardTweetCommand {

    @JsonProperty("tweet")
    private Long tweetId;

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }
}
