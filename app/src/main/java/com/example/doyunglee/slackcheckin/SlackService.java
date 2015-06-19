package com.example.doyunglee.slackcheckin;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SlackService {

    @POST("/services/{slack_channel_url_key}")
    void postSlackMessage(@Path(value = "slack_channel_url_key", encode = false) String urlKey,
                          @Body SlackMessage slackMessage, Callback<Object> callback);


}
