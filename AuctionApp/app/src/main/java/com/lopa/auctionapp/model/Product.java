package com.lopa.auctionapp.model;

public class Product {
    private int _id;
    private String productName;
    private int userType;
    private String emailId;

    public int getProductId() {
        return _id;
    }

    public void setProductId(int _id) {
        this._id = _id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName){
        this.productName = productName;
    }

    public Product(){}
    public Product(String productName,int userType,String emailId){
        this._id = _id;
        this.productName = productName;
        this.userType = userType;
        this.emailId = emailId;
    }


}
