package com.github.jiawei.intelligent_parking_system;

import android.app.Application;
import android.content.Context;

public class MuApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // MultiDex.install(base);
    }
}
