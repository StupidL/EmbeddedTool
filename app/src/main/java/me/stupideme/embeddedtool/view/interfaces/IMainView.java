package me.stupideme.embeddedtool.view.interfaces;

import android.view.View;

import java.util.List;

/**
 * Created by StupidL on 2016/9/30.
 */

public interface IMainView {

    /**
     * add a send button to a view group which becomes it's parent
     */
    void addSendButton();

    /**
     * add a receive button to a view group which becomes it's parent
     */
    void addReceiveButton();

    /**
     * add a text view to a view group which becomes to it's parent.
     */
    void addTextView();

    /**
     * add a edit text to a view group which becomes it's parent
     */
    void addEditText();

    /**
     * find view by it's id
     * @param id the view's id
     * @return the view found.
     */
    View getViewById(int id);

    /**
     * clear all views in container
     */
    void clearViews();

    /**
     * refresh data type spinner adapter for button
     * @param list
     */
    void updateTypeSpinnerAdapter(List<String> list);
}
