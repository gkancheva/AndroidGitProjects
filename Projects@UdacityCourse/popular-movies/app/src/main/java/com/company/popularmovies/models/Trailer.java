package com.company.popularmovies.models;

public class Trailer {
    private String mYoutubeKey;
    private String mYoutubePath;
    private String mName;

    public Trailer(String youtubeKey, String youtubePath, String name) {
        this.mYoutubeKey = youtubeKey;
        this.mYoutubePath = youtubePath;
        this.mName = name;
    }

    public String getYoutubeKey() {
        return this.mYoutubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.mYoutubeKey = youtubeKey;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getYoutubePath() {
        return mYoutubePath;
    }

    public void setYoutubePath(String youtubePath) {
        this.mYoutubePath = youtubePath;
    }
}