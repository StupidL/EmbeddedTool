package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.widget.Button;

import me.stupideme.embeddedtool.DataType;

/**
 * Created by StupidL on 2016/10/4.
 */

public class StupidButton extends Button {

    protected StupidTextView mBindTextView;
    protected StupidEditText mBindEditText;
    protected DataType mDataType;

    public StupidButton(Context context) {
        super(context);
    }

    public void setBindTextView(StupidTextView view) {
        mBindTextView = view;
    }

    public void setBindEditText(StupidEditText view) {
        mBindEditText = view;
    }

    public StupidTextView getBindTextView() {
        return mBindTextView;
    }

    public StupidEditText getBindEditText() {
        return mBindEditText;
    }

    protected void setDataType(DataType type) {
        mDataType = type;
    }

    public DataType getDataType() {
        return mDataType;
    }
}
