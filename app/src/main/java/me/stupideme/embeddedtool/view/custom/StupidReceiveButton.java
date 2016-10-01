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
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.ViewType;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/30.
 */

public class StupidReceiveButton extends Button implements StupidButtonDialog.StupidButtonDialogListener {

    private MainPresenter mPresenter;
    private ViewType mViewType;
    private StupidButtonDialog mDialog;
    private ViewType[] mButtonTypes = {ViewType.BUTTON_0, ViewType.BUTTON_1, ViewType.BUTTON_2,
            ViewType.BUTTON_3, ViewType.BUTTON_4};

    private String data;

    public StupidReceiveButton(final Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
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
                data = mPresenter.receiveDataOverButton();  // receive data over wifi or bluetooth and save
                Intent intent = new Intent();
                intent.setAction(Constants.ACTION_BUTTON_CLICKED + getId());
                context.sendBroadcast(intent);
            }
        });
    }

    public StupidReceiveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public void setViewType(int type) {
        mViewType = mButtonTypes[type];
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        mPresenter.removeButton(this);
        Log.v("StupidReceiveButton ", "button removed");
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
        if (map.containsKey("id"))
            setId(Integer.parseInt(map.get("id")));

    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void setBgColor(int color) {
        setBackgroundColor(color);
    }

    @Override
    public String toString() {
        return "Iam Stupid Receive Button";
    }

}
