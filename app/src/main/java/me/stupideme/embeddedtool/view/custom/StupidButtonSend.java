package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidButtonSend extends Button implements StupidButtonDialog.StupidButtonDialogListener {

    //debug
    private static final java.lang.String TAG = StupidButtonSend.class.getSimpleName();

    /**
     * dialog to set attrs
     */
    private StupidButtonDialog mDialog;

    /**
     * a reference to a edit text
     */
    private StupidEditText mBindView;

    private StupidTextView mBindTextView;

    /**
     * data type to operate
     */
    private String mDataType;

    /**
     * position of the type array so we can set the correct position when recreated view from template
     */
    private int mTypePos = -1;

    /**
     * background color
     */
    private int mBackgroundColor = getResources().getColor(R.color.Gray);

    /**
     * color of the color array so we can set the correct position when recreated view from template
     */
    private int mColorPos = -1;

    private OnSendMessageListener mSendMessageListener;

    public StupidButtonSend(final Context context) {
        super(context);

        mDialog = new StupidButtonDialog(context, this);

        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(200);
        setHeight(100);
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showButtonName(getText().toString());
                mDialog.showButtonWidth(getWidth());
                mDialog.showButtonHeight(getHeight());
                mDialog.showButtonId(getId());
                mDialog.showSpinnerColor(mColorPos);
                mDialog.showSpinnerType(mTypePos);
                mDialog.show();
                return false;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDataType() != null) {
                    if (getBindView() != null) {
                        mSendMessageListener.onSendMessage(Constants.REQUEST_CODE_SEND,
                                getDataType(), getBindView().getText().toString());
                        if (getBindTextView() != null)
                            getBindTextView().append("\n" + getBindView().getText().toString());
                        getBindView().setText(null);

                    } else {
                        Toast.makeText(context, "该按钮需要绑定一个编辑框～", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请先设置要操作的类型", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setSendMessageListener(OnSendMessageListener listener) {
        mSendMessageListener = listener;
    }

    public void updateSpinnerAdapter(List<java.lang.String> list) {
        mDialog.updateTypeSpinnerAdapter(list);
        Log.v(TAG, java.lang.String.valueOf(list.size()));
    }

    /**
     * set color position in spinner
     *
     * @param i position
     */
    public void setColorPos(int i) {
        mColorPos = i;
    }

    /**
     * get color position in spinner
     *
     * @return position
     */
    public int getColorPos() {
        return mColorPos;
    }

    /**
     * set data type position in spinner
     *
     * @param pos position
     */
    public void setTypePos(int pos) {
        mTypePos = pos;
    }

    /**
     * get data type position in spinner
     *
     * @return data type position
     */
    public int getTypePos() {
        return mTypePos;
    }

    /**
     * get data type
     *
     * @return data type
     */
    public String getDataType() {
        return mDataType;
    }

    /**
     * set data type
     *
     * @param type data type
     */
    public void setDataType(String type) {
        this.mDataType = type;
        Log.v(TAG, "String:" + type);
    }

    /**
     * get bind view
     *
     * @return the bind view
     */
    public StupidEditText getBindView() {
        return mBindView;
    }

    /**
     * set bind view
     *
     * @param mBindView the view to bind
     */
    public void setBindView(StupidEditText mBindView) {
        this.mBindView = mBindView;
    }

    /**
     * get background color
     *
     * @return background color
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBindTextView(StupidTextView view) {
        mBindTextView = view;
    }

    public StupidTextView getBindTextView() {
        return mBindTextView;
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        mBindView = null;
        mSendMessageListener = null;
        FrameLayout frameLayout = (FrameLayout) getParent();
        frameLayout.removeView(this);
        Log.v("StupidSendButton ", "button removed");
    }

    @Override
    public void onSave(Map<java.lang.String, java.lang.String> map) {
        mDialog.dismiss();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey(Constants.KEY_NAME))
            setText(map.get(Constants.KEY_NAME));
        if (map.containsKey(Constants.KEY_WIDTH)) {
            params.width = Integer.parseInt(map.get(Constants.KEY_WIDTH));
            setLayoutParams(params);
        }
        if (map.containsKey(Constants.KEY_HEIGHT)) {
            params.height = Integer.parseInt(map.get(Constants.KEY_HEIGHT));
            setLayoutParams(params);
        }
        if (map.containsKey(Constants.KEY_ID)) {
            setId(Integer.parseInt(map.get(Constants.KEY_ID)));
        }
        if (map.containsKey(Constants.KEY_TYPE_POS)) {
            mTypePos = Integer.parseInt(map.get(Constants.KEY_TYPE_POS));
        }
        if(map.containsKey(Constants.KEY_TYPE_STRING)){
            setDataType(map.get(Constants.KEY_TYPE_STRING));
        }
        if (map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v(TAG, "dialog canceled");
    }

}
