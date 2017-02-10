package com.example.model.mediator.webdata;

import java.util.Objects;

/**
 * This "Plain Ol' Java Object" (POJO) class represents meta-data of
 * interest downloaded in Json from the Video Service via the
 * VideoServiceProxy.
 */
public class Video {
    /**
     * Various fields corresponding to data downloaded in Json from
     * the Video WebService.
     */
    private long id;
    private String title;
    private long duration;
    private String contentType;
    private String dataUrl;
    private float starRatingSum ;
    private float starRatingCount;
	
    /**
     * No-op constructor
     */
    public Video() {
    }
    
    /**
     * Constructor that initializes title, duration, and contentType.
     */
    public Video(String title,
                 long duration,
                 String contentType) {
        this.title = title;
        this.duration = duration;
        this.contentType = contentType;
    }

    /**
     * Constructor that initializes all the fields of interest.
     */
    public Video(long id,
                 String title,
                 long duration,
                 String contentType,
                 String dataUrl,
                 float starRatingSum,
                 float starRatingCount) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.contentType = contentType;
        this.dataUrl = dataUrl;
        this.starRatingSum = starRatingSum;
        this.starRatingCount = starRatingCount;
    }

    /*
     * Getters and setters to access Video.
     */

    /**
     * Get the Id of the Video.
     * 
     * @return id of video
     */
    public long getId() {
        return id;
    }

    /**
     * Get the Video by Id
     * 
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the Title of Video.
     * 
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the Title of Video.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the Duration of Video.
     * 
     * @return Duration of Video.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Set the Duration of Video.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Get the DataUrl of Video
     * 
     * @return dataUrl of Video
     */
    public String getDataUrl() {
        return dataUrl;
    }

    /**
     * Set the DataUrl of the Video.
     */
    public void setDataUrl(String dataUrl) {
        this.dataUrl = dataUrl;
    }

    /**
     * Get ContentType of Video.
     * 
     * @return contentType of Video.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Set the ContentType of Video.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    /**
     * Get the  starRatingSum of the Video.
     * 
     * @return starRatingSum of video
     */
    public float getStarRatingSum() {
        return starRatingSum;
    }

    /**
     * Get the Video by starRatingSum
     * 
     * @param starRatingSum
     */
    public void setStarRatingSum(float starRatingSum) {
        this.starRatingSum = starRatingSum;
    }

    
    /**
     * Get the  starRatingCount of the Video.
     * 
     * @return starRatingCount of video
     */
    public float getStarRatingCount() {
        return starRatingCount;
    }

    /**
     * Get the Video by starRatingCount
     * 
     * @param starRatingCount
     */
    public void setStarRatingCount(float starRatingCount) {
        this.starRatingCount = starRatingCount;
    }
	
    /**
     * @return the textual representation of Video object.
     */
    @Override
    public String toString() {
        return "{" +
            "Id: "+ id + ", "+
            "Title: "+ title + ", "+
            "Duration: "+ duration + ", "+
            "ContentType: "+ contentType + ", "+
            "Data URL: "+ dataUrl + " , "+
            "Star Rating Sum: "+ starRatingSum +" , "+
            "Star Rating Count: "+ starRatingCount +
            "}";
    }

    /** 
     * @return an Integer hash code for this object. 
     */
    @Override
    public int hashCode() {
        return Objects.hash(getTitle(),
                            getDuration());
    }

    /**
     * @return Compares this Video instance with specified 
     *         Video and indicates if they are equal.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Video)
            && Objects.equals(getTitle(),
                              ((Video) obj).getTitle())
            && getDuration() == ((Video) obj).getDuration();
    }
}
