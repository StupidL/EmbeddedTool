package me.stupideme.embeddedtool.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import me.stupideme.embeddedtool.Constants;

/**
 * Created by stupidl on 16-10-9.
 */

public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();
    private static DBManager mInstance;
    private SQLiteDatabase db;

    private static final String TABLE_TEMPLATES = "templates";
    private static final String TABLE_TYPE_DEFAULT = "data_type_default";
    private static final String TABLE_TYPE_CUSTOM = "data_type_custom";
    private static final String TABLE_PROTOCOL_DEFAULT = "data_protocol_default";
    private static final String TABLE_PROTOCOL_CUSTOM = "data_protocol_custom";

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
        db.insert(TABLE_TYPE_CUSTOM, null, values);
    }

    /**
     * insert a custom data protocol
     *
     * @param values
     */
    public void insertDataProtocol(ContentValues values) {
        db.insert(TABLE_PROTOCOL_CUSTOM, null, values);
    }

    /**
     * delete a custom data type
     *
     * @param name
     */
    public void deleteDataType(String name) {
        db.delete(TABLE_TYPE_CUSTOM, "name = ?", new String[]{name});
    }

    /**
     * update a custom data protocol
     *
     * @param values
     */
    public void updateDataProtocol(ContentValues values) {
        db.delete(TABLE_PROTOCOL_CUSTOM, "_id >= ?", new String[]{"0"});
        db.insert(TABLE_PROTOCOL_CUSTOM, null, values);
    }

    /**
     * query default data type
     *
     * @return
     */
    public Cursor queryTypeDefault() {
        return db.rawQuery("SELECT * FROM " + TABLE_TYPE_DEFAULT + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * query custom data type
     *
     * @return
     */
    public Cursor queryTypeCustom() {
        return db.rawQuery("SELECT * FROM " + TABLE_TYPE_CUSTOM + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * query default data protocol
     *
     * @return
     */
    public Cursor queryProtocolDefault() {
        return db.rawQuery("SELECT * FROM " + TABLE_PROTOCOL_DEFAULT + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * query custom data protocol
     *
     * @return
     */
    public Cursor queryProtocolCustom() {
        return db.rawQuery("SELECT * FROM " + TABLE_PROTOCOL_CUSTOM + " WHERE _id >= ?", new String[]{"0"});
    }

    /**
     * delete all custom types
     */
    public void deleteAllTypeCustom() {
        db.delete(TABLE_TYPE_CUSTOM, "_id >= ?", new String[]{"0"});
    }

    /**
     * delete all custom protocols
     */
    public void deleteAllProtocolCustom() {
        db.delete(TABLE_PROTOCOL_CUSTOM, "_id >= ?", new String[]{"0"});
    }

    /**
     * set default types and protocol
     */
    private void initDefault() {
        db.execSQL("INSERT OR REPLACE INTO data_type_default (name, code) VALUES ('LED', 'aa')");
        db.execSQL("INSERT OR REPLACE INTO data_type_default (name, code) VALUES ('BUZZER', 'ab')");
        db.execSQL("INSERT OR REPLACE INTO data_type_default (name, code) VALUES ('TEMPERATURE', 'ac')");
        db.execSQL("INSERT OR REPLACE INTO data_protocol_default (header, tail) VALUES ('FFFFFF','FFFFFF')");
    }
}
