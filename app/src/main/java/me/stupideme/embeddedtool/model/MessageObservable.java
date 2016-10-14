package me.stupideme.embeddedtool.model;

/**
 * Created by stupidl on 16-10-14.
 */

public interface MessageObservable {
    void attach(StupidObserver observer);
    void detach(StupidObserver observer);
    void notifyObservers(String message);
}
