package com.zhouyou.readerghost.base;

import android.app.Application;

/**
 * 作者：ZhouYou
 * 日期：2017/1/6.
 */
public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
