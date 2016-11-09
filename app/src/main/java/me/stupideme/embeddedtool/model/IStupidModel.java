package me.stupideme.embeddedtool.model;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

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
     * save a view's info
     * @param values content values
     */
    void saveViewInfo(ContentValues values);

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

    /**
     * stop bluetooth service when MainActivity destroyed
     */
    void stopBluetoothService();

}
