package me.stupideme.embeddedtool;

import android.app.Application;

import me.stupideme.embeddedtool.db.DBManager;

/**
 * Created by stupidl on 16-10-14.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.init(getApplicationContext());
    }
}
