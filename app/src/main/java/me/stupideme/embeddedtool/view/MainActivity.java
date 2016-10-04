package me.stupideme.embeddedtool.view;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.MainPresenter;
import me.stupideme.embeddedtool.view.bluetooth.DeviceListActivity;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

public class MainActivity extends AppCompatActivity implements IMainView {

    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    public static MainPresenter mPresenter;
    private FrameLayout mFrameLayout;

    private View.OnTouchListener mTouchListener;
    private static int viewIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this, this);
        initView();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getStringExtra("device_address");
                    mPresenter.connectDevice(address, true);
                    Log.v("MainPresenter ", "connect device secure...");
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getStringExtra("device_address");
                    mPresenter.connectDevice(address, false);
                    Log.v("MainPresenter ", "connect device insecure...");
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("MainActivity", "BT enabled");
                } else {
                    Log.d("MainActivity", "BT not enabled");
                    finish();
                }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }


    @Override
    public void addSendButton() {
        StupidButtonSend stupidButtonSend = new StupidButtonSend(MainActivity.this, mPresenter);
        stupidButtonSend.setId(viewIndex++);
        stupidButtonSend.setText("Button" + stupidButtonSend.getId());
        stupidButtonSend.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(stupidButtonSend);
        Log.i("StupidSendBtnID: ", stupidButtonSend.getId() + "");
    }

    @Override
    public void addReceiveButton() {
        StupidButtonReceive button = new StupidButtonReceive(MainActivity.this, mPresenter);
        button.setId(viewIndex++);
        button.setText("Button" + button.getId());
        button.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(button);
        Log.i("StupidReceiveBtnID: ", button.getId() + "");
    }

    @Override
    public void removeButton(Button view) {
        mFrameLayout.removeView(view);
    }

    @Override
    public void addTextView() {
        StupidTextView stupidTextView = new StupidTextView(MainActivity.this, mPresenter);
        stupidTextView.setId(viewIndex++);
        stupidTextView.setText("id: " + stupidTextView.getId());
        stupidTextView.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(stupidTextView);
        Log.i("StupidTextViewID ", stupidTextView.getId() + "");
    }

    @Override
    public void removeTextView(TextView view) {
        mFrameLayout.removeView(view);
    }

    @Override
    public void addEditText() {
        StupidEditText editText = new StupidEditText(this, mPresenter);
        editText.setOnTouchListener(mTouchListener);
        editText.setId(viewIndex++);
        editText.setText("ID: " + editText.getId());
        mFrameLayout.addView(editText);
        Log.v("StupidEditTextID ", editText.getId() + "");
    }

    @Override
    public void removeEditText(StupidEditText view) {
        mFrameLayout.removeView(view);
    }

    @Override
    public View getViewById(int id) {
        return mFrameLayout.findViewById(id);
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setOnTouchListener();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_bluetooth_secure) {
            startActivityForResult(new Intent(MainActivity.this, DeviceListActivity.class),
                    REQUEST_CONNECT_DEVICE_SECURE);
        }
        if (id == R.id.action_bluetooth_insecure) {
            startActivityForResult(new Intent(MainActivity.this, DeviceListActivity.class),
                    REQUEST_CONNECT_DEVICE_INSECURE);
        }
        if (id == R.id.action_bluetooth_discoverable) {
            if (BluetoothAdapter.getDefaultAdapter().getScanMode() !=
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void setOnTouchListener() {
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
    }

}