package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidTextView extends TextView implements StupidTextViewDialog.StupidTextViewListener {

    private StupidTextViewDialog mDialog;
    private OnBindViewIdChangedListener mBindViewListener;
    private int mBackgroundColor = getResources().getColor(R.color.Gray);
    private int mBindViewId = -1;
    private int mColorPos = -1;

    public StupidTextView(Context context) {
        super(context);

        setClickable(true);
        mDialog = new StupidTextViewDialog(context, this);

        setMaxLines(10);
        setTextColor(Color.WHITE);
        setWidth(1400);
        setHeight(250);
        setTextSize(18);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showTextViewId(getId());
                mDialog.showTextViewWidth(getWidth());
                mDialog.showTextViewHeight(getHeight());
                mDialog.showBindViewId(mBindViewId);
                mDialog.showSpinnerColor(mColorPos);
                mDialog.show();
                return false;
            }
        });
        setPadding(16, 16, 16, 16);
    }

    public void setBindViewListener(OnBindViewIdChangedListener listener) {
        mBindViewListener = listener;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public boolean hasBindView() {
        return mBindViewId != -1;
    }

    public int getBindViewId() {
        return mBindViewId;
    }

    public void setBindViewId(int id){
        mBindViewId = id;
    }

    public int getColorPos() {
        return mColorPos;
    }

    public void setColorPos(int i) {
        mColorPos = i;
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        ((FrameLayout) getParent()).removeView(this);
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void onSave(Map<String, String> map) {
        mDialog.dismiss();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey(Constants.KEY_ID)) {
            setId(Integer.parseInt(map.get(Constants.KEY_ID)));
        }
        if (map.containsKey(Constants.KEY_WIDTH)) {
            params.width = Integer.parseInt(map.get(Constants.KEY_WIDTH));
            setLayoutParams(params);
        }
        if (map.containsKey(Constants.KEY_HEIGHT)) {
            params.height = Integer.parseInt(map.get(Constants.KEY_HEIGHT));
            setLayoutParams(params);
        }
        if (map.containsKey(Constants.KEY_BIND_VIEW_ID)) {
            mBindViewId = Integer.parseInt(map.get(Constants.KEY_BIND_VIEW_ID));
            if (mBindViewId != -1) {
                mBindViewListener.onBindViewIdChanged(mBindViewId, getId());
            }
        }
        if(map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
    }

}
