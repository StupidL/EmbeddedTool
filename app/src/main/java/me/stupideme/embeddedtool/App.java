package me.stupideme.embeddedtool;

import android.app.Application;

import me.stupideme.embeddedtool.db.DBManager;
import me.stupideme.embeddedtool.model.StupidModelImpl;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;

/**
 * Created by stupidl on 16-10-14.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //init database
        DBManager.init(getApplicationContext());

        OnSendMessageListener listener = StupidModelImpl.getInstance();
    }
}
