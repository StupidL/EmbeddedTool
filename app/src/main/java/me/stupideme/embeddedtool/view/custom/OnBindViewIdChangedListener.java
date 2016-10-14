package me.stupideme.embeddedtool.view.custom;

/**
 * Created by stupidl on 16-10-12.
 */

public interface OnBindViewIdChangedListener {
    /**
     * listen if a view's bind view changed or not
     * @param other id of the view to bind
     * @param self id of the view itself
     */
    void onBindViewIdChanged(int other, int self);
}
