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
    private int mColorPos = -1;

    public StupidEditText(Context context) {
        super(context);

        mDialog = new StupidEditTextDialog(context, this);

        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(800);
        setHeight(200);
        setTextSize(18);
        setPadding(16, 16, 16, 16);
        setGravity(Gravity.NO_GRAVITY);
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener( new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showEditTextWidth(getWidth());
                mDialog.showEditTextHeight(getHeight());
                mDialog.showEditTextId(getId());
                mDialog.showBindViewId(mBindViewId);
                mDialog.showSpinnerColor(mColorPos);
                mDialog.show();
                return true;
            }
        });
    }

    public void setBindViewListener(OnBindViewIdChangedListener listener) {
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

    public void setBindViewId(int id){
        mBindViewId = id;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getColorPos(){
        return mColorPos;
    }

    public void setColorPos(int i){
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
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey(Constants.KEY_WIDTH)) {
            params.width = Integer.parseInt(map.get(Constants.KEY_WIDTH));
            setLayoutParams(params);
        }
        if (map.containsKey(Constants.KEY_HEIGHT)) {
            params.height = Integer.parseInt(map.get(Constants.KEY_HEIGHT));
            setLayoutParams(params);
        }
        if (map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
        if (map.containsKey(Constants.KEY_ID))
            setId(Integer.parseInt(map.get(Constants.KEY_ID)));
        if (map.containsKey(Constants.KEY_BIND_VIEW_ID)) {
            mBindViewId = Integer.parseInt(map.get(Constants.KEY_BIND_VIEW_ID));
            mBindViewListener.onBindViewIdChanged(mBindViewId, getId());
        }
        mDialog.dismiss();
    }

}
