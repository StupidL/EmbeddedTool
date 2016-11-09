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
import me.stupideme.embeddedtool.Util;

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

    /**
     * a reference of bind text view
     */
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

    /**
     * send message listener
     */
    private OnSendMessageListener mSendMessageListener;

    /**
     * constructor
     *
     * @param context context
     */
    public StupidButtonSend(final Context context) {
        super(context);

        //init a dialog
        mDialog = new StupidButtonDialog(context, this);
        //set text color
        setTextColor(Color.WHITE);
        //set default background color
        setBackgroundColor(getResources().getColor(R.color.Gray));
        //set default width
        setWidth(200);
        //set default height
        setHeight(100);
        //set layout params
        setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        //set on long click listener
        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //show name in dialog
                mDialog.showButtonName(getText().toString());
                //show width in dialog
                mDialog.showButtonWidth(getWidth());
                //show height in dialog
                mDialog.showButtonHeight(getHeight());
                //show id in dialog
                mDialog.showButtonId(getId());
                //set color spinner position
                mDialog.showSpinnerColor(mColorPos);
                //set type spinner position
                mDialog.showSpinnerType(mTypePos);
                //show dialog
                mDialog.show();
                return false;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getDataType() != null) {
                    if (getBindView() != null) {
                        //get text in edit text and adjust it to legal string
                        String text = getBindView().getText().toString();
                        //send message
//                        String message = Util.adjustInputString(getDataType(),text);
                        Log.v(TAG, "text = " + text);

                        mSendMessageListener.onSendMessage(Constants.REQUEST_CODE_SEND, getDataType(), text);
                        //update bind text view
                        if (getBindTextView() != null) {
                            getBindTextView().append("\n" + text);
                        }
                        //update bind edit text
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

    /**
     * set send message listener
     *
     * @param listener listener
     */
    public void setSendMessageListener(OnSendMessageListener listener) {
        mSendMessageListener = listener;
    }

    /**
     * update spinner contents
     *
     * @param list a set of data types
     */
    public void updateSpinnerAdapter(List<String> list) {
        mDialog.updateTypeSpinnerAdapter(list);
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

    /**
     * set bind text view
     *
     * @param view text view
     */
    public void setBindTextView(StupidTextView view) {
        mBindTextView = view;
    }

    /**
     * get bind text view
     *
     * @return text view
     */
    public StupidTextView getBindTextView() {
        return mBindTextView;
    }

    /**
     * delete button itself
     */
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
    public void onSave(Map<String, String> map) {
        //dismiss dialog
        mDialog.dismiss();
        //get layout params
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        //set name
        if (map.containsKey(Constants.KEY_NAME)) {
            setText(map.get(Constants.KEY_NAME));
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
        //set id
        if (map.containsKey(Constants.KEY_ID)) {
            setId(Integer.parseInt(map.get(Constants.KEY_ID)));
        }
        //set type position
        if (map.containsKey(Constants.KEY_TYPE_POS)) {
            mTypePos = Integer.parseInt(map.get(Constants.KEY_TYPE_POS));
        }
        //set type name
        if (map.containsKey(Constants.KEY_TYPE_STRING)) {
            setDataType(map.get(Constants.KEY_TYPE_STRING));
        }
        //set color position
        if (map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            setBackgroundColor(color);
            mBackgroundColor = color;
        }
    }

    /**
     * dismiss dialog
     */
    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v(TAG, "dialog canceled");
    }

}
