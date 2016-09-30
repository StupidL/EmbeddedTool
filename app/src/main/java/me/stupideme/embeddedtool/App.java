package me.stupideme.embeddedtool;

import android.app.Application;

import me.stupideme.embeddedtool.net.DataUtil;

/**
 * Created by StupidL on 2016/9/30.
 */

public class App extends Application {
    public static DataUtil mDataUtil = new DataUtil();

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        mDataUtil = null;
    }
}
