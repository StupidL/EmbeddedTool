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
                mDialog.show();
                return false;
            }
        });
        setPadding(16, 16, 16, 16);
    }

    public void setBindviewListener(OnBindViewIdChangedListener listener){
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
        if (map.containsKey("bind_view_id")) {
            mBindViewId = Integer.parseInt(map.get("bind_view_id"));
            mBindViewListener.onBindViewIdChanged(mBindViewId, getId());
        }
        int color = getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]);
        setBackgroundColor(color);
        mBackgroundColor = color;
    }

}
