package me.stupideme.embeddedtool;

/**
 * Created by StupidL on 2016/9/29.
 */

public class StupidProtocol {
    public static final String BUTTON_SEND_HEADER = "FFBB";
    public static final String BUTTON_SEND_TAIL = "FFBBFF";
    public static final String BUTTON_RECEIVE_HEADER = "FFBB";
    public static final String BUTTON_RECEIVE_TAIL = "FFBBFF";

    public static final int APP_ID = 0x01;
    public static final int VERSION = 0x01;
    public static final int COMMAND = 0x01;
    public static final int FRAME_HEADER = APP_ID << 32 + VERSION << 16 + COMMAND;
}
