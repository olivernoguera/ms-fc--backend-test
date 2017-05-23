package com.scmspain.entities;

/**
 * Created by olivernoguera on 23/05/2017.
 */
public enum TypeTweet {

    PUBLISHED(0),
    DISCARDED(1);

    private int type;

    TypeTweet(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
