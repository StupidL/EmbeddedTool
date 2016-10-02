package me.stupideme.embeddedtool.view.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.ViewType;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/10/1.
 */

public class StupidEditText extends EditText implements StupidEditTextDialog.StupidEditTextDialogListener {

    private MainPresenter mPresenter;
    private StupidEditTextDialog mDialog;
    private View bindView;
    private MyReceiver receiver;

    private ViewType mViewType;
    private ViewType[] mButtonTypes = {ViewType.BUTTON_0, ViewType.BUTTON_1, ViewType.BUTTON_2,
            ViewType.BUTTON_3, ViewType.BUTTON_4};

    public StupidEditText(final Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
        mDialog = new StupidEditTextDialog(context, this);

        setMaxLines(10);
        setLines(10);
        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(800);
        setHeight(300);
        setTextSize(18);
        setPadding(16, 16, 16, 16);
        setGravity(Gravity.NO_GRAVITY);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showEditTextWidth(getWidth());
                mDialog.showEditTextHeight(getHeight());
                mDialog.show();
                return true;
            }
        });

    }

    public StupidEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public String toString() {
        return "I am Stupid Edit Text";
    }

    @Override
    public void setViewType(int i) {
        mViewType = mButtonTypes[i];
    }

    @Override
    public void onDelete() {
        if (receiver != null) {
            getContext().unregisterReceiver(receiver);
            receiver = null;
        }
        mDialog.dismiss();
        mPresenter.removeEditText(this);
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void onSave(Map<String, String> map) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey("width")) {
            params.width = Integer.parseInt(map.get("width"));
            setLayoutParams(params);
        }
        if (map.containsKey("height")) {
            params.height = Integer.parseInt(map.get("height"));
            setLayoutParams(params);
        }
        mDialog.dismiss();
    }

    @Override
    public void setBgColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public void bindViewById(int id) {
        bindView = mPresenter.bindViewById(id);
        Log.v("StupidEditText ", "bind view success and ready to show data");
        if (receiver != null) {
            getContext().unregisterReceiver(receiver);
            receiver = null;
        }
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter(Constants.ACTION_BUTTON_CLICKED + bindView.getId());
        getContext().registerReceiver(receiver, filter);
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", 0);
            Log.v("button type ", type + "");
            if (type == Constants.BUTTON_TYPE_SEND)
                if (bindView != null) {
                    append("\n" + bindView.toString());
                    mPresenter.sendDataOverEditText(StupidEditText.this.toString());
                    Log.v("receiver ", "data has been send");
                    append("\n" + StupidEditText.this.getText().toString());
                }
            Log.v("Receiver ", "receiver run");
        }
    }
}
