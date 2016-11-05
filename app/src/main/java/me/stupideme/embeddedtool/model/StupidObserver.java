package me.stupideme.embeddedtool.model;

/**
 * Created by stupidl on 16-10-14.
 * observe messages from bluetooth
 */
public interface StupidObserver {

    /**
     * receive message from bluetooth
     * @param msg message received from bluetooth
     */
    void receiveMessage(String msg);
}
