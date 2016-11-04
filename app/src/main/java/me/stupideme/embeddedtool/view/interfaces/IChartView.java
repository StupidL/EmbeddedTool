package me.stupideme.embeddedtool.view.interfaces;

import me.stupideme.embeddedtool.model.StupidObserver;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;

/**
 * Created by stupidl on 16-10-16.
 */

public interface IChartView extends StupidObserver{

    /**
     * listener for chart view
     * @param listener chart view
     */
    void setOnSendMessageListener(OnSendMessageListener listener);
}
