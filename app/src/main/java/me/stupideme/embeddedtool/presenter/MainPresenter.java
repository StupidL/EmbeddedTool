package me.stupideme.embeddedtool.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.model.IStupidModel;
import me.stupideme.embeddedtool.model.StupidModelImpl;
import me.stupideme.embeddedtool.view.IMainView;
import me.stupideme.embeddedtool.view.custom.StupidButton;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public class MainPresenter {

    //debug
    private static final String TAG = MainPresenter.class.getSimpleName();
    private IStupidModel iStupidModel;
    private IMainView iMainView;

    /**
     * constructor
     *
     * @param view    MainActivity
     * @param context context passed to IStupidModel
     */
    public MainPresenter(IMainView view, Context context) {
        iMainView = view;
        iStupidModel = new StupidModelImpl(context);
    }

    /**
     * connect device by bluetooth address in the secure or insecure way
     *
     * @param address bluetooth address
     * @param secure  secure or not
     */
    public void connectDevice(String address, boolean secure) {
        iStupidModel.connectDevice(address, secure);
        Log.i(TAG, "connect device(" + address + " " + secure);
    }

    /**
     * add a send type button
     */
    public void addSendButton() {
        iMainView.addSendButton();
        Log.i(TAG, "add send button");
    }

    /**
     * add a receive type button
     */
    public void addReceiveButton() {
        iMainView.addReceiveButton();
        Log.i(TAG, "add receive button");
    }

    /**
     * remove a button(send type or receive type)
     *
     * @param view button to be removed
     */
    public void removeButton(Button view) {
        iMainView.removeButton(view);
        Log.i(TAG, "remove button");
    }

    /**
     * add a text view
     */
    public void addTextView() {
        iMainView.addTextView();
        Log.i(TAG, "add text view");
    }

    /**
     * remove a text view
     *
     * @param view text view to be removed
     */
    public void removeTextView(TextView view) {
        iMainView.removeTextView(view);
        Log.i(TAG, "remove text view");
    }

    /**
     * add a edit text
     */
    public void addEditText() {
        iMainView.addEditText();
        Log.i(TAG, "add edit text");
    }

    /**
     * remove a edit text
     *
     * @param view edit text to be removed
     */
    public void removeEditText(StupidEditText view) {
        iMainView.removeEditText(view);
        Log.i(TAG, "remove edit text");
    }

    /**
     * send data by a send type button
     *
     * @param type data's type
     * @param s    string data to be send
     */
    public void sendDataOverButton(DataType type, String s) {
        iStupidModel.sendDataOverButton(type, s);
        Log.i(TAG, "send data over button " + "(" + type + ", " + s + ")");
    }

    /**
     * receive data over a receive type button
     *
     * @param type data's type
     * @return string data that received
     */
    public String receiveDataOverButton(DataType type) {
        Log.i(TAG, "receive data over button " + "(" + type + ")");
        return iStupidModel.receiveDataOverButton(type);
    }

    /**
     * button bind a text view by id
     *
     * @param other button's id
     * @param self  text view's id
     * @return 1 means both send type and receive type button is valid
     */
    public int bindTextViewById(int other, int self) {
        Log.i(TAG, "bind text view by id " + "(other: " + other + ", self: " + self + ")");
        StupidButton button = (StupidButton) iMainView.getViewById(other);
        button.setBindTextView((StupidTextView) iMainView.getViewById(self));
        return 1;
    }

    /**
     * button bind a edit text by id
     *
     * @param other button's id
     * @param self  edit text's id
     * @return 1 means send type button bind button successfully,
     * 0 means edit text is not a StupidButtonType and invalid
     */
    public int bindEditTextById(int other, int self) {
        Log.i(TAG, "bind edit text by id " + "(other: " + other + ", self: " + self + ")");
        if (iMainView.getViewById(other) instanceof StupidButtonSend) {
            StupidButton button = (StupidButton) iMainView.getViewById(other);
            button.setBindEditText((StupidEditText) iMainView.getViewById(self));
            return 1;
        }
        return 0;
    }

    /**
     * save current layout as a template
     *
     * @param layout container that contains kinds of views
     */
    public void saveTemplate(FrameLayout layout, String templateName) {
        int number = layout.getChildCount();
        while (number > 0) {
            View view = layout.getChildAt(number - 1);
            if (view instanceof StupidButtonSend) {
                iStupidModel.saveStupidSendButtonInfo(templateName, (StupidButtonSend) view);
            } else if (view instanceof StupidButtonReceive) {
                iStupidModel.saveStupidButtonReceiveInfo(templateName, (StupidButtonReceive) view);
            } else if (view instanceof StupidTextView) {
                iStupidModel.saveStupidTextViewInfo(templateName, (StupidTextView) view);
            } else if (view instanceof StupidEditText) {
                iStupidModel.saveStupidEditTextInfo(templateName, (StupidEditText) view);
            }
            number--;
        }
        Log.i(TAG, "saved template");
    }

    /**
     * load template from database or file and create them in FrameLayout
     *
     * @param layout       container
     * @param templateName template name saved in database
     */
    public void loadTemplate(FrameLayout layout, String templateName, Context context, MainPresenter presenter) {
        Log.i(TAG, "load template \"" + templateName + "\"");
        iMainView.clearViews();
        iStupidModel.createFromTemplate(layout, templateName, context, presenter);
    }
}
