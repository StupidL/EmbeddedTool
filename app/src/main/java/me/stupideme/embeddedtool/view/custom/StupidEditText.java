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

    /**
     * dialog to set attrs
     */
    private StupidEditTextDialog mDialog;

    /**
     * a reference of bind view
     */
    private OnBindViewIdChangedListener mBindViewListener;

    /**
     * default background color
     */
    private int mBackgroundColor = getResources().getColor(R.color.Gray);

    /**
     * default bind view's id
     */
    private int mBindViewId = -1;

    /**
     * default color spinner position
     */
    private int mColorPos = -1;

    public StupidEditText(Context context) {
        super(context);

        //init dialog
        mDialog = new StupidEditTextDialog(context, this);

        //set default text color
        setTextColor(Color.WHITE);
        //set default background color
        setBackgroundColor(getResources().getColor(R.color.Gray));
        //set default width
        setWidth(800);
        //set default height
        setHeight(200);
        //set default text size
        setTextSize(18);
        //set default padding
        setPadding(16, 16, 16, 16);
        //set default gravity
        setGravity(Gravity.NO_GRAVITY);
        //set layout params
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        //set on long click listener
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //show width in dialog
                mDialog.showEditTextWidth(getWidth());
                //show height in dialog
                mDialog.showEditTextHeight(getHeight());
                //show id in dialog
                mDialog.showEditTextId(getId());
                //show bind view id in dialog
                mDialog.showBindViewId(mBindViewId);
                //set position of color spinner
                mDialog.showSpinnerColor(mColorPos);
                //show dialog
                mDialog.show();
                return true;
            }
        });
    }

    /**
     * set bind view changed listener
     *
     * @param listener listener
     */
    public void setBindViewListener(OnBindViewIdChangedListener listener) {
        mBindViewListener = listener;
    }

    /**
     * constructor
     *
     * @param context context
     * @param attrs   attrs
     */
    public StupidEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * if has bind a button
     *
     * @return true when mBindViewId != -1
     */
    public boolean hasBindView() {
        return mBindViewId != -1;
    }

    /**
     * get bind view id
     *
     * @return id of bind view
     */
    public int getBindViewId() {
        return mBindViewId;
    }

    /**
     * set bind view id
     *
     * @param id id of bind view
     */
    public void setBindViewId(int id) {
        mBindViewId = id;
    }

    /**
     * get background color of edit text
     *
     * @return color
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * get color spinner position
     *
     * @return position
     */
    public int getColorPos() {
        return mColorPos;
    }

    /**
     * set color spinner position
     *
     * @param i position
     */
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
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        //set width
        if (map.containsKey(Constants.KEY_WIDTH)) {
            params.width = Integer.parseInt(map.get(Constants.KEY_WIDTH));
            setLayoutParams(params);
        }
        //set height
        if (map.containsKey(Constants.KEY_HEIGHT)) {
            params.height = Integer.parseInt(map.get(Constants.KEY_HEIGHT));
            setLayoutParams(params);
        }
        //set color position
        if (map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
        //set id
        if (map.containsKey(Constants.KEY_ID)) {
            setId(Integer.parseInt(map.get(Constants.KEY_ID)));
        }
        //set bind view id
        if (map.containsKey(Constants.KEY_BIND_VIEW_ID)) {
            mBindViewId = Integer.parseInt(map.get(Constants.KEY_BIND_VIEW_ID));
            mBindViewListener.onBindViewIdChanged(mBindViewId, getId());
        }
        //dismiss dialog
        mDialog.dismiss();
    }

}
