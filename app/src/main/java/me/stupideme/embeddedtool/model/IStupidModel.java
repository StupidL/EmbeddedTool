package me.stupideme.embeddedtool.model;

import android.content.Context;
import android.widget.FrameLayout;

import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.presenter.MainPresenter;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public interface IStupidModel {

    /**
     * send data over send type button
     *
     * @param type data type
     * @param s    content
     */
    void sendDataOverButton(DataType type, String s);

    /**
     * receive data over receive type button
     *
     * @param type data type
     * @return content
     */
    String receiveDataOverButton(DataType type);

    /**
     * connect to device by address in secure or insecure way
     *
     * @param address address of bluetooth
     * @param secure  secure or not
     */
    void connectDevice(String address, boolean secure);

    /**
     * save send type button information
     *
     * @param name template name
     * @param view button
     */
    void saveStupidSendButtonInfo(String name, StupidButtonSend view);

    /**
     * save receive type button information
     *
     * @param name template name
     * @param view button
     */
    void saveStupidButtonReceiveInfo(String name, StupidButtonReceive view);

    /**
     * save text view information
     *
     * @param name template name
     * @param view button
     */
    void saveStupidTextViewInfo(String name, StupidTextView view);

    /**
     * save edit text information
     *
     * @param name template name
     * @param view button
     */
    void saveStupidEditTextInfo(String name, StupidEditText view);

    /**
     * create view from database by template name
     *
     * @param frameLayout  container
     * @param templateName template name
     */
    void createFromTemplate(FrameLayout frameLayout, String templateName,
                            Context context, MainPresenter presenter);
}
