package com.example.myproject1.model;

import java.time.LocalDateTime;
import java.util.Random;

public class GameSession {
    private Random random = null;
    private int startId;
    private String id;
    private int targetNumber;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean completed;
    private boolean active;
    private String username; // username
    public GameSession(String username, int startId) {
        this.startId = startId;
        this.id = "GAME" + String.format("%05d", this.startId);
        this.targetNumber = getRandomInt();
        this.startTime = LocalDateTime.now();
        this.endTime = LocalDateTime.now();
        this.username = username;
        this.completed=false;
        this.active = true; //create a game means the player wants to play a new game
    }
    public GameSession() {
    }
    public GameSession startId(int startId) {
        this.startId = startId;
        return this;
    }
    public GameSession id(String id) {
        this.id = id;
        return this;
    }
    public GameSession targetNumber(int targetNumber) {
        this.targetNumber=targetNumber;
        return this;
    }
    public GameSession startTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }
    public GameSession endTime(LocalDateTime endTime) {
        this.endTime = endTime;
        return this;
    }
    public GameSession completed(boolean completed) {
        this.completed = completed;
        return this;
    }
    public GameSession active(boolean active) {
        this.active = active;
        return this;
    }
    public GameSession username(String username) {
        this.username = username;
        return this;
    }
    private int getRandomInt() {
        if (random == null)
            random = new Random();

        return random.nextInt(1000) + 1;
    }

    public int getStartId() {
        return startId;
    }

    public void setStartId(int startId) {
        this.startId = startId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTargetNumber() {
        return targetNumber;
    }

    public void setTargetNumber(int targetNumber) {
        this.targetNumber = targetNumber;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
