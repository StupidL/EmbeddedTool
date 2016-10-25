package me.stupideme.embeddedtool.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.net.BluetoothService;
import me.stupideme.embeddedtool.presenter.MainPresenter;
import me.stupideme.embeddedtool.view.custom.OnBindViewIdChangedListener;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

public class MainActivity extends AppCompatActivity implements IMainView, OnBindViewIdChangedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * request code to start DeviceListActivity to connect bluetooth in secure way
     */
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;

    /**
     * request code to start DeviceListActivity to connect bluetooth in insecure way
     */
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;

    /**
     * request code to enable bluetooth
     */
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     * request code to select a template for database and create it in MainActivity
     */
    private static final int REQUEST_SELECT_TEMPLATE = 4;

    /**
     * request code to start settings activity
     */
    private static final int REQUEST_SETTINGS_ADVANCED = 5;
    private static final int REQUEST_DOCUMENT = 6;

    /**
     * the presenter. We are using MVP pattern
     */
    public static MainPresenter mPresenter;

    private Handler mHandler;

    /**
     * a frame layout to container all of the views such as a Send Button, a Receive button
     */
    private FrameLayout mFrameLayout;

    /**
     * a touch listener to implements move event
     */
    private View.OnTouchListener mTouchListener;

    /**
     * id of every single view added to container
     */
    private static int viewIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init presenter
        mPresenter = MainPresenter.getInstance(MainActivity.this);
        //init view
        initView();
        //set handler for bluetooth service in model
        mPresenter.setHandler(mHandler);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    //use presenter to connect device according to address in secure way
                    String address = data.getStringExtra(Constants.EXTRA_DEVICE_ADDRESS);
                    mPresenter.connectDevice(address, true);
                    Log.v("MainPresenter ", "connect device secure...");
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    //use presenter to connect device according to address in insecure way
                    String address = data.getStringExtra(Constants.EXTRA_DEVICE_ADDRESS);
                    mPresenter.connectDevice(address, false);
                    Log.v("MainPresenter ", "connect device insecure...");
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    //a dialog appears in onStart() method, if click "ok" system will enable bluetooth
                    // and we need do nothing here
                    Log.d("MainActivity", "BT enabled");
                } else {
                    //if click "cancel", application will exit.
                    Log.d("MainActivity", "BT not enabled");
                    finish();
                }
                break;
            case REQUEST_SELECT_TEMPLATE:
                if (resultCode == Activity.RESULT_OK) {
                    String name = data.getStringExtra("TemplateName");
                    mPresenter.loadTemplate(mFrameLayout, name, MainActivity.this);
                    for (int i = 0; i < mFrameLayout.getChildCount(); i++) {
                        View view = mFrameLayout.getChildAt(i);
                        view.setOnTouchListener(mTouchListener);
                        if (view instanceof StupidButtonSend) {
                            mPresenter.setSendMessageListenerForButton((StupidButtonSend) view);
                        } else if (view instanceof StupidButtonReceive) {
                            mPresenter.setSendMessageListenerForButton((StupidButtonReceive) view);
                        } else if (view instanceof StupidTextView) {
                            ((StupidTextView) view).setBindViewListener(MainActivity.this);
                        } else if (view instanceof StupidEditText) {
                            ((StupidEditText) view).setBindViewListener(MainActivity.this);
                        }
                    }
                    mPresenter.updateSpinnerAdapter();
                }
                break;
            case REQUEST_SETTINGS_ADVANCED:
                if (resultCode == Activity.RESULT_OK) {
                    mPresenter.updateSpinnerAdapter();
                    Log.v(TAG, "Result_OK, updateSpinnerAdapter");
                }
                break;

        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
