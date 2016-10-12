package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidButtonSend extends StupidButton implements StupidButtonDialog.StupidButtonDialogListener {

    private StupidButtonDialog mDialog;
    private int mBackgroundColor = getResources().getColor(R.color.Gray);

    public StupidButtonSend(Context context) {
        super(context);
        mDialog = new StupidButtonDialog(context, this);

        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(200);
        setHeight(100);
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showButtonName(getText().toString());
                mDialog.showButtonWidth(getWidth());
                mDialog.showButtonHeight(getHeight());
                mDialog.showButtonId(getId());
                mDialog.show();
                return false;
            }
        });

    }

    @Override
    public String toString() {
        return "I am Stupid Send Button";
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        mBindTextView = null;
        mBindEditText = null;
        FrameLayout frameLayout = (FrameLayout) getParent();
        frameLayout.removeView(this);
        Log.v("StupidSendButton ", "button removed");
    }

    @Override
    public void onSave(Map<String, String> map) {
        mDialog.dismiss();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey("name"))
            setText(map.get("name"));
        if (map.containsKey("width")) {
            params.width = Integer.parseInt(map.get("width"));
            setLayoutParams(params);
        }
        if (map.containsKey("height")) {
            params.height = Integer.parseInt(map.get("height"));
            setLayoutParams(params);
        }
        if (map.containsKey("id")) {
            setId(Integer.parseInt(map.get("id")));
        }
        if (map.containsKey("type")) {
            setDataType(Constants.mButtonTypes[Integer.parseInt(map.get("type"))]);
        }
        int color = getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]);
        setBackgroundColor(color);

        mBackgroundColor = color;

    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v("StupidSendButton ", "dialog canceled");
    }

}
