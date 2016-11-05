package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.model.StupidObserver;

/**
 * Created by StupidL on 2016/9/30.
 */

public class StupidButtonReceive extends Button implements
        StupidButtonDialog.StupidButtonDialogListener, StupidObserver {

    //debug
    private static final java.lang.String TAG = StupidButtonReceive.class.getSimpleName();

    /**
     * dialog to set attrs
     */
    private StupidButtonDialog mDialog;

    /**
     * a reference of a text view
     */
    private StupidTextView mBindView;

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

    public StupidButtonReceive(final Context context) {
        super(context);

        //init dialog
        mDialog = new StupidButtonDialog(context, this);
        //set text color
        setTextColor(Color.WHITE);
        //set background color
        setBackgroundColor(getResources().getColor(R.color.Gray));
        //set width
        setWidth(200);
        //set height
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
                //set position of color spinner
                mDialog.showSpinnerColor(mColorPos);
                //set position of type spinner
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
                        //send a message before receive
                        mSendMessageListener.onSendMessage(Constants.REQUEST_CODE_RECEIVE,//request code
                                getDataType(), //data type
                                Constants.MESSAGE_BODY_EMPTY);//body
                        getBindView().append("\n" + "Waiting...");

                    } else {
                        Toast.makeText(context, "该按钮需要绑定一个文本框～", Toast.LENGTH_SHORT).show();
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
     * update spinner
     *
     * @param list a set of data types
     */
    public void updateSpinnerAdapter(List<String> list) {
        mDialog.updateTypeSpinnerAdapter(list);
    }

    /**
     * receive message
     *
     * @param msg message
     */
    @Override
    public void receiveMessage(String msg) {
        if (mBindView != null) {
            mBindView.append("\n" + msg);
        }
    }

    /**
     * getter of background color
     *
     * @return background color
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * setter of color position
     *
     * @param i position
     */
    public void setColorPos(int i) {
        mColorPos = i;
    }

    /**
     * getter of color position
     *
     * @return position
     */
    public int getColorPos() {
        return mColorPos;
    }

    /**
     * setter of type position
     *
     * @param pos position
     */
    public void setTypePos(int pos) {
        mTypePos = pos;
    }

    /**
     * getter of type position
     *
     * @return position
     */
    public int getTypePos() {
        return mTypePos;
    }

    /**
     * getter of data type
     *
     * @return data type
     */
    public String getDataType() {
        return mDataType;
    }

    /**
     * setter of data type
     *
     * @param type data type
     */
    public void setDataType(String type) {
        this.mDataType = type;
    }

    /**
     * getter of bind view
     *
     * @return the bind view
     */
    public StupidTextView getBindView() {
        return mBindView;
    }

    /**
     * setter of bind view
     *
     * @param mBindView the view to bind
     */
    public void setBindView(StupidTextView mBindView) {
        this.mBindView = mBindView;
    }

    /**
     * delete button itself
     */
    @Override
    public void onDelete() {
        mDialog.dismiss();
        mBindView = null;
        mSendMessageListener = null;
        ((FrameLayout) getParent()).removeView(this);
    }

    /**
     * save attrs in dialog
     *
     * @param map map contains attrs
     */
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

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

}
