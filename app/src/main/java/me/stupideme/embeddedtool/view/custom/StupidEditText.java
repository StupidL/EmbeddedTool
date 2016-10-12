package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/10/1.
 */

public class StupidEditText extends EditText implements StupidEditTextDialog.StupidEditTextDialogListener {

    private StupidEditTextDialog mDialog;
    private OnBindViewIdChangedListener mBindViewListener;
    private int mBackgroundColor = getResources().getColor(R.color.Gray);
    private int mBindViewId = -1;

    public StupidEditText(Context context) {
        super(context);

        mDialog = new StupidEditTextDialog(context, this);

        setMaxLines(10);
        setLines(1);
        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(800);
        setHeight(100);
        setTextSize(18);
        setPadding(16, 16, 16, 16);
        setGravity(Gravity.NO_GRAVITY);
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showEditTextWidth(getWidth());
                mDialog.showEditTextHeight(getHeight());
                mDialog.showEditTextId(getId());
                mDialog.show();
                return true;
            }
        });

    }

    public void setBindViewListener(OnBindViewIdChangedListener listener){
        mBindViewListener = listener;
    }

    public StupidEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean hasBindView() {
        return mBindViewId != -1;
    }

    public int getBindViewId() {
        return mBindViewId;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
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
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey("width")) {
            params.width = Integer.parseInt(map.get("width"));
            setLayoutParams(params);
        }
        if (map.containsKey("height")) {
            params.height = Integer.parseInt(map.get("height"));
            setLayoutParams(params);
        }
        if (map.containsKey("color")) {
            int color = getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
        if (map.containsKey("id"))
            setId(Integer.parseInt(map.get("id")));
        if (map.containsKey("bind_view_id")) {
            mBindViewId = Integer.parseInt(map.get("bind_view_id"));
            mBindViewListener.onBindViewIdChanged(mBindViewId, getId());
        }
        mDialog.dismiss();
    }

}
