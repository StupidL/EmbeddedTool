package me.stupideme.embeddedtool.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.App;
import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.db.DBManager;

/**
 * Created by stupidl on 16-10-19.
 */

public class SettingsModelImpl implements ISettingsModel {

    private static final String TAG = SettingsModelImpl.class.getSimpleName();
    private DBManager mManager;
    private static SettingsModelImpl INSTANCE;

    private SettingsModelImpl() {
        mManager = App.manager;
    }

    public static SettingsModelImpl getInstance() {
        if (INSTANCE == null) {
            synchronized (SettingsModelImpl.class) {
                if (INSTANCE == null)
                    INSTANCE = new SettingsModelImpl();
            }
        }
        return INSTANCE;
    }

    @Override
    public void removeDataType(String name) {
        mManager.deleteDataType(name);
        Log.v(TAG, "delete data type " + name);
    }

    @Override
    public void addDataType(Map<String, String> map) {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_DATA_NAME, map.get(Constants.KEY_DATA_NAME));
        values.put(Constants.KEY_DATA_CODE, map.get(Constants.KEY_DATA_CODE));
        mManager.insertDataType(values);
    }

    @Override
    public void saveDataProtocol(Map<String, String> map) {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_DATA_HEADER, map.get(Constants.KEY_DATA_HEADER));
        values.put(Constants.KEY_DATA_TAIL, map.get(Constants.KEY_DATA_TAIL));
        mManager.insertDataProtocol(values);
    }


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
