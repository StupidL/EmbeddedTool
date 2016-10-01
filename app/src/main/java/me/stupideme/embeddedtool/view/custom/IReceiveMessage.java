package me.stupideme.embeddedtool.view.custom;

/**
 * Created by StupidL on 2016/10/1.
 */

public interface IReceiveMessage {
    /**
     * send text to the text view which have been bind to this button
     * @param s the text send to the text view
     */
    void receiveToTarget(String s);
}
