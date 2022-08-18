package com.example.myproject1.model;

public class Rank {
    private String username;
    private String gameSessionId;
    private int tryTimes;
    private int totalSeconds;
    public Rank username(String username) {
        this.username = username;
        return this;
    }
    public Rank gameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
        return this;
    }
    public Rank tryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
        return this;
    }
    public Rank totalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public int getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(int tryTimes) {
        this.tryTimes = tryTimes;
    }

    public int getTotalSeconds() {
        return totalSeconds;
    }

    public void setTotalSeconds(int totalSeconds) {
        this.totalSeconds = totalSeconds;
    }
}
