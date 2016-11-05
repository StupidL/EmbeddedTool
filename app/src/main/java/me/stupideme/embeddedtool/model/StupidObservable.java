package me.stupideme.embeddedtool.model;

/**
 * Created by stupidl on 16-10-14.
 * observable to manager observes
 */

public interface StupidObservable {

    /**
     * attach a observer
     * @param observer observer
     */
    void attach(StupidObserver observer);

    /**
     * detach a observer
     * @param observer observer
     */
    void detach(StupidObserver observer);

    /**
     * notify all observers
     * @param message message from bluetooth
     */
    void notifyObservers(String message);
}
