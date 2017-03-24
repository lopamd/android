package com.lopa.auctionapp.model;

public class User {
    private int userId;
    private String userName;
    private String emailId;
    private String password;
    private String mobile;
    private int userType;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public User(){}
    public User(String userName, String emailId, String password, String mobile, int userType){
        this.userName = userName;
        this.emailId = emailId;
        this.password = password;
        this.mobile = mobile;
        this.userType = userType;
    }

}
