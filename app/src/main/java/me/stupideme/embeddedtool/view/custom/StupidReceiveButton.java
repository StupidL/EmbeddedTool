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
 * Created by StupidL on 2016/9/30.
 */

public class StupidReceiveButton extends Button implements StupidButtonDialog.StupidButtonDialogListener {

    private MainPresenter mPresenter;
    private IReceiveMessage iReceiveMessage;
    private ViewType mViewType;
    private StupidButtonDialog mDialog;
    private ViewType[] mButtonTypes = {ViewType.BUTTON_0, ViewType.BUTTON_1, ViewType.BUTTON_2,
            ViewType.BUTTON_3, ViewType.BUTTON_4};

    private String data;

    public StupidReceiveButton(Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
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

    public StupidReceiveButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bindTextView(IReceiveMessage link) {
        iReceiveMessage = link;
    }

    public ViewType getViewType() {
        return mViewType;
    }

    @Override
    public void onSetViewType(int type) {
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

        data = mPresenter.receiveDataOverButton();  // receive data over wifi or bluetooth and save
        if (iReceiveMessage != null)
            iReceiveMessage.receiveToTarget(toString());// send the data to the text view
        Log.v("StupidReceiveButton ", "data saved");
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void onBindTextView(int id) {
        IReceiveMessage view = (IReceiveMessage) mPresenter.findTextView(id);
        bindTextView(view);
        Log.v("StupidReceiveButton ", "bind text view success");
    }

    @Override
    public String toString() {
        return "Iam Stupid Receive Button";
    }

}
