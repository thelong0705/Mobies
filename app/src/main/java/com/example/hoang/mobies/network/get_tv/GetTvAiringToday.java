package com.example.hoang.mobies.network.get_tv;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Inpriron on 6/19/2017.
 */

public interface GetTvAiringToday {
    @GET("tv/airing_today")
    Call<MainTvObject> getTvAiringToday(@Query("api_key") String api_key, @Query("language") String language
            , @Query("page") int page);
}
