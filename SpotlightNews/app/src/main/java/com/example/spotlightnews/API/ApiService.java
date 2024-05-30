package com.example.spotlightnews.API;

import com.example.spotlightnews.News.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("top-headlines")
    Call<NewsResponse> getData(@Query("country") String country, @Query("category") String category, @Query("apiKey") String apiKey);

}
