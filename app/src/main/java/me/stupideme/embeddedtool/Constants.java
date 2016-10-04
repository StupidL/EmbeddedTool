package me.stupideme.embeddedtool;

/**
 * Created by StupidL on 2016/10/1.
 */

public class Constants {
    public static final int[] mColors = {R.color.Gray, R.color.Purple, R.color.Indigo, R.color.Teal,
            R.color.Orange, R.color.Brown, R.color.BlueGray};

    public static final DataType[] mButtonTypes = {DataType.BUTTON_0, DataType.BUTTON_1, DataType.BUTTON_2,
            DataType.BUTTON_3, DataType.BUTTON_4};

    public static final String ACTION_BUTTON_CLICKED = "me.stupidme.action.BUTTON_CLICKED";

    public static final int BUTTON_TYPE_SEND = 0x10;
    public static final int BUTTON_TYPE_RECEIVE = 0x11;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

}
