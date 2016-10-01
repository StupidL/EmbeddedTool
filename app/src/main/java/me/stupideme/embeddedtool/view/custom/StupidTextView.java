package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Map;

import me.stupideme.embeddedtool.ViewType;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidTextView extends TextView implements ISendMessage, IReceiveMessage, StupidTextViewDialog.StupidTextViewListener {

    private StupidTextViewDialog mDialog;
    private MainPresenter mPresenter;
    private ViewType mViewType;

    public StupidTextView(Context context, MainPresenter presenter) {
        super(context);
        setClickable(true);
        mPresenter = presenter;
        mDialog = new StupidTextViewDialog(context, this);
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
    }

    public StupidTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public void sendToTarget(String s) {
        setText(s);
    }

    @Override
    public void receiveToTarget(String s) {
        setText(s);
    }

    @Override
    public void setViewType(ViewType type) {
        mViewType = type;
    }

    @Override
    public void onDelete() {
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
        mPresenter.sendDataOverTextView(toString());
    }

    @Override
    public String toString() {
        return "I am Stupid Text View.";
    }
}
