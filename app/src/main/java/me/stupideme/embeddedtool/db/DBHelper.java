package me.stupideme.embeddedtool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import me.stupideme.embeddedtool.view.custom.StupidTextView;

/**
 * Created by stupidl on 16-10-9.
 */

class DBHelper extends SQLiteOpenHelper {

    /**
     * the name of this database
     */
    private static final String DB_NAME = "embedded.db";

    /**
     * the version of this database
     */
    private static final int VERSION = 1;

    /**
     * create table templates
     */
    private static final String CREATE_TABLE_TEMPLATES = "CREATE TABLE templates(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " template_name, view_id , " +
            "view_type, view_type_pos, has_bind_view, bind_view_id, view_text, view_width, view_height, " +
            "view_x, view_y, view_color, spinner_color_pos" + ")";

    DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table templates
        sqLiteDatabase.execSQL(CREATE_TABLE_TEMPLATES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
