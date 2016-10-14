package me.stupideme.embeddedtool.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import me.stupideme.embeddedtool.App;
import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.db.DBManager;
import me.stupideme.embeddedtool.net.BluetoothService;
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
//        mManager = DBManager.getInstance(context);
        mManager = App.manager;
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
        values.put(Constants.VIEW_TYPE_POS,view.getTypePos());
        values.put(Constants.HAS_BIND_VIEW, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.BIND_VIEW_ID, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        values.put(Constants.SPINNER_COLOR_POS,view.getColorPos());
        mManager.insertSendButton(values);
    }

    @Override
    public void saveStupidButtonReceiveInfo(String name, StupidButtonReceive view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_BUTTON_RECEIVE);
        values.put(Constants.VIEW_TYPE_POS,view.getTypePos());
        values.put(Constants.HAS_BIND_VIEW, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.BIND_VIEW_ID, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        values.put(Constants.SPINNER_COLOR_POS,view.getColorPos());
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
        values.put(Constants.SPINNER_COLOR_POS,view.getColorPos());
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
        values.put(Constants.SPINNER_COLOR_POS,view.getColorPos());
        mManager.insertReceiveButton(values);
    }

    @Override
    public Cursor queryTemplate(String templateName) {
        return mManager.queryTemplate(templateName);
    }

}