//            // if bluetooth is not enabled, request to enable it.
//            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        }
//    }

    /**
     * create and init a send type button, which will be contained in mFrameLayout.
     */
    @Override
    public void addSendButton() {
        StupidButtonSend stupidButtonSend = new StupidButtonSend(MainActivity.this);
        stupidButtonSend.setId(viewIndex++);
        stupidButtonSend.setText("Button" + stupidButtonSend.getId());
        stupidButtonSend.setOnTouchListener(mTouchListener);
        mPresenter.setSendMessageListenerForButton(stupidButtonSend);
        mFrameLayout.addView(stupidButtonSend);
        mPresenter.updateSpinnerAdapter();
    }

    /**
     * create and init a receive type button, which will be contained in mFrameLayout.
     */
    @Override
    public void addReceiveButton() {
        StupidButtonReceive button = new StupidButtonReceive(MainActivity.this);
        button.setId(viewIndex++);
        button.setText("Button" + button.getId());
        button.setOnTouchListener(mTouchListener);
        mPresenter.setSendMessageListenerForButton(button);
        mPresenter.attachObserver(button);
        mFrameLayout.addView(button);
        mPresenter.updateSpinnerAdapter();
    }

    @Override
    public void addTextView() {
        StupidTextView stupidTextView = new StupidTextView(this);
        stupidTextView.setId(viewIndex++);
        stupidTextView.setText("TextView ID: " + stupidTextView.getId());
        stupidTextView.setOnTouchListener(mTouchListener);
        stupidTextView.setBindViewListener(this);
        mFrameLayout.addView(stupidTextView);
        Log.i("StupidTextViewID ", stupidTextView.getId() + "");
    }

    @Override
    public void addEditText() {
        StupidEditText editText = new StupidEditText(this);
        editText.setOnTouchListener(mTouchListener);
        editText.setBindViewListener(this);
        editText.setId(viewIndex++);
        editText.setText("EditText ID: " + editText.getId());
        mFrameLayout.addView(editText);
        Log.v("StupidEditTextID ", editText.getId() + "");
    }

    @Override
    public View getViewById(int id) {
        return mFrameLayout.findViewById(id);
    }

    @Override
    public void clearViews() {
        mFrameLayout.removeAllViews();
    }

    @Override
    public void updateTypeSpinnerAdapter(List<String> list) {
        for (int i = 0; i < mFrameLayout.getChildCount(); i++) {
            View view = mFrameLayout.getChildAt(i);
            if (view instanceof StupidButtonSend) {
                ((StupidButtonSend) view).updateSpinnerAdapter(list);
            }
            if (view instanceof StupidButtonReceive) {
                ((StupidButtonReceive) view).updateSpinnerAdapter(list);
            }
        }
    }


    @Override
    public void onBindViewIdChanged(int other, int self) {
        View view = getViewById(self);
        View view1 = getViewById(other);
        if (view instanceof StupidTextView) {
            mPresenter.bindTextViewById(other, self);
            Log.v(TAG, "bind text view success");
        } else if (view instanceof StupidEditText) {
            if (view1 instanceof StupidButtonSend) {
                mPresenter.bindEditTextById(other, self);
                Log.v(TAG, "bind edit text success");
            } else
                Toast.makeText(MainActivity.this, "编辑框只能绑定发送类型的按钮～", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * init view
     */
    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("嵌入式助手");
        setSupportActionBar(toolbar);
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_main);
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.fab_menu);
        FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.fab_send_button);
        FloatingActionButton receiveButton = (FloatingActionButton) findViewById(R.id.fab_receive_button);
        FloatingActionButton textView = (FloatingActionButton) findViewById(R.id.fab_text_view);
        FloatingActionButton editText = (FloatingActionButton) findViewById(R.id.fab_edit_text);
        FloatingActionButton chart = (FloatingActionButton) findViewById(R.id.fab_chart);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addSendButton();
            }
        });
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addReceiveButton();
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addTextView();
            }
        });
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addEditText();
            }
        });
        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChartActivity.class));
            }
        });

        // onTouchListener
        mTouchListener = new View.OnTouchListener() {
            float dX, dY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        break;
                    default:
                        return false;
                }
                return false;
            }
        };

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);

                switch (msg.what) {
                    case Constants.MESSAGE_STATE_CHANGE:
                        //receive this message when the connection status changed
                        switch (msg.arg1) {
                            case BluetoothService.STATE_CONNECTED:
                                getSupportActionBar().setTitle("已连接设备" + mConnectedDeviceName);
                                break;
                            case BluetoothService.STATE_CONNECTING:
                                getSupportActionBar().setTitle("正在连接设备......");
                                break;
                            case BluetoothService.STATE_LISTEN:
                            case BluetoothService.STATE_NONE:
                                getSupportActionBar().setTitle("嵌入式助手");
                                break;
                        }
                        break;
                    case Constants.MESSAGE_WRITE:

                        break;
                    case Constants.MESSAGE_READ:
                        //receive this message when connected thread runs method run() successfully
                        byte[] readBuf = (byte[]) msg.obj;
                        //construct a string from the valid bytes in the buffer
                        mPresenter.notifyObservers(new String(readBuf, 0, msg.arg1));

                        break;
                    case Constants.MESSAGE_DEVICE_NAME:
                        //receive this message when connected a device
                        Toast.makeText(MainActivity.this, "Connected to " + mConnectedDeviceName,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.MESSAGE_TOAST:
                        //receive this message when the connection failed or lost
                        Toast.makeText(MainActivity.this, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    /**
     * create option menu
     *
     * @param menu menu
     * @return create menu or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * handle onClick events on option menu
     *
     * @param item view be clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_advanced) {
            // action settings clicked
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivityForResult(intent, REQUEST_SETTINGS_ADVANCED);
        }
        if (id == R.id.action_bluetooth_secure) {
            // action bluetooth secure clicked,
            // start DeviceListActivity with request code REQUEST_CONNECT_DEVICE_SECURE.
            startActivityForResult(new Intent(MainActivity.this, DeviceListActivity.class),
                    REQUEST_CONNECT_DEVICE_SECURE);
        }
        if (id == R.id.action_bluetooth_insecure) {
            // action bluetooth insecure clicked,
            // start DeviceListActivity with request code REQUEST_CONNECT_DEVICE_INSECURE.
            startActivityForResult(new Intent(MainActivity.this, DeviceListActivity.class),
                    REQUEST_CONNECT_DEVICE_INSECURE);
        }
        if (id == R.id.action_bluetooth_discoverable) {
            // action bluetooth discoverable clicked, request to be discoverable.
            // we need do nothing else just start the activity and click "ok",
            // system will do other things for us
            if (BluetoothAdapter.getDefaultAdapter().getScanMode() !=
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            }
        }
        if (id == R.id.action_clear) {
            mPresenter.removeAllViews();
        }
        if (id == R.id.action_save_template) {
            // create a dialog to save a template
            // you can add a name for this template
            TextInputLayout layout = new TextInputLayout(MainActivity.this);
            layout.setPadding(32, 32, 32, 0);
            final TextInputEditText editText = new TextInputEditText(MainActivity.this);
            editText.setHint("保存的模板名称");
            layout.addView(editText);

            final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mPresenter.saveTemplate(mFrameLayout, editText.getText().toString());
                            Toast.makeText(MainActivity.this, "模板保存成功!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setTitle("保存为模板")
                    .setView(layout).create();
            dialog.show();

        }
        if (id == R.id.action_select_template) {
            startActivityForResult(new Intent(MainActivity.this, TemplateActivity.class), REQUEST_SELECT_TEMPLATE);
        }
        if (id == R.id.action_document) {
            startActivityForResult(new Intent(MainActivity.this, DocumentActivity.class), REQUEST_DOCUMENT);
        }
        return super.onOptionsItemSelected(item);
    }

}