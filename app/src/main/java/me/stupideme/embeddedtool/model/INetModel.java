package me.stupideme.embeddedtool.model;

/**
 * Created by StupidL on 2016/9/30.
 */

public interface INetModel {
    void sendDataOverButton(String s);

    String receiveDataOverButton();

    void sendDataOverTextView(String s);

    String receiveDataOverTextView();

    void sendDataOverEditText(String s);

    String receiveDataOverEditText();
}
