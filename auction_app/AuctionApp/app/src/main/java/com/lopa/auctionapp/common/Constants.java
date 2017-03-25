package com.lopa.auctionapp.common;

public class Constants {

    //Erros:
    public static int DB_INSERT_ERROR = -1;
    public static int DEFAULT_ERROR = -10;

    //All user type constants
    public static String UTYPE = "USERTYPE";
    public static String EMAIL = "USEREMAIL";
    public static int UTYPE_INVALID = 0;
    public static int UTYPE_SELLER = 1;
    public static int UTYPE_BUYER = 2;
    public static int UTYPE_BOTH = 3;

    public static String TOAST_ISUBMITTED= "Products are submitted successfully";
    public static String TOAST_IUPDATED = "Products are updated successfully";
    public static String TOAST_SUBMIT_NOPRODUCT = "No product to submit";
    public static String TOAST_UPDATE_NOPRODUCT = "No product to update";
    public static String TOAST_SELLER_BUYER = "Now you are both seller and buyer";
    public static String LOG_OUT_POPUP_TITLE = "Do you want to Logout?";
    public static String LOG_OUT_POPUP_BODY ="Are you sure you want to Logout?";
    public static String LOGIN_BLANK = "Username or Password cannot be blank.";
    public static String LOGIN_INVALID_MSG = "Invalid Username or Password.";
    public static String ENTER_PRODUCT_HINT = " Enter your product name ";
    public static String FIELD_BLANK_MSG = "Field can not be blank";
    public static String REG_FIELD_BLANK ="Information field should not be blank.";
    public static String REG_RADIO_BLANK = "Check the radio Button for Seller/Buyer/Both.";
    public static String REG_REGISTERED_EMAIL_MSG = "Email ID already registered.";
    public static String REG_SUCCESS_MSG = "Your Account is Successfully Created.";
    public static String REG_INVALID_MOBILE = "Not a valid mobile number";
    public static String REG_INVALID_EMAIL = "Not a valid email";
    public static String REG_ERROR_DB = "User registration failed. Try again";

    public static String POPUP_OK = "OK";
    public static String POPUP_INFO="Information";
    public static String POPUP_ALERT="Alert";

}
