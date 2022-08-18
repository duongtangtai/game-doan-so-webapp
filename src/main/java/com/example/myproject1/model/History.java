package com.example.myproject1.model;

import java.time.LocalDateTime;

public class History {
    private String gameSessionId;
    private int tryTimes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean completed;
    public History gameSessionId (String gameSessionId) {
        this.gameSessionId = gameSessionId;
        return this;
    }
    public History tryTimes (int tryTimes) {
        this.tryTimes = tryTimes;
        return this;
    }
    public History startTime (LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }
    public History endTime (LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }
    public History completed (boolean completed) {
        this.completed = completed;
        return this;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
