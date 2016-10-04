package me.stupideme.embeddedtool.view.bluetooth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import me.stupideme.embeddedtool.R;

import static me.stupideme.embeddedtool.view.bluetooth.DeviceListActivity.EXTRA_DEVICE_ADDRESS;


/**
 * Created by stupidl on 16-9-12.
 */
public class DevicePairedFragment extends Fragment {

    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ProgressDialog mProgressDialog;

    public DevicePairedFragment() {
        // Required empty public constructor
    }

    public static DevicePairedFragment newInstance() {
        return new DevicePairedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.device_name);
        mProgressDialog = new ProgressDialog(getActivity());
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);

        //Get a set of currently paired devices


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paired_device, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ListView pairedListView = (ListView) view.findViewById(R.id.list_paired_device);
        mPairedDevicesArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.device_name);
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);
        if (mBtAdapter != null) {
            Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
            // If there are paired devices, add each one to the ArrayAdapter
            if (pairedDevices.size() > 0) {
                //findViewById(R.id.title_paired_devices).setVisibility(View.VISIBLE);
                for (BluetoothDevice device : pairedDevices) {
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    Log.i("notify", "paired");
                }
            } else {
                String noDevices = getResources().getText(R.string.none_paired).toString();
                mPairedDevicesArrayAdapter.add(noDevices);
                mPairedDevicesArrayAdapter.notifyDataSetChanged();
                Log.i("notify", "no paired");
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        getActivity().unregisterReceiver(mReceiver);
    }


    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            mBtAdapter.cancelDiscovery();
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            if (info.contains(":")) {
                Intent intent = new Intent();
                String address = info.substring(info.length() - 17);
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "蓝牙地址不正确", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mPairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDialog.dismiss();
                mProgressDialog.cancel();
                if (mPairedDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(R.string.none_found).toString();
                    mPairedDevicesArrayAdapter.add(noDevices);
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "未找到设备", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
