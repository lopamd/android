package com.example.utils;

/**
 * Class that contains all the Constants required in our Video Upload
 * client App.
 */
public class Constants {
    /**
     * URL of the VideoWebService.  Please Read the Instructions in
     * README.md to set up the SERVER_URL.
     */
    public static final String SERVER_URL = "http://192.168.43.71:8080";
        //"http://10.0.0.7:8080";
    
    /**
     * Define a constant for 1 MB.
     */
    public static final long MEGA_BYTE = 1024 * 1024;

    /**
     * Maximum size of Video to be uploaded in MB.
     */
    public static final long MAX_SIZE_MEGA_BYTE = 50 * MEGA_BYTE;
}
