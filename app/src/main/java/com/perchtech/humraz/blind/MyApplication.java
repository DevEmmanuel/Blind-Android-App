package com.perchtech.humraz.blind;

import android.app.Application;
import android.content.res.Configuration;

import net.gotev.speech.Speech;


public class MyApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Speech.init(this);
    }



}