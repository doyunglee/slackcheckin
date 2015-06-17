package com.example.doyunglee.slackcheckin;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by doyunglee on 6/17/15.
 */
public interface SlackService {

    @POST("/services/T026B13VA/B064U29MZ/vwexYIFT51dMaB5nrejM6MjK")
    void postSlackMessage(@Body SlackMessage slackMessage, Callback<Void> callback);

}
