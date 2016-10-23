package me.stupideme.embeddedtool.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by stupidl on 16-10-9.
 */

public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();
    private static DBManager mInstance;
    private SQLiteDatabase db;

    private static final String TABLE_TEMPLATES = "templates";
    private static final String TABLE_TYPE = "data_type";
    private static final String TABLE_PROTOCOL = "data_protocol";

    /**
     * private constructor
     *
     * @param context context
     */
    private DBManager(Context context) {
        DBHelper mHelper = new DBHelper(context);
        Log.i(TAG, String.valueOf(mHelper.getDatabaseName()));
        Log.i(TAG, String.valueOf(mHelper));
        db = mHelper.getWritableDatabase();
        initDefault();
    }

    /**
     * single instance pattern
     *
     * @param context context
     * @return instance
     */
    public static DBManager getInstance(Context context) {
        Log.v(TAG, "DBManager init");
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null)
                    mInstance = new DBManager(context);
            }
        }
        return mInstance;
    }

    /**
     * insert data from a send type button
     */
    public void insertSendButton(ContentValues sendButton) {
        db.insert(TABLE_TEMPLATES, null, sendButton);
    }

    /**
     * insert data from a receive type button
     */
    public void insertReceiveButton(ContentValues receiveButton) {
        db.insert(TABLE_TEMPLATES, null, receiveButton);
    }

    /**
     * insert data from a text view
     */
    public void insertTextView(ContentValues textView) {
        db.insert(TABLE_TEMPLATES, null, textView);
    }

    /**
     * insert data from edit text
     */
    public void insertEditText(ContentValues editText) {
        db.insert(TABLE_TEMPLATES, null, editText);
    }

    /**
     * delete a template by template name
     *
     * @param templateName template name
     */
    public void deleteTemplate(String templateName) {
        db.delete(TABLE_TEMPLATES, "template_name = ?", new String[]{templateName});
    }

    public void deleteAllTemplates() {
        db.execSQL("DELETE FROM " + TABLE_TEMPLATES);
        String sql = "UPDATE SQLITE_SEQUENCE SET SEQ = 0 WHERE NAME = '" + TABLE_TEMPLATES + "\'";
        db.execSQL(sql);
    }

    public Cursor queryAllTemplateName() {
        return db.rawQuery("SELECT DISTINCT template_name FROM " + TABLE_TEMPLATES + " where _id >= ?", new String[]{"0"});
    }

    /**
     * query a template by template name
     *
     * @param templateName template name
     * @return cursor
     */
    public Cursor queryTemplate(String templateName) {
        return db.rawQuery("SELECT * FROM " + TABLE_TEMPLATES + " WHERE template_name = ?", new String[]{templateName});
    }

    /**
     * query all templates
     *
     * @return cursor
     */
    public Cursor queryAllTemplates() {
        return db.rawQuery("SELECT * FROM " + TABLE_TEMPLATES + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * insert a data type
     *
     * @param values
     */
    public void insertDataType(ContentValues values) {
        db.insert(TABLE_TYPE, null, values);
    }

    /**
     * insert a data protocol
     *
     * @param values
     */
    public void insertDataProtocol(ContentValues values) {
        db.insert(TABLE_PROTOCOL, null, values);
    }

    /**
     * delete a data type
     *
     * @param name
     */
    public void deleteDataType(String name) {
        db.delete(TABLE_TYPE, "name = ?", new String[]{name});
    }

    /**
     * query data type
     *
     * @return
     */
    public Cursor queryDataType() {
        return db.rawQuery("SELECT * FROM " + TABLE_TYPE + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * query default data type
     *
     * @return
     */
    public Cursor queryDataTypeDefault() {
        return db.rawQuery("SELECT * FROM " + TABLE_TYPE + " WHERE _id <= ?", new String[]{"3"});
    }

    /**
     * query default data protocol
     *
     * @return
     */
    public Cursor queryDataProtocolDefault() {
        return db.rawQuery("SELECT * FROM " + TABLE_PROTOCOL + " WHERE _id = ?", new String[]{"1"});
    }

    /**
     * query all data protocol
     *
     * @return
     */
    public Cursor queryDataProtocol() {
        return db.rawQuery("SELECT * FROM " + TABLE_PROTOCOL + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * delete all custom types
     */
    public void deleteDataTypeCustom() {
        db.delete(TABLE_TYPE, "_id >= ?", new String[]{"4"});
    }

    /**
     * delete all custom protocols
     */
    public void deleteDataProtocolCustom() {
        db.delete(TABLE_PROTOCOL, "_id >= ?", new String[]{"2"});
    }

    public String queryTypeCodeByName(String name) {
        Cursor cursor = db.rawQuery("SELECT * FROM data_type WHERE name = ?", new String[]{name});
        cursor.moveToFirst();
        String s = cursor.getString(cursor.getColumnIndex("code"));
        cursor.close();
        return s;
    }

    /**
     * set default types and protocol
     */
    private void initDefault() {
        db.execSQL("INSERT OR REPLACE INTO data_type (_id, name, code) VALUES ('1', 'LED', 'aa')");
        db.execSQL("INSERT OR REPLACE INTO data_type (_id, name, code) VALUES ('2', 'BUZZER', 'ab')");
        db.execSQL("INSERT OR REPLACE INTO data_type (_id, name, code) VALUES ('3', 'TEMPERATURE', 'ac')");
        db.execSQL("INSERT OR REPLACE INTO data_protocol (_id, header, tail) VALUES ('1', 'FFFFFF','FFFFFF')");
    }
}
