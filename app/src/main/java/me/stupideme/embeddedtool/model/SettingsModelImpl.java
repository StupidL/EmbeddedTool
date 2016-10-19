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
    public List<Map<String, String>> getAllDataType() {
        List<Map<String, String>> list = new ArrayList<>();
        //load default types
        Cursor cursor1 = mManager.queryTypeDefault();
        cursor1.moveToFirst();
        while (cursor1.getCount() > 0 && !cursor1.isAfterLast()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_NAME, cursor1.getString(cursor1.getColumnIndex(Constants.KEY_DATA_NAME)));
            map.put(Constants.KEY_DATA_CODE, cursor1.getString(cursor1.getColumnIndex(Constants.KEY_DATA_CODE)));
            list.add(map);
            cursor1.moveToNext();
        }
        cursor1.close();
        //load custom types
        Cursor cursor = mManager.queryTypeCustom();
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
    public void saveDataProtocol(Map<String, String> map) {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_DATA_HEADER, map.get(Constants.KEY_DATA_HEADER));
        values.put(Constants.KEY_DATA_TAIL, map.get(Constants.KEY_DATA_TAIL));
        mManager.insertDataProtocol(values);
    }

    @Override
    public List<Map<String, String>> getDataProtocol() {
        List<Map<String, String>> list = new ArrayList<>();
        //load default protocol
        Cursor cursor1 = mManager.queryProtocolDefault();
        cursor1.moveToFirst();
        while (cursor1.getCount() > 0 && !cursor1.isAfterLast()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_HEADER, cursor1.getString(cursor1.getColumnIndex(Constants.KEY_DATA_HEADER)));
            map.put(Constants.KEY_DATA_TAIL, cursor1.getString(cursor1.getColumnIndex(Constants.KEY_DATA_TAIL)));
            list.add(map);
            cursor1.moveToNext();
        }
        cursor1.close();
        //load custom protocol
        Cursor cursor = mManager.queryProtocolCustom();
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
    public List<Map<String, String>> getDefault() {
        mManager.deleteAllTypeCustom();
        mManager.deleteAllProtocolCustom();
        List<Map<String, String>> list = new ArrayList<>();
        Cursor cursor = mManager.queryTypeDefault();
        cursor.moveToFirst();
        while (cursor.getCount() > 0 && !cursor.isAfterLast()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_NAME, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_NAME)));
            map.put(Constants.KEY_DATA_CODE, cursor.getString(cursor.getColumnIndex(Constants.KEY_DATA_CODE)));
            list.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        Cursor cursor1 = mManager.queryProtocolDefault();
        cursor1.moveToFirst();
        while (cursor1.getCount() > 0 && !cursor1.isAfterLast()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.KEY_DATA_HEADER, cursor1.getString(cursor1.getColumnIndex(Constants.KEY_DATA_HEADER)));
            map.put(Constants.KEY_DATA_TAIL, cursor1.getString(cursor1.getColumnIndex(Constants.KEY_DATA_TAIL)));
            list.add(map);
            cursor1.moveToNext();
        }
        cursor1.close();
        Log.v(TAG, String.valueOf(list.size()));
        return list;
    }
}
