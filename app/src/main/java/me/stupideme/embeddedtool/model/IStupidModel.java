package me.stupideme.embeddedtool.model;

import android.database.Cursor;

import java.util.List;

import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public interface IStupidModel {

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
     * query template by name from database
     * @param templateName name of template
     * @return cursor
     */
    Cursor queryTemplate(String templateName);

    /**
     * query data type for spinner
     * @return a list contains all data types
     */
    List<String> queryDataTypesForSpinner();
}
