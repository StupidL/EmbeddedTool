package me.stupideme.embeddedtool.view.custom;

import me.stupideme.embeddedtool.DataType;

/**
 * Created by stupidl on 16-10-14.
 */

public interface OnSendMessageListener {
    void onSendMessage(int requestCode, DataType type, String msg);
}
