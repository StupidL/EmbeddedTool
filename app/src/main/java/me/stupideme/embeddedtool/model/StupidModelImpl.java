package me.stupideme.embeddedtool.model;

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

        String body2 = adjustMessageBody(code, body);
        bean.setBody(body2);

        Cursor cursor = mManager.queryDataProtocol();
        cursor.moveToLast();
        String header = cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_HEADER));
        String tail = cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_TAIL));
        bean.setHeader(header);
        Log.v(TAG, "header: " + header);
        Log.v(TAG, "tail: " + tail);
        Log.v(TAG, "body: " + body);
        Log.v(TAG, "body2 = " + body2);
        bean.setTail(tail);
        String msg = bean.toString();

        Log.v(TAG, "write string: " + msg);
//        Log.v(TAG, "write string: " + Arrays.toString(msg.getBytes()));

        final byte[] re = Util.hexStringToByte(msg);
        Log.v(TAG, "hexStringToBytes:" + Arrays.toString(re));


        for (byte aTest : re) {
            mService.write(new byte[]{aTest});
        }

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

    private String adjustMessageBody(String code, String body) {
        String nBody = null;
        switch (code) {
            case "aa":
                int num = Integer.parseInt(body.substring(0, 1));
                switch (num) {
                    case 0:
                        nBody = "3F";
                        break;
                    case 1:
                        nBody = "06";
                        break;
                    case 2:
                        nBody = "5B";
                        break;
                    case 3:
                        nBody = "4F";
                        break;
                    case 4:
                        nBody = "66";
                        break;
                    case 5:
                        nBody = "6D";
                        break;
                    case 6:
                        nBody = "7D";
                        break;
                    case 7:
                        nBody = "07";
                        break;
                    case 8:
                        nBody = "7F";
                        break;
                    case 9:
                        nBody = "67";
                        break;
                    default:
                        nBody = body;
                        break;
                }
                break;

            case "ab":
                int num2 = Integer.parseInt(body.substring(0, 1));
                switch (num2) {
                    case 1:
                        nBody = "01";
                        break;
                    case 2:
                        nBody = "02";
                        break;
                    case 3:
                        nBody = "04";
                        break;
                    case 4:
                        nBody = "08";
                        break;
                    case 5:
                        nBody = "10";
                        break;
                    case 6:
                        nBody = "20";
                        break;
                    case 7:
                        nBody = "40";
                        break;
                    case 8:
                        nBody = "80";
                        break;
                    case 9:
                        nBody = "63";
                        break;
                    default:
                        nBody = body;
                        break;
                }
                break;
            default:
                nBody = body;
                break;
            case "ac":
                int num3 = Integer.parseInt(body.substring(0, 1));
                switch (num3) {
                    case 1:
                        nBody = "01";
                        break;
                    case 2:
                        nBody = "02";
                        break;
                    case 3:
                        nBody = "03";
                        break;
                    case 4:
                        nBody = "04";
                        break;
                    case 5:
                        nBody = "05";
                        break;
                    case 6:
                        nBody = "06";
                        break;
                    case 7:
                        nBody = "07";
                        break;
                    case 8:
                        nBody = "08";
                        break;
                    default:
                        nBody = body;
                        break;
                }
        }
        return nBody;
    }

}
