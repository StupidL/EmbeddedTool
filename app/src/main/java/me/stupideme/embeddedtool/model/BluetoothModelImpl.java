package me.stupideme.embeddedtool.model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.net.BluetoothChatService;

/**
 * Created by StupidL on 2016/9/30.
 */

public class BluetoothModelImpl implements IBluetoothModel {

    private BluetoothChatService mService;
    private Context mContext;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
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
//                            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                     construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
//                    mConversationArrayAdapter.add("Me:  " + writeMessage);
                    break;
                case Constants.MESSAGE_READ:
//                    byte[] readBuf = (byte[]) msg.obj;
//                     construct a string from the valid bytes in the buffer
//                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    Toast.makeText(mContext, "Connected to " + mConnectedDeviceName,
                            Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_TOAST:
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
