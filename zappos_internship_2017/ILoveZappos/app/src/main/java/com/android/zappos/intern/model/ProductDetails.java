package com.android.zappos.intern.model;

import com.android.zappos.intern.common.Constants;
import com.google.gson.annotations.SerializedName;
import java.util.List;
import android.databinding.BaseObservable;
import android.databinding.Bindable;


public class ProductDetails extends BaseObservable{
    @SerializedName(Constants.ORIG_TERM)
    private String originalTerm;

    @SerializedName(Constants.RESULT_COUNT)
    private int currentResultCount;

    @SerializedName(Constants.TOTAL_RESULT_COUNT)
    private int totalResultCount;

    @SerializedName(Constants.TERM)
    private String term;

    @SerializedName(Constants.RESULTS)
    private List<Product> results;

    public List<Product> getResults() {
        return results;
    }

    public void setResults(List<Product> results) {
        this.results = results;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    @Bindable
    public int getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(int totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    @Bindable
    public int getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(int currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    @Bindable
    public String getOriginalTerm() {
        return originalTerm;
    }

    public void setOriginalTerm(String originalTerm) {
        this.originalTerm = originalTerm;
    }


}
