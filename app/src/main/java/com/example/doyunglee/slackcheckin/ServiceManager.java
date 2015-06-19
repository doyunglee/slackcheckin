package com.example.doyunglee.slackcheckin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class ServiceManager {

    public final static String SLACK_ENDPOINT = "https://hooks.slack.com";
    private static final Gson GSON = new GsonBuilder().create();

    private static RestAdapter serviceAdapter = new RestAdapter
            .Builder()
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setConverter(new GsonConverter(GSON))
            .setEndpoint(SLACK_ENDPOINT).build();

    private static SlackService slackServiceInstance;

    public static SlackService getSlackServiceInstance() {

        if (slackServiceInstance == null) {
            slackServiceInstance = serviceAdapter.create(SlackService.class);
        }
        return slackServiceInstance;
    }
}
