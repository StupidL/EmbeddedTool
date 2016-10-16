package me.stupideme.embeddedtool.view;

import me.stupideme.embeddedtool.model.StupidObserver;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;

/**
 * Created by stupidl on 16-10-16.
 */

public interface IChartView extends StupidObserver{
    void setOnSendMessageListener(OnSendMessageListener listener);
}
