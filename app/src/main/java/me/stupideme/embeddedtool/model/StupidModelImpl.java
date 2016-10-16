package me.stupideme.embeddedtool.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import me.stupideme.embeddedtool.App;
import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.db.DBManager;
import me.stupideme.embeddedtool.net.BluetoothService;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by StupidL on 2016/9/30.
 */

public class StupidModelImpl implements IStupidModel, OnSendMessageListener, StupidObservable {

    //debug
    private static final String TAG = StupidModelImpl.class.getSimpleName();

    /**
     * bluetooth service
     */
    private BluetoothService mService;

    /**
     * manage database operations
     */
    private DBManager mManager;

    /**
     * list of observers
     */
    private List<StupidObserver> mObservers;

    /**
     * handle kinds of messages from bluetooth service
     */
    private Handler mHandler;

    /**
     * instance of StupidModelImpl
     */
    private static StupidModelImpl INSTANCE;

    /**
     * private constructor
     */
    private StupidModelImpl() {
        mManager = App.manager;
        mObservers = new ArrayList<>();
    }

    /**
     * get instance
     * @return instance
     */
    public static StupidModelImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (StupidModelImpl.class) {
                if (INSTANCE == null)
                    INSTANCE = new StupidModelImpl();
            }
        }
        return INSTANCE;
    }

    /**
     * set handler for bluetooth service's need
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        mHandler = handler;
        mService = new BluetoothService(mHandler);
    }

    @Override
    public void onSendMessage(int requestCode, DataType type, String body) {
        MessageBean bean = new MessageBean();
        bean.setRequestCode(requestCode);
        bean.setDataType(type);
        bean.setBody(body);
        String msg = bean.toString();
        byte[] buff = msg.getBytes();
        mService.write(buff);
        //just for a test
        for (int i = 0; i < 3; i++) {
            Message message = new Message();
            message.what = Constants.MESSAGE_READ;
            message.obj = ("hello from bt").getBytes();
            message.arg1 = 13;
            mHandler.sendMessage(message);
        }
    }

    @Override
    public Cursor queryTemplate(String templateName) {
        return mManager.queryTemplate(templateName);
    }

    @Override
    public void attach(StupidObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void detach(StupidObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (StupidObserver o : mObservers) {
            o.receiveMessage(message);
        }
    }

    @Override
    public void connectDevice(String address, boolean secure) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        mService.connect(device, secure);
    }

    @Override
    public void saveStupidSendButtonInfo(String name, StupidButtonSend view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_BUTTON_SEND);
        values.put(Constants.VIEW_TYPE_POS, view.getTypePos());
        values.put(Constants.HAS_BIND_VIEW, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.BIND_VIEW_ID, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        values.put(Constants.SPINNER_COLOR_POS, view.getColorPos());
        mManager.insertSendButton(values);
    }

    @Override
    public void saveStupidButtonReceiveInfo(String name, StupidButtonReceive view) {
        ContentValues values = new ContentValues();
        values.put(Constants.TEMPLATE_NAME, name);
        values.put(Constants.VIEW_ID, view.getId());
        values.put(Constants.VIEW_TYPE, Constants.VIEW_TYPE_BUTTON_RECEIVE);
        values.put(Constants.VIEW_TYPE_POS, view.getTypePos());
        values.put(Constants.HAS_BIND_VIEW, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.BIND_VIEW_ID, Constants.HAS_BIND_VIEW_INVALID);
        values.put(Constants.VIEW_TEXT, view.getText().toString());
        values.put(Constants.VIEW_WIDTH, view.getWidth());
        values.put(Constants.VIEW_HEIGHT, view.getHeight());
        values.put(Constants.VIEW_X, view.getX());
        values.put(Constants.VIEW_Y, view.getY());
        values.put(Constants.VIEW_COLOR, view.getBackgroundColor());
        values.put(Constants.SPINNER_COLOR_POS, view.getColorPos());
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
        values.put(Constants.SPINNER_COLOR_POS, view.getColorPos());
        mManager.insertTextView(values);
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
        values.put(Constants.SPINNER_COLOR_POS, view.getColorPos());
        mManager.insertEditText(values);
    }

}
