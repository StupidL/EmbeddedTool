package me.stupideme.embeddedtool.view.custom;

/**
 * Created by stupidl on 16-10-14.
 * a listener to listener send message actions
 */

public interface OnSendMessageListener {

    /**
     * send a message to embedded device by bluetooth
     * @param requestCode request code
     * @param type data type
     * @param msg body of message bean
     */
    void onSendMessage(String requestCode, String type, String msg);
}
