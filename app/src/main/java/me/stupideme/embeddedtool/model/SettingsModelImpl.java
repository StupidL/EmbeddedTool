package me.stupideme.embeddedtool.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.db.DBManager;

/**
 * Created by stupidl on 16-10-19.
 * to communicate with SettingsActivity and the data base.
 * settings activity's changed can be reflect to data base
 */

public class SettingsModelImpl implements ISettingsModel {

    //debug
    private static final String TAG = SettingsModelImpl.class.getSimpleName();

    /**
     * database manager
     */
    private DBManager mManager;

    /**
     * instance of this model
     */
    private static SettingsModelImpl INSTANCE;

    /**
     * private constructor
     */
    private SettingsModelImpl() {
        //get database manager instance
        mManager = DBManager.getInstance();
    }

    /**
     * get instance of this model.
     * singleton pattern
     * @return INSTANCE
     */
    public static SettingsModelImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (SettingsModelImpl.class) {
                if (INSTANCE == null)
                    INSTANCE = new SettingsModelImpl();
            }
        }
        return INSTANCE;
    }

    /**
     * remove a custom data type from database
     * @param name name of type
     */
    @Override
    public void removeDataType(String name) {
        mManager.deleteDataType(name);
    }

    /**
     * add a custom data type to database
     * @param map a map contains name and code
     */
    @Override
    public void addDataType(Map<String, String> map) {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_DATA_NAME, map.get(Constants.KEY_DATA_NAME));
        values.put(Constants.KEY_DATA_CODE, map.get(Constants.KEY_DATA_CODE));
        mManager.insertDataType(values);
    }

    /**
     * save a custom protocol to database
     * @param map a map contains header and tail
     */
    @Override
    public void saveDataProtocol(Map<String, String> map) {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_DATA_HEADER, map.get(Constants.KEY_DATA_HEADER));
        values.put(Constants.KEY_DATA_TAIL, map.get(Constants.KEY_DATA_TAIL));
        Log.v(TAG, "tail: " + map.get(Constants.KEY_DATA_TAIL));
        mManager.insertDataProtocol(values);
    }

    /**
     * get all data types from database
     * @return a list contains data types' info
     */
    @Override
    public List<Map<String, String>> getDataType() {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = mManager.queryDataType();
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_NAME, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_NAME)));
            map.put(Constants.KEY_DATA_CODE, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_CODE)));
            list.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * get custom data protocol from data base
     * @return a list contains protocol's info
     */
    @Override
    public List<Map<String, String>> getDataProtocol() {
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = mManager.queryDataProtocol();
        cursor.moveToFirst();
        while (!cursor.isAfterLast() && cursor.getCount() > 0) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_HEADER, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_HEADER)));
            map.put(Constants.KEY_DATA_TAIL, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_TAIL)));
            list.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * get default data types from database
     * @return a list
     */
    @Override
    public List<Map<String, String>> getDataTypeDefault() {
        mManager.deleteDataTypeCustom();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = mManager.queryDataTypeDefault();
        cursor.moveToFirst();
        while (cursor.getCount() > 0 && !cursor.isAfterLast()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_NAME, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_NAME)));
            map.put(Constants.KEY_DATA_CODE, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_CODE)));
            list.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        Log.v(TAG, String.valueOf(list.size()));
        return list;
    }

    /**
     * get default protocol from database
     * @return a list
     */
    @Override
    public List<Map<String, String>> getProtocolDefault() {
        mManager.deleteDataProtocolCustom();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = mManager.queryDataProtocolDefault();
        cursor.moveToFirst();
        while (cursor.getCount() > 0 && !cursor.isAfterLast()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_HEADER, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_HEADER)));
            map.put(Constants.KEY_DATA_TAIL, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_TAIL)));
            list.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        Log.v(TAG, String.valueOf(list.size()));
        return list;
    }
}
