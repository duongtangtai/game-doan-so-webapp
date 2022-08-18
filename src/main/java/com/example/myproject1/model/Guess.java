package com.example.myproject1.model;

import java.time.LocalDateTime;

public class Guess {
    private String gameSessionId;
    private int guessNumber;
    private String result;
    private LocalDateTime time;
    public Guess(String gameSessionId, int guessNum, String result) {
        this.gameSessionId = gameSessionId;
        this.guessNumber = guessNum;
        this.result = result;
        this.time = LocalDateTime.now();
    }
    public Guess() {
    }
    public Guess gameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
        return this;
    }
    public Guess guessNumber(int guessNumber) {
        this.guessNumber = guessNumber;
        return this;
    }
    public Guess result(String result) {
        this.result = result;
        return this;
    }
    public Guess time(LocalDateTime time) {
        this.time = time;
        return this;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public int getGuessNumber() {
        return guessNumber;
    }

    public void setGuessNumber(int guessNumber) {
        this.guessNumber = guessNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
