package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidTextView extends TextView implements StupidSendButton.ISendLink,StupidReceiveButton.IReceiveLink{

    public StupidTextView(Context context) {
        super(context);
        setClickable(true);
    }



    public StupidTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void sendDataToTextView(String s) {
        setText(s);
    }

    @Override
    public void receiveDataToTextView(String s) {
        setText(s);
    }
}
