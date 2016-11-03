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
        bean.setDataType(code.toLowerCase());
        bean.setBody(body);
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
        Log.v(TAG, "write string: " + Arrays.toString(msg.getBytes()));

        byte[] re = hexStringToByte(msg);
        Log.v(TAG, "hexStringToBytes:" + Arrays.toString(re));


        byte[] test = {-128};
        mService.write(test);

        Log.v(TAG, "send message success");

    }
//
//    public static final String bytesToHexString(byte[] bArray) {
//        StringBuffer sb = new StringBuffer(bArray.length);
//        String sTemp;
//        for (int i = 0; i < bArray.length; i++) {
//            sTemp = Integer.toHexString(0xFF & bArray[i]);
//            if (sTemp.length() < 2)
//                sb.append(0);
//            sb.append(sTemp.toUpperCase());
//        }
//        return sb.toString();
//    }

    private static byte[] hexStringToByte(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
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

    @Override
    public void saveViewInfo(ContentValues values) {
        mManager.insertView(values);
    }

}
