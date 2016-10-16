package me.stupideme.embeddedtool.presenter;

import me.stupideme.embeddedtool.model.IStupidModel;
import me.stupideme.embeddedtool.model.StupidModelImpl;
import me.stupideme.embeddedtool.model.StupidObservable;
import me.stupideme.embeddedtool.model.StupidObserver;
import me.stupideme.embeddedtool.view.IChartView;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;

/**
 * Created by stupidl on 16-10-16.
 */

public class ChartPresenter {
    private IChartView iChartView;
    private IStupidModel iStupidModel;

    public ChartPresenter(IChartView view){
        iChartView = view;
        iStupidModel = StupidModelImpl.getInstance();
    }

    public void setSendMessageListener(){
        iChartView.setOnSendMessageListener((OnSendMessageListener) iStupidModel);
    }

    public void attachObserver(StupidObserver observer){
        ((StupidObservable)iStupidModel).attach(observer);
    }

    public void detachObserver(StupidObserver observer){
        ((StupidObservable)iStupidModel).detach(observer);
    }
}
