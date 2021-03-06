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

    //debug
    private static final String TAG = DBManager.class.getSimpleName();

    /**
     * instance of DBManager
     */
    private static DBManager mInstance;

    /**
     * sqlite database
     */
    private SQLiteDatabase db;

    /**
     * context for constructor's need
     */
    private static Context mContext;

    /**
     * name of table templates
     */
    private static final String TABLE_TEMPLATES = "templates";

    /**
     * name of table type
     */
    private static final String TABLE_TYPE = "data_type";

    /**
     * name of table protocol
     */
    private static final String TABLE_PROTOCOL = "data_protocol";

    /**
     * private constructor
     */
    private DBManager() {
        DBHelper mHelper = new DBHelper(mContext);
        db = mHelper.getWritableDatabase();
        initDefault();
    }

    /**
     * get context
     * @param context context
     */
    public static void init(Context context) {
        mContext = context;
    }

    /**
     * single instance pattern
     *
     * @return instance
     */
    public static DBManager getInstance() {
        Log.v(TAG, "DBManager init");
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null)
                    mInstance = new DBManager();
            }
        }
        return mInstance;
    }

    /**
     * insert a view
     *
     * @param values content values
     */
    public void insertView(ContentValues values) {
        db.insert(TABLE_TEMPLATES, null, values);
    }

    /**
     * delete a template by template name
     *
     * @param templateName template name
     */
    public void deleteTemplate(String templateName) {
        db.delete(TABLE_TEMPLATES, "template_name = ?", new String[]{templateName});
    }

    /**
     * delete all templates
     */
    public void deleteAllTemplates() {
        db.execSQL("DELETE FROM " + TABLE_TEMPLATES);
        String sql = "UPDATE SQLITE_SEQUENCE SET SEQ = 0 WHERE NAME = '" + TABLE_TEMPLATES + "\'";
        db.execSQL(sql);
    }

    /**
     * query all templates' name
     * @return cursor
     */
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
        Log.v(TAG, "tail: " + values.get(Constants.KEY_DATA_TAIL));
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
        return db.rawQuery("SELECT * FROM " + TABLE_TYPE + " WHERE _id <= ?", new String[]{"4"});
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
        db.delete(TABLE_TYPE, "_id >= ?", new String[]{"5"});
    }

    /**
     * delete all custom protocols
     */
    public void deleteDataProtocolCustom() {
        db.delete(TABLE_PROTOCOL, "_id >= ?", new String[]{"2"});
    }

    /**
     * get code of data type by name
     * @param name name of type
     * @return code
     */
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
        db.execSQL("INSERT OR IGNORE INTO data_type (_id, name, code) VALUES ('1', '数码管', 'aa')");
        db.execSQL("INSERT OR IGNORE INTO data_type (_id, name, code) VALUES ('2', 'LED', 'ab')");
        db.execSQL("INSERT OR IGNORE INTO data_type (_id, name, code) VALUES ('3', '亮度', 'ac')");
        db.execSQL("INSERT OR IGNORE INTO data_type (_id, name, code) VALUES ('4', 'USB', 'ad')");
        db.execSQL("INSERT OR REPLACE INTO data_protocol (_id, header, tail) VALUES ('1', 'FF','FF')");
    }
}
