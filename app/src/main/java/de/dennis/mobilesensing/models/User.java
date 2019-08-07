package de.dennis.mobilesensing.models;

import java.util.Date;

class User {
    String nickname;
    String email;
    String password;
    Date DoB;
    char gender;
//    String profileUrl;

    public User(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}