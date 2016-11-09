package me.stupideme.embeddedtool.view.interfaces;

import java.util.List;

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

    /**
     * show data types when activity crated
     * @param list a list contains all data types
     */
    void updateTypeSpinner(List<String> list);
}
