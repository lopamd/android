package com.android.zappos.intern.apiInterface;

import com.android.zappos.intern.model.ProductDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ZapposInterface {

        @GET("Search?")
        Call<ProductDetails> getProduct(@Query("term") String name, @Query("key") String apiKey);
}
