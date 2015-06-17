package com.example.doyunglee.slackcheckin;

/**
 * Created by doyunglee on 6/17/15.
 */

//This is the POJO that we use to mimic the JSON.
public class SlackMessage {
    private String text;

    public SlackMessage(String text) {
        this.text = text;
    }
}
