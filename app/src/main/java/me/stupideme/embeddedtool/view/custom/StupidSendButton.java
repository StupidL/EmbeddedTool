package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Map;

import me.stupideme.embeddedtool.ViewType;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidSendButton extends Button implements StupidButtonDialog.StupidButtonDialogListener {

    private MainPresenter mPresenter;
    private ISendLink iSendLink;
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
                mDialog.setName(getText().toString());
                mDialog.show();
                return false;
            }
        });
    }

    /**
     * bind the text view
     * @param link text view to be bind
     */
    public void bindTextView(ISendLink link){
        iSendLink = link;
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
    public void onSave(Map<String,String> map) {
        mDialog.dismiss();
        setText(map.get("name"));
        mPresenter.sendDataOverButton(toString());
        iSendLink.sendDataToTextView(toString());
        Log.v("StupidSendButton ", "data saved");
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v("StupidSendButton ", "dialog canceled");
    }

    @Override
    public void onBindTextView(int id) {
        ISendLink view = (ISendLink) mPresenter.findTextView(id);
        bindTextView(view);
        Log.v("StupidSendButton ","bind text view success");
    }

    public interface ISendLink {

        /**
         * send text to the text view which have been bind to this button
         * @param s the text send to the text view
         */
        void sendDataToTextView(String s);
    }
}
