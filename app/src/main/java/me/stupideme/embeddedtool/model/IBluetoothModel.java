package me.stupideme.embeddedtool.model;

import me.stupideme.embeddedtool.DataType;

/**
 * Created by StupidL on 2016/9/30.
 */

public interface IBluetoothModel {
    void sendDataOverButton(DataType type, String s);

    String receiveDataOverButton(DataType type);

    void connectDevice(String address, boolean secure);
}
