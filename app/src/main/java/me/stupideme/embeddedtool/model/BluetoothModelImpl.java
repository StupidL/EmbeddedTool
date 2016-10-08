package me.stupideme.embeddedtool.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.net.BluetoothChatService;

/**
 * Created by StupidL on 2016/9/30.
 */

public class BluetoothModelImpl implements IBluetoothModel {

    private BluetoothChatService mService;
    private Context mContext;
    private String tmpReadMessage;
    private String tmpWriteMessage;
    private List<String> readMessage = new ArrayList<>();
    private List<String> writeMessage = new ArrayList<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    //receive this message when the connection status changed
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Toast.makeText(mContext, "已连接设备" + mConnectedDeviceName,
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            Toast.makeText(mContext, "正在连接设备......",
                                    Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    //receive this message when connected thread runs method write(byte[] buffer) successfully
                    byte[] writeBuf = (byte[]) msg.obj;
                    //construct a string from the buffer
                    tmpWriteMessage = new String(writeBuf);
                    break;
                case Constants.MESSAGE_READ:
                    //receive this message when connected thread runs method run() successfully
                    byte[] readBuf = (byte[]) msg.obj;
                    //construct a string from the valid bytes in the buffer
                    tmpReadMessage = new String(readBuf, 0, msg.arg1);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    //receive this message when connected a device
                    Toast.makeText(mContext, "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
                    //receive this message when the connection failed or lost
                    Toast.makeText(mContext, msg.getData().getString(Constants.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public BluetoothModelImpl(Context context) {
        mService = new BluetoothChatService(mHandler);
        mContext = context;
    }

    @Override
    public void connectDevice(String address, boolean secure) {
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
        mService.connect(device, secure);
    }

    @Override
    public void sendDataOverButton(DataType type, String s) {
        switch (type) {
            case BUTTON_0:

                break;
            case BUTTON_1:

                break;
        }
    }

    @Override
    public String receiveDataOverButton(DataType type) {
        return "";
    }


}
