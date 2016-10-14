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
    }

    /**
     * single instance pattern
     *
     * @param context context
     * @return instance
     */
    public static synchronized DBManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DBManager(context);
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

}
