package me.stupideme.embeddedtool;

/**
 * Created by StupidL on 2016/10/1.
 */

public class Constants {
    public static final int[] mColors = {R.color.Gray, R.color.Purple, R.color.Indigo, R.color.Teal,
            R.color.Orange, R.color.Brown, R.color.BlueGray};

    public static final DataType[] mDataTypes = {DataType.LED, DataType.BUZZER, DataType.TEMPERATURE};

    public static final int REQUEST_CODE_SEND = 0x01;
    public static final int REQUEST_CODE_RECEIVE = 0x10;
    public static final int REQUEST_CODE_CHART = 0x11;
    public static final int MESSAGE_BODY_EMPTY = 0x00000000;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";


    // database table templates columns
    public static final String TEMPLATE_NAME = "template_name";
    public static final String VIEW_ID = "view_id";
    public static final String VIEW_TYPE = "view_type";
    public static final String HAS_BIND_VIEW = "has_bind_view";
    public static final String BIND_VIEW_ID = "bind_view_id";
    public static final String VIEW_TEXT = "view_text";
    public static final String VIEW_WIDTH = "view_width";
    public static final String VIEW_HEIGHT= "view_height";
    public static final String VIEW_X = "view_x";
    public static final String VIEW_Y = "view_y";
    public static final String VIEW_COLOR = "view_color";
    public static final String SPINNER_COLOR_POS = "spinner_color_pos";
    public static final String VIEW_TYPE_POS = "view_type_pos";

    public static final int VIEW_TYPE_BUTTON_SEND = 1;
    public static final int VIEW_TYPE_BUTTON_RECEIVE = 2;
    public static final int VIEW_TYPE_TEXT_VIEW = 3;
    public static final int VIEW_TYPE_EDIT_TEXT = 4;

    public static final int HAS_BIND_VIEW_INVALID = -1;


    public static final String KEY_NAME = "name";
    public static final String KEY_WIDTH = "width";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_ID = "id";
    public static final String KEY_TYPE_POS = "type";
    public static final String KEY_COLOR_POS = "color";
    public static final String KEY_BIND_VIEW_ID = "bind_view_id";
    public static final String KEY_MAX_X = "MaxX";
    public static final String KEY_MAX_Y = "MaxY";
    public static final String KEY_MIN_X = "MinX";
    public static final String KEY_MIN_Y = "MinY";

}
