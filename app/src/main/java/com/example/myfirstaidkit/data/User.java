package com.example.myfirstaidkit.data;

import java.util.Date;

public class User {

    private String username;
    private String email;
    private String birthday;
    private String avatar;
    private String password;
    private String confirmPassword;

    public User( String username, String email, String birthday, String avatar, String password, String confirmPassword) {

        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.avatar = avatar;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
