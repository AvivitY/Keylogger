package com.example.keylogger;

import android.app.Application;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MSP.initHelper(this);
    }
}
