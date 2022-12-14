package com.example.myproject1.model;

public class Player {
    private String username;
    private String password;
    private String name;
    public Player(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }
    public Player(){
    }
    public Player username(String username) {
        this.username = username;
        return this;
    }
    public Player password(String password) {
        this.password = password;
        return this;
    }
    public Player name(String name) {
        this.name = name;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

