package me.stupideme.embeddedtool.presenter;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.model.IStupidModel;
import me.stupideme.embeddedtool.model.StupidModelImpl;
import me.stupideme.embeddedtool.view.IMainView;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public class MainPresenter {

    //debug
    private static final String TAG = MainPresenter.class.getSimpleName();
    private IStupidModel iStupidModel;
    private IMainView iMainView;


    private Map<String, String> mTextViewMap = new HashMap<>();
    private Map<String, String> mEditTextMap = new HashMap<>();

    /**
     * constructor
     *
     * @param view    MainActivity
     * @param context context passed to IStupidModel
     */
    public MainPresenter(IMainView view, Context context) {
        iMainView = view;
        iStupidModel = new StupidModelImpl(context);
    }

    /**
     * connect device by bluetooth address in the secure or insecure way
     *
     * @param address bluetooth address
     * @param secure  secure or not
     */
    public void connectDevice(String address, boolean secure) {
        iStupidModel.connectDevice(address, secure);
        Log.i(TAG, "connect device(" + address + " " + secure);
    }

    /**
     * add a send type button
     */
    public void addSendButton() {
        iMainView.addSendButton();
        Log.i(TAG, "add send button");
    }

    /**
     * add a receive type button
     */
    public void addReceiveButton() {
        iMainView.addReceiveButton();
        Log.i(TAG, "add receive button");
    }

    /**
     * add a text view
     */
    public void addTextView() {
        iMainView.addTextView();
        Log.i(TAG, "add text view");
    }

    /**
     * add a edit text
     */
    public void addEditText() {
        iMainView.addEditText();
        Log.i(TAG, "add edit text");
    }

    /**
     * remove all the views of container
     */
    public void removeAllViews() {
        iMainView.clearViews();
    }

    /**
     * send data by a send type button
     *
     * @param type data's type
     * @param s    string data to be send
     */
    public void sendDataOverButton(DataType type, String s) {
        iStupidModel.sendDataOverButton(type, s);
        Log.i(TAG, "send data over button " + "(" + type + ", " + s + ")");
    }

    /**
     * receive data over a receive type button
     *
     * @param type data's type
     * @return string data that received
     */
    public String receiveDataOverButton(DataType type) {
        Log.i(TAG, "receive data over button " + "(" + type + ")");
        return iStupidModel.receiveDataOverButton(type);
    }

    /**
     * button bind a text view by id
     *
     * @param other button's id
     * @param self  text view's id
     * @return 1 means both send type and receive type button is valid
     */
    public int bindTextViewById(int other, int self) {
        Log.i(TAG, "bind text view by id " + "(other: " + other + ", self: " + self + ")");
        StupidButtonReceive button = (StupidButtonReceive) iMainView.getViewById(other);
        button.setBindView((StupidTextView) iMainView.getViewById(self));
        return 1;
    }

    /**
     * button bind a edit text by id
     *
     * @param other button's id
     * @param self  edit text's id
     * @return 1 means send type button bind button successfully,
     * 0 means edit text is not a StupidButtonType and invalid
     */
    public int bindEditTextById(int other, int self) {
        Log.i(TAG, "bind edit text by id " + "(other: " + other + ", self: " + self + ")");
        if (iMainView.getViewById(other) instanceof StupidButtonSend) {
            StupidButtonSend button = (StupidButtonSend) iMainView.getViewById(other);
            button.setBindView((StupidEditText) iMainView.getViewById(self));
            return 1;
        }
        Log.v(TAG, "StupidEditText only can bind StupidButtonSend");
        return 0;
    }

    /**
     * save current layout as a template
     *
     * @param layout container that contains kinds of views
     */
    public void saveTemplate(FrameLayout layout, String templateName) {
        int number = layout.getChildCount();
        while (number > 0) {
            View view = layout.getChildAt(number - 1);
            if (view instanceof StupidButtonSend) {
                iStupidModel.saveStupidSendButtonInfo(templateName, (StupidButtonSend) view);
            } else if (view instanceof StupidButtonReceive) {
                iStupidModel.saveStupidButtonReceiveInfo(templateName, (StupidButtonReceive) view);
            } else if (view instanceof StupidTextView) {
                iStupidModel.saveStupidTextViewInfo(templateName, (StupidTextView) view);
            } else if (view instanceof StupidEditText) {
                iStupidModel.saveStupidEditTextInfo(templateName, (StupidEditText) view);
            }
            number--;
        }
        Log.i(TAG, "saved template");
    }

    /**
     * load template from database or file and create them in FrameLayout
     *
     * @param layout       container
     * @param templateName template name saved in database
     */
    public void loadTemplate(FrameLayout layout, String templateName, Context context) {
        Log.i(TAG, "load template \"" + templateName + "\"");
        iMainView.clearViews();
        createFromTemplate(layout, templateName, context);
    }


    private void createFromTemplate(FrameLayout frameLayout, String templateName, Context context) {
        Cursor cursor = iStupidModel.queryTemplate(templateName);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_TYPE)));
            switch (type) {
                case Constants.VIEW_TYPE_BUTTON_SEND:
                    frameLayout.addView(createButtonSend(cursor, context));
                    break;
                case Constants.VIEW_TYPE_BUTTON_RECEIVE:
                    frameLayout.addView(createButtonReceive(cursor, context));
                    break;
                case Constants.VIEW_TYPE_TEXT_VIEW:
                    frameLayout.addView(createTextView(cursor, context));
                    break;
                case Constants.VIEW_TYPE_EDIT_TEXT:
                    frameLayout.addView(createEditText(cursor, context));
                    break;
            }
            cursor.moveToNext();
        }
        bindViewByMap();
        cursor.close();
    }

    /**
     * bind view by id after all views are created to avoid null pointer exception
     */
    private void bindViewByMap() {

        for (Map.Entry entry : mEditTextMap.entrySet()) {
            int other = Integer.parseInt(entry.getKey().toString());
            int self = Integer.parseInt(entry.getValue().toString());
            Log.v(TAG, "other id: " + other + " self id : " + self);
            bindEditTextById(other, self);
        }

        for (Map.Entry entry : mTextViewMap.entrySet()) {
            int other = Integer.parseInt(entry.getKey().toString());
            int self = Integer.parseInt(entry.getValue().toString());
            Log.v(TAG, "other id: " + other + " self id : " + self);
            bindTextViewById(other, self);
        }

        mEditTextMap.clear();   //map need to clear after finishing create views
        mTextViewMap.clear();
    }

    /**
     * create a edit text to container
     *
     * @param cursor  cursor to get info
     * @param context context to construct view
     * @return the edit text
     */
    private StupidEditText createEditText(Cursor cursor, Context context) {
        StupidEditText view = new StupidEditText(context);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        boolean has_bind_view = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Constants.HAS_BIND_VIEW)));
        Log.d(TAG, has_bind_view + "");
        int bind_view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.BIND_VIEW_ID)));
        if (bind_view_id != -1) {
            mEditTextMap.put(bind_view_id + "", view_id + "");
            view.setBindViewId(bind_view_id);
        }
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        int colorPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.SPINNER_COLOR_POS)));
        view.setId(view_id);
        view.setText(view_text);
        view.setX(view_x);
        view.setY(view_y);
        view.setBackgroundColor(color);
        view.setColorPos(colorPos);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        view.setLayoutParams(params);
        return view;
    }

    /**
     * create a text view to container
     *
     * @param cursor  cursor to get info
     * @param context context to construct view
     * @return the text view
     */
    private StupidTextView createTextView(Cursor cursor, Context context) {
        StupidTextView view = new StupidTextView(context);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        boolean has_bind_view = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Constants.HAS_BIND_VIEW)));
        Log.d(TAG, has_bind_view + "");
        int bind_view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.BIND_VIEW_ID)));
        if (bind_view_id != -1) {
            mTextViewMap.put(bind_view_id + "", view_id + "");
            view.setBindViewId(bind_view_id);
        }
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        int colorPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.SPINNER_COLOR_POS)));
        view.setId(view_id);
        view.setText(view_text);
        view.setX(view_x);
        view.setY(view_y);
        view.setBackgroundColor(color);
        view.setColorPos(colorPos);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        view.setLayoutParams(params);
        return view;
    }

    /**
     * create a receive type button to container
     *
     * @param cursor  cursor to get info
     * @param context context to construct view
     * @return the button
     */
    private StupidButtonReceive createButtonReceive(Cursor cursor, Context context) {
        StupidButtonReceive button = new StupidButtonReceive(context);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        int colorPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.SPINNER_COLOR_POS)));
        int typePos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_TYPE_POS)));
        button.setId(view_id);
        button.setText(view_text);
        button.setX(view_x);
        button.setY(view_y);
        button.setBackgroundColor(color);
        button.setColorPos(colorPos);
        button.setTypePos(typePos);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        button.setLayoutParams(params);
        return button;
    }

    /**
     * create a send type button to container
     *
     * @param cursor  cursor to get info
     * @param context context to construct view
     * @return the button
     */
    private StupidButtonSend createButtonSend(Cursor cursor, Context context) {
        StupidButtonSend button = new StupidButtonSend(context);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        int colorPos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.SPINNER_COLOR_POS)));
        int typePos = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_TYPE_POS)));
        button.setId(view_id);
        button.setText(view_text);
        button.setX(view_x);
        button.setY(view_y);
        button.setBackgroundColor(color);
        button.setColorPos(colorPos);
        button.setTypePos(typePos);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        button.setLayoutParams(params);
        return button;
    }
}
