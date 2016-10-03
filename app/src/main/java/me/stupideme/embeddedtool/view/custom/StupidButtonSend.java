package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidButtonSend extends Button implements StupidButtonDialog.StupidButtonDialogListener {

    private MainPresenter mPresenter;
    private DataType mDataType;
    private StupidButtonDialog mDialog;
    private DataType[] mButtonTypes = {DataType.BUTTON_0, DataType.BUTTON_1, DataType.BUTTON_2,
            DataType.BUTTON_3, DataType.BUTTON_4};

    public StupidButtonSend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public StupidButtonSend(final Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
        mDialog = new StupidButtonDialog(context, this);

        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(200);
        setHeight(100);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(
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

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.sendDataOverButton(toString());
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BUTTON_CLICKED + getId());
                intent.putExtra("type",Constants.BUTTON_TYPE_SEND);
                context.sendBroadcast(intent);
            }
        });
    }

    public DataType getViewType() {
        return mDataType;
    }

    @Override
    public String toString() {
        return "I am Stupid Send Button";
    }

    @Override
    public void setViewType(int type) {
        mDataType = mButtonTypes[type];
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        mPresenter.removeButton(this);
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
        setBackgroundColor(getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]));

    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v("StupidSendButton ", "dialog canceled");
    }

}
