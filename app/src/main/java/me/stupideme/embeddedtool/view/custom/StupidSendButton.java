package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.util.Map;

import me.stupideme.embeddedtool.ViewType;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidSendButton extends Button implements StupidButtonDialog.StupidButtonDialogListener {

    private MainPresenter mPresenter;
    private ISendMessage iSendMessage;
    private ViewType mViewType;
    private StupidButtonDialog mDialog;
    private ViewType[] mButtonTypes = {ViewType.BUTTON_0, ViewType.BUTTON_1, ViewType.BUTTON_2,
            ViewType.BUTTON_3, ViewType.BUTTON_4};

    public StupidSendButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public StupidSendButton(Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
        mDialog = new StupidButtonDialog(context, this);
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showButtonName(getText().toString());
                mDialog.showButtonWidth(getWidth());
                mDialog.showButtonHeight(getHeight());
                mDialog.show();
                return false;
            }
        });
    }

    /**
     * bind the text view
     *
     * @param link text view to be bind
     */
    public void bindTextView(ISendMessage link) {
        iSendMessage = link;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public String toString() {
        return "I am Stupid Send Button";
    }

    @Override
    public void onSetViewType(int type) {
        mViewType = mButtonTypes[type];
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
        mPresenter.sendDataOverButton(toString());
        if (iSendMessage != null)
            iSendMessage.sendToTarget(toString());
        Log.v("StupidSendButton ", "data saved");
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v("StupidSendButton ", "dialog canceled");
    }

    @Override
    public void onBindTextView(int id) {
        ISendMessage view = (ISendMessage) mPresenter.findTextView(id);
        bindTextView(view);
        Log.v("StupidSendButton ", "bind text view success");
    }

}
