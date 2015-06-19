package com.example.doyunglee.slackcheckin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NotificationReceiver extends BroadcastReceiver {
    static final String LOG_TAG = NotificationReceiver.class.getSimpleName();
    static final String SLACK_MSG = "I'm Here!";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "NOTIFICATION RECEIVED!");
        //Send string to Slack
        String userInput = SLACK_MSG;
        SlackMessage slackMessage = new SlackMessage(userInput);
        ServiceManager.getSlackServiceInstance().postSlackMessage(BuildConfig.SLACK_CHANNEL_URL_KEY,
                slackMessage, new Callback<Object>() {
            @Override
            public void success(Object object, Response response) {
                Log.i(LOG_TAG, "Sending to Slack Success!");
            }

            @Override
            public void failure(RetrofitError error) {
                error.getCause();
                Log.i(LOG_TAG, "Sending to Slack Fail!");
                Log.i(LOG_TAG, error.getCause().toString());
            }
        });

    }
}
