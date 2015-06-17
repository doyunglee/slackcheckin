package com.example.doyunglee.slackcheckin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by doyunglee on 6/17/15.
 */
public class NotificationReceiver extends BroadcastReceiver {
    static final String LOG_TAG = NotificationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LOG_TAG, "NOTIFICATION RECEIVED!");
        //Send string to Slack
        String userInput = "I'm Here!";
        SlackMessage slackMessage = new SlackMessage(userInput);
        ServiceManager.getSlackServiceInstance().postSlackMessage(slackMessage, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                Log.i(LOG_TAG, "Sending to Slack Success!");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(LOG_TAG, "Sending to Slack Fail!");
            }
        });

    }
}
