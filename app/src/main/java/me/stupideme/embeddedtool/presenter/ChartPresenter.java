package me.stupideme.embeddedtool.presenter;

import me.stupideme.embeddedtool.model.IStupidModel;
import me.stupideme.embeddedtool.model.StupidModelImpl;
import me.stupideme.embeddedtool.model.StupidObservable;
import me.stupideme.embeddedtool.model.StupidObserver;
import me.stupideme.embeddedtool.view.interfaces.IChartView;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;

/**
 * Created by stupidl on 16-10-16.
 */

public class ChartPresenter {

    /**
     * chart view
     */
    private IChartView iChartView;
    /**
     * iStupidModel
     */
    private IStupidModel iStupidModel;

    /**
     * instance of ChertPresenter
     */
    private static ChartPresenter INSTANCE;

    /**
     * private constructor
     *
     * @param view IChartView
     */
    private ChartPresenter(IChartView view) {
        //init iChartView
        iChartView = view;
        //get instance of iStupidModel
        iStupidModel = StupidModelImpl.getInstance();
    }

    /**
     * get instance. singleton pattern
     *
     * @param view IChartView
     * @return instance of ChartPresenter
     */
    public static ChartPresenter getInstance(IChartView view) {
        if (INSTANCE == null) {
            synchronized (ChartPresenter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ChartPresenter(view);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * set send message listener for chart view
     */
    public void setSendMessageListener() {
        iChartView.setOnSendMessageListener((OnSendMessageListener) iStupidModel);
    }

    /**
     * attach observer
     *
     * @param observer observer
     */
    public void attachObserver(StupidObserver observer) {
        ((StupidObservable) iStupidModel).attach(observer);
    }

    /**
     * detach observer
     *
     * @param observer observer
     */
    public void detachObserver(StupidObserver observer) {
        ((StupidObservable) iStupidModel).detach(observer);
    }

    /**
     * get all data types for chart view
     */
    public void updateTypeSpinnerAdapter() {
        iChartView.updateTypeSpinner(iStupidModel.queryDataTypesForSpinner());
    }
}
