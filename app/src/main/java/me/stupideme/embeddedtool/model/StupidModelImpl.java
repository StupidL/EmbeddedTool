package me.stupideme.embeddedtool.model;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.Util;
import me.stupideme.embeddedtool.bluetooth.BluetoothService;
import me.stupideme.embeddedtool.db.DBManager;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;

/**
 * Created by StupidL on 2016/9/30.
 */

public class StupidModelImpl implements IStupidModel, OnSendMessageListener, StupidObservable {

    //debug
    private static final java.lang.String TAG = StupidModelImpl.class.getSimpleName();

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


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Thread mThread = new Thread(mRunnable);

    /**
     * private constructor
     */
    private StupidModelImpl() {
        mManager = DBManager.getInstance();
        mObservers = new ArrayList<>();
    }

    /**
     * get instance
     *
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
     * @param handler handler from MainActivity
     */
    public void setHandler(Handler handler) {
        mHandler = handler;
        mService = BluetoothService.getInstance(handler);
    }

    /**
     * send message to embedded device through bluetooth
     *
     * @param requestCode a code to diff request type : send button, receive button or chart
     * @param type        data type
     * @param body        content
     */
    @Override
    public void onSendMessage(String requestCode, String type, String body) {
        MessageBean bean = new MessageBean();
        bean.setRequestCode(requestCode);
        String code = mManager.queryTypeCodeByName(type);
        Log.v("code", code);
        Log.v(TAG, "body: " + body);
        bean.setDataType(code.toUpperCase());

        switch (code) {
            case "aa":
                int num = Integer.parseInt(body.substring(0, 1));
                switch (num) {
                    case 0:
                        bean.setBody("3F");
                        break;
                    case 1:
                        bean.setBody("06");
                        break;
                    case 2:
                        bean.setBody("5B");
                        break;
                    case 3:
                        bean.setBody("4F");
                        break;
                    case 4:
                        bean.setBody("66");
                        break;
                    case 5:
                        bean.setBody("6D");
                        break;
                    case 6:
                        bean.setBody("7D");
                        break;
                    case 7:
                        bean.setBody("07");
                        break;
                    case 8:
                        bean.setBody("7F");
                        break;
                    case 9:
                        bean.setBody("67");
                        break;
                    default:
                        bean.setBody(body);
                        break;
                }

                break;

            case "ab":
                int num2 = Integer.parseInt(body.substring(0, 1));
                switch (num2) {
                    case 1:
                        bean.setBody("01");
                        break;
                    case 2:
                        bean.setBody("02");
                        break;
                    case 3:
                        bean.setBody("04");
                        break;
                    case 4:
                        bean.setBody("08");
                        break;
                    case 5:
                        bean.setBody("10");
                        break;
                    case 6:
                        bean.setBody("20");
                        break;
                    case 7:
                        bean.setBody("40");
                        break;
                    case 8:
                        bean.setBody("80");
                        break;
                    case 9:
                        bean.setBody("63");
                        break;
                    default:
                        bean.setBody(body);
                        break;
                }
                break;
        }

//        bean.setBody(body);
        Cursor cursor = mManager.queryDataProtocol();
        cursor.moveToLast();
        String header = cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_HEADER));
        String tail = cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_TAIL));
        bean.setHeader(header);
        Log.v(TAG, "header: " + header);
        Log.v(TAG, "tail: " + tail);
        bean.setTail(tail);
        String msg = bean.toString();

        Log.v(TAG, "write string: " + msg);
//        Log.v(TAG, "write string: " + Arrays.toString(msg.getBytes()));

        final byte[] re = Util.hexStringToByte(msg);
        Log.v(TAG, "hexStringToBytes:" + Arrays.toString(re));


        for (byte aTest : re) {
            mService.write(new byte[]{aTest});
        }

        Log.v(TAG, "send message success");

    }

    /**
     * query a template by name
     *
     * @param templateName name of template
     * @return a cursor
     */
    @Override
    public Cursor queryTemplate(java.lang.String templateName) {
        return mManager.queryTemplate(templateName);
    }

    /**
     * query all data types for spinner
     *
     * @return a list contains all data types
     */
    @Override
    public List<java.lang.String> queryDataTypesForSpinner() {
        List<java.lang.String> list = new ArrayList<>();
        Cursor cursor = mManager.queryDataType();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_NAME)));
            cursor.moveToNext();
        }
        cursor.close();
        for (java.lang.String s : list)
            System.out.println(s);
        return list;
    }

    /**
     * stop bluetooth service
     */
    @Override
    public void stopBluetoothService() {
        mService.stop();
    }

    /**
     * an observer attach subject
     *
     * @param observer observer
     */
    @Override
    public void attach(StupidObserver observer) {
        mObservers.add(observer);
    }

    /**
     * an observer detach subject
     *
     * @param observer observer
     */
    @Override
    public void detach(StupidObserver observer) {
        mObservers.remove(observer);
    }

    /**
     * notify all observers
     *
     * @param message message to be sent to observers
     */
    @Override
    public void notifyObservers(String message) {
        Log.v(TAG, "notify observers: " + message);
        for (StupidObserver o : mObservers) {
            o.receiveMessage(message);
        }
    }

    /**
     * connect a device
     *
     * @param address address of bluetooth
     * @param secure  secure or not
     */
    @Override
    public void connectDevice(String address, boolean secure) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        mService.connect(device, secure);
    }

    /**
     * save view's info to database
     *
     * @param values content values
     */
    @Override
    public void saveViewInfo(ContentValues values) {
        mManager.insertView(values);
    }

}
