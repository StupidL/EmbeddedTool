package me.stupideme.embeddedtool.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.db.DBManager;
import me.stupideme.embeddedtool.net.BluetoothService;
import me.stupideme.embeddedtool.presenter.MainPresenter;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public class StupidModelImpl implements IStupidModel {

    //debug
    private static final String TAG = StupidModelImpl.class.getSimpleName();

    /**
     * bluetooth service
     */
    private BluetoothService mService;

    /**
     * context
     */
    private Context mContext;

    /**
     * manage database operations
     */
    private DBManager mManager;

    private Map<String, String> mTextViewMap = new HashMap<>();
    private Map<String, String> mEditTextMap = new HashMap<>();

    private String tmpReadMessage;
    private String tmpWriteMessage;

    /**
     * handle kinds of messages from bluetooth service
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    //receive this message when the connection status changed
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            Toast.makeText(mContext, "已连接设备" + mConnectedDeviceName,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Toast.makeText(mContext, "正在连接设备......",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    //receive this message when connected thread runs method write(byte[] buffer) successfully
                    byte[] writeBuf = (byte[]) msg.obj;
                    //construct a string from the buffer
                    tmpWriteMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    //receive this message when connected thread runs method run() successfully
                    byte[] readBuf = (byte[]) msg.obj;
                    //construct a string from the valid bytes in the buffer
                    tmpReadMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    //receive this message when connected a device
                    Toast.makeText(mContext, "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    //receive this message when the connection failed or lost
                    Toast.makeText(mContext, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public StupidModelImpl(Context context) {
        mService = new BluetoothService(mHandler);
        mContext = context;
        mManager = DBManager.getInstance(context);
    }

    @Override
    public void connectDevice(String address, boolean secure) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        mService.connect(device, secure);
    }

    @Override
    public void sendDataOverButton(DataType type, String s) {
        switch (type) {
            case LED:

                break;
            case BUZZER:

                break;
        }
    }

    @Override
    public String receiveDataOverButton(DataType type) {
        return "";
    }


    @Override
    public void saveStupidSendButtonInfo(String name, StupidButtonSend view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_BUTTON_SEND);
        values.put(Constants.HAS_BIND_VIEW, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.BIND_VIEW_ID, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        mManager.insertSendButton(values);
    }

    @Override
    public void saveStupidButtonReceiveInfo(String name, StupidButtonReceive view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_BUTTON_RECEIVE);
        values.put(Constants.HAS_BIND_VIEW, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.BIND_VIEW_ID, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        mManager.insertReceiveButton(values);
    }

    @Override
    public void saveStupidTextViewInfo(String name, StupidTextView view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_TEXT_VIEW);
        values.put(Constants.HAS_BIND_VIEW, view.hasBindView());
        values.put(Constants.BIND_VIEW_ID, view.getBindViewId());
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        mManager.insertReceiveButton(values);
    }

    @Override
    public void saveStupidEditTextInfo(String name, StupidEditText view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_EDIT_TEXT);
        values.put(Constants.HAS_BIND_VIEW, view.hasBindView());
        values.put(Constants.BIND_VIEW_ID, view.getBindViewId());
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        mManager.insertReceiveButton(values);
    }

    @Override
    public void createFromTemplate(FrameLayout frameLayout, String templateName,
                                   Context context, MainPresenter presenter) {
        Cursor cursor = mManager.queryTemplate(templateName);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int type = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_TYPE)));
            switch (type) {
                case Constants.VIEW_TYPE_BUTTON_SEND:
                    frameLayout.addView(createButtonSend(cursor, context, presenter));
                    break;
                case Constants.VIEW_TYPE_BUTTON_RECEIVE:
                    frameLayout.addView(createButtonReceive(cursor, context, presenter));
                    break;
                case Constants.VIEW_TYPE_TEXT_VIEW:
                    frameLayout.addView(createTextView(cursor, context, presenter));
                    break;
                case Constants.VIEW_TYPE_EDIT_TEXT:
                    frameLayout.addView(createEditText(cursor, context, presenter));
                    break;
            }
            cursor.moveToNext();
        }
        bindViewByMap(presenter);
        cursor.close();
    }

    /**
     * bind view by id after all views are created to avoid null pointer exception
     * @param presenter
     */
    private void bindViewByMap(MainPresenter presenter) {

        for (Map.Entry entry : mEditTextMap.entrySet()) {
            int other = Integer.parseInt(entry.getKey().toString());
            int self = Integer.parseInt(entry.getValue().toString());
            Log.v(TAG, "other id: " + other + " self id : " + self);
            presenter.bindEditTextById(other, self);
        }

        for (Map.Entry entry : mTextViewMap.entrySet()) {
            int other = Integer.parseInt(entry.getKey().toString());
            int self = Integer.parseInt(entry.getValue().toString());
            Log.v(TAG, "other id: " + other + " self id : " + self);
            presenter.bindTextViewById(other, self);
        }

        mEditTextMap.clear();   //map need to clear after finishing create views
        mTextViewMap.clear();
    }

    /**
     * create a edit text to container
     *
     * @param cursor    cursor to get info
     * @param context   context to construct view
     * @param presenter presenter needed in view
     * @return the edit text
     */
    private StupidEditText createEditText(Cursor cursor, Context context, MainPresenter presenter) {
        StupidEditText view = new StupidEditText(context, presenter);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        boolean has_bind_view = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Constants.HAS_BIND_VIEW)));
        Log.d(TAG, has_bind_view + "");
        int bind_view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.BIND_VIEW_ID)));
        if (bind_view_id != -1) {
            mEditTextMap.put(bind_view_id + "", view_id + "");
        }
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        view.setId(view_id);
        view.setText(view_text);
        view.setX(view_x);
        view.setY(view_y);
        view.setBackgroundColor(color);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        view.setLayoutParams(params);
        return view;
    }

    /**
     * create a text view to container
     *
     * @param cursor    cursor to get info
     * @param context   context to construct view
     * @param presenter presenter needed in view
     * @return the text view
     */
    private StupidTextView createTextView(Cursor cursor, Context context, MainPresenter presenter) {
        StupidTextView view = new StupidTextView(context, presenter);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        boolean has_bind_view = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(Constants.HAS_BIND_VIEW)));
        Log.d(TAG, has_bind_view + "");
        int bind_view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.BIND_VIEW_ID)));
        if (bind_view_id != -1) {
            mTextViewMap.put(bind_view_id + "", view_id + "");
        }
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        view.setId(view_id);
        view.setText(view_text);
        view.setX(view_x);
        view.setY(view_y);
        view.setBackgroundColor(color);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        view.setLayoutParams(params);
        return view;
    }

    /**
     * create a receive type button to container
     *
     * @param cursor    cursor to get info
     * @param context   context to construct view
     * @param presenter presenter needed in view
     * @return the button
     */
    private StupidButtonReceive createButtonReceive(Cursor cursor, Context context, MainPresenter presenter) {
        StupidButtonReceive button = new StupidButtonReceive(context, presenter);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        button.setId(view_id);
        button.setText(view_text);
        button.setX(view_x);
        button.setY(view_y);
        button.setBackgroundColor(color);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        button.setLayoutParams(params);
        return button;
    }

    /**
     * create a send type button to container
     *
     * @param cursor    cursor to get info
     * @param context   context to construct view
     * @param presenter presenter needed in view
     * @return the button
     */
    private StupidButtonSend createButtonSend(Cursor cursor, Context context, MainPresenter presenter) {
        StupidButtonSend button = new StupidButtonSend(context, presenter);
        int view_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_ID)));
        String view_text = cursor.getString(cursor.getColumnIndex(Constants.VIEW_TEXT));
        int view_width = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_WIDTH)));
        int view_height = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_HEIGHT)));
        float view_x = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_X)));
        float view_y = Float.parseFloat(cursor.getString(cursor.getColumnIndex(Constants.VIEW_Y)));
        int color = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.VIEW_COLOR)));
        button.setId(view_id);
        button.setText(view_text);
        button.setX(view_x);
        button.setY(view_y);
        button.setBackgroundColor(color);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) button.getLayoutParams();
        params.width = view_width;
        params.height = view_height;
        button.setLayoutParams(params);
        return button;
    }

}
