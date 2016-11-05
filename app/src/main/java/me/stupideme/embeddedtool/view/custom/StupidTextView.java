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

    /**
     * dialog
     */
    private StupidTextViewDialog mDialog;

    /**
     * on bind view listener
     */
    private OnBindViewIdChangedListener mBindViewListener;

    /**
     * default background color
     */
    private int mBackgroundColor = getResources().getColor(R.color.Gray);

    /**
     * default bind view id
     */
    private int mBindViewId = -1;

    /**
     * default position of color spinner
     */
    private int mColorPos = -1;

    /**
     * constructor
     * @param context context
     */
    public StupidTextView(Context context) {
        super(context);

        setClickable(true);
        //init dialog
        mDialog = new StupidTextViewDialog(context, this);
        //set max lines
        setMaxLines(10);
        //set text color
        setTextColor(Color.WHITE);
        //set default width
        setWidth(1400);
        //set default height
        setHeight(250);
        //set default text size
        setTextSize(18);
        //set default background color
        setBackgroundColor(getResources().getColor(R.color.Gray));
        //set padding
        setPadding(16, 16, 16, 16);
        //set layout params
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        //set long click listener
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //show id in dialog
                mDialog.showTextViewId(getId());
                //show width in dialog
                mDialog.showTextViewWidth(getWidth());
                //show height in dialog
                mDialog.showTextViewHeight(getHeight());
                //show bind view id in dialog
                mDialog.showBindViewId(mBindViewId);
                //set position of color spinner
                mDialog.showSpinnerColor(mColorPos);
                //show dialog
                mDialog.show();
                return false;
            }
        });

    }

    /**
     * set bind view listener
     * @param listener listener
     */
    public void setBindViewListener(OnBindViewIdChangedListener listener) {
        mBindViewListener = listener;
    }

    /**
     * get background color
     * @return color
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * if has bind view
     * @return true if mBindViewId != -1
     */
    public boolean hasBindView() {
        return mBindViewId != -1;
    }

    /**
     * get bind view id
     * @return id
     */
    public int getBindViewId() {
        return mBindViewId;
    }

    /**
     * set bind view id
     * @param id id
     */
    public void setBindViewId(int id){
        mBindViewId = id;
    }

    /**
     * get color position in spinner
     * @return position
     */
    public int getColorPos() {
        return mColorPos;
    }

    /**
     * set color position in spinner
     * @param i position
     */
    public void setColorPos(int i) {
        mColorPos = i;
    }

    /**
     * delete text view itself
     */
    @Override
    public void onDelete() {
        mDialog.dismiss();
        ((FrameLayout) getParent()).removeView(this);
    }

    /**
     * dismiss dialog
     */
    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    /**
     * save attrs
     * @param map a map contains attrs
     */
    @Override
    public void onSave(Map<String, String> map) {
        //dismiss dialog first
        mDialog.dismiss();
        //get layout params
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        //set id
        if (map.containsKey(Constants.KEY_ID)) {
            setId(Integer.parseInt(map.get(Constants.KEY_ID)));
        }
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
        //set bind view id
        if (map.containsKey(Constants.KEY_BIND_VIEW_ID)) {
            mBindViewId = Integer.parseInt(map.get(Constants.KEY_BIND_VIEW_ID));
            if (mBindViewId != -1) {
                mBindViewListener.onBindViewIdChanged(mBindViewId, getId());
            }
        }
        //set color position
        if(map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
    }

}
