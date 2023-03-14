package com.example.bachelorarbeit.payload.request;

import jakarta.validation.constraints.Email;

public class ChangeUserRequest {
    private long userId;
    private String userName;
    @Email
    private String email;


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
