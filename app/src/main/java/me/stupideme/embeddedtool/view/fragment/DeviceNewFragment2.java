package me.stupideme.embeddedtool.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;


/**
 * Created by StupidL on 2016/10/31.
 */

public class DeviceNewFragment2 extends Fragment {


    private static final String TAG = "DeviceNewFragment";
    private ProgressDialog mProgressDialog;
    private BluetoothAdapter mBtAdapter;
    private RelativeLayout relativeLayout;
    ArrayAdapter<String> mNewDevicesArrayAdapter;

    private static final int REQUEST_ENABLE_BT = 0x200;


    public DeviceNewFragment2() {
    }

    public static me.stupideme.embeddedtool.view.fragment.DeviceNewFragment newInstance() {
        return new me.stupideme.embeddedtool.view.fragment.DeviceNewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_device, container, false);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relative_layout);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.bluetooth_search);
        mNewDevicesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.device_name);
        ListView newDevicesListView = (ListView) view.findViewById(R.id.list_new_device);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        mProgressDialog = new ProgressDialog(getActivity());
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //mBtAdapter.cancelDiscovery();
                setupBluetooth();
                if (mBtAdapter.isEnabled()) {
                    mProgressDialog.setTitle("扫描设备");
                    mProgressDialog.setMessage("扫描设备中，请稍等...");
                    mProgressDialog.show();
                    mProgressDialog.setCancelable(false);
                    doDiscovery();

//                    mClient.searchDevices(new OnSearchDeviceListener() {
//                        @Override
//                        public void onStartDiscovery() {
//                            mProgressDialog.setTitle("扫描设备");
//                            mProgressDialog.setMessage("扫描设备中，请稍等...");
//                            mProgressDialog.show();
//                            mProgressDialog.setCancelable(false);
//                            Log.v(TAG, "start search bluetooth");
//                        }
//
//                        @Override
//                        public void onNewDeviceFounded(BluetoothDevice bluetoothDevice) {
//                            mNewDevicesArrayAdapter.add(bluetoothDevice.getName() + "\n" + bluetoothDevice.getAddress());
//                            mNewDevicesArrayAdapter.notifyDataSetChanged();
//                            Log.v(TAG, "new device found");
//                        }
//
//                        @Override
//                        public void onSearchCompleted(List<BluetoothDevice> list, List<BluetoothDevice> list1) {
//                            mProgressDialog.dismiss();
//                            Log.v(TAG, "list size: " + list.size() + "list1 size: " + list1.size());
//                            Log.v(TAG, "search completed");
//
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//                            Toast.makeText(getActivity(), "错误", Toast.LENGTH_SHORT).show();
//                            Log.v(TAG, "search error");
//                        }
//                    });
                }
            }
        });

        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        getActivity().unregisterReceiver(mReceiver);
    }


    private void setupBluetooth() {
        if (mBtAdapter == null) {
            Toast.makeText(getActivity(), "设备不支持蓝牙！", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBtAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mNewDevicesArrayAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDialog.dismiss();
                mProgressDialog.cancel();
                if (mNewDevicesArrayAdapter.getCount() == 0) {

                    final Snackbar snackbar = Snackbar.make(relativeLayout, "未找到设备", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.setDuration(1500);
                    snackbar.show();
                }
            }
        }
    };

    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBtAdapter.cancelDiscovery();
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_DEVICE_ADDRESS, address);
            Log.v(TAG, "BT address: " + address);
            //back to MainActivity and try to connect device
            getActivity().setResult(Activity.RESULT_OK, intent);
            getActivity().finish();
        }
    };
}

