package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidTextView extends TextView implements StupidTextViewDialog.StupidTextViewListener {

    private StupidTextViewDialog mDialog;
    private MainPresenter mPresenter;

    public StupidTextView(Context context, MainPresenter presenter) {
        super(context);
        setClickable(true);
        mPresenter = presenter;
        mDialog = new StupidTextViewDialog(context, this);

        setMaxLines(10);
        setTextColor(Color.WHITE);
        setWidth(1400);
        setHeight(250);
        setTextSize(18);
        setBackgroundColor(getResources().getColor(R.color.Gray));
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
    }

    @Override
    public void bindTextViewById(int id) {
        mPresenter.bindTextViewById(id, getId());
    }

}
