package me.stupideme.embeddedtool.view.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidTextView extends TextView implements StupidTextViewDialog.StupidTextViewListener {

    private StupidTextViewDialog mDialog;
    private MainPresenter mPresenter;
    private View mBindView;
    public MyReceiver receiver;

    public StupidTextView(Context context, MainPresenter presenter) {
        super(context);
        setClickable(true);
        mPresenter = presenter;
        mDialog = new StupidTextViewDialog(context, this);

        setMaxLines(10);
        setTextColor(Color.WHITE);
        setWidth(1400);
        setHeight(250);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showTextViewId(getId());
                mDialog.showTextViewWidth(getWidth());
                mDialog.showTextViewHeight(getHeight());
                mDialog.show();
                return false;
            }
        });
        setPadding(16, 16, 16, 16);
    }

    public StupidTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDelete() {
        if (receiver != null) {
            getContext().unregisterReceiver(receiver);
            receiver = null;
        }
        mDialog.dismiss();
        mPresenter.removeTextView(this);
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void onSave(Map<String, String> map) {
        mDialog.dismiss();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey("id")) {
            setId(Integer.parseInt(map.get("id")));
        }
        if (map.containsKey("width")) {
            params.width = Integer.parseInt(map.get("width"));
            setLayoutParams(params);
        }
        if (map.containsKey("height")) {
            params.height = Integer.parseInt(map.get("height"));
            setLayoutParams(params);
        }
        setBackgroundColor(getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]));
        mPresenter.sendDataOverTextView(toString());
    }

    @Override
    public void bindViewById(int id) {
        mBindView = mPresenter.bindViewById(id);
        Log.v("StupidTextView ", "bind view success and ready to show data");
        if (receiver != null) {
            getContext().unregisterReceiver(receiver);
            receiver = null;
        }
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(Constants.ACTION_BUTTON_CLICKED + mBindView.getId());
        getContext().registerReceiver(receiver, filter);
    }

    @Override
    public String toString() {
        return "I am Stupid Text View.";
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mBindView != null)
                append("\n" + mBindView.toString());
            Log.v("Receiver ", "receiver run");
        }
    }
}
