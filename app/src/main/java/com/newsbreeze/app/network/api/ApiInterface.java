package com.newsbreeze.app.network.api;

import com.newsbreeze.app.models.NewsData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    // GET request
    @GET("top-headlines")
    Call<NewsData> getNewsData(
            @Query("country") String country,
            @Query("apiKey") String apikey
    );

}
