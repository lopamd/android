package com.android.zappos.intern.model;

import com.android.zappos.intern.common.Constants;
import com.google.gson.annotations.SerializedName;
import android.databinding.BaseObservable;
import android.databinding.Bindable;


public class Product extends BaseObservable{
    @SerializedName(Constants.BRAND_NAME)
    String brandName;

    @SerializedName(Constants.THUMB_URL)
    String thumbnailImageUrl;

    @SerializedName(Constants.PRODUCT_ID)
    String productId;

    @SerializedName(Constants.ORIG_PRICE)
    String originalPrice;

    @SerializedName(Constants.STYLE_ID)
    String styleId;

    @SerializedName(Constants.COLOR_ID)
    String colorId;

    @SerializedName(Constants.PRICE)
    String price;

    @SerializedName(Constants.PERCENT_OFF)
    String percentOff;

    @SerializedName(Constants.PRODUCT_URL)
    String productUrl;

    @SerializedName(Constants.PRODUCT_NAME)
    String productName;

    @Bindable
    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Bindable
    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }


    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    @Bindable
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Bindable
    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    @Bindable
    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    @Bindable
    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Bindable
    public String getPercentOff() {
        return percentOff;
    }

    public void setPercentOff(String percentOff) {
        this.percentOff = percentOff;
    }

    @Bindable
    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    @Bindable
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
