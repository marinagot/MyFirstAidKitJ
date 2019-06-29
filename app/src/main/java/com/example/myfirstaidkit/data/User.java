package com.example.myfirstaidkit.data;

import android.arch.persistence.room.Entity;

@Entity(primaryKeys = {"id"})
public class User {
    private Integer id;
    private String username;
    private String email;
    private String birthday;
    private String avatar;
    private String password;

    public User(Integer id, String username, String email, String birthday, String avatar, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.avatar = avatar;
        this.password = password;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    }
