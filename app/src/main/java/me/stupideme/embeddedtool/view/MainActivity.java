package me.stupideme.embeddedtool.view;

import android.os.Bundle;
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
import me.stupideme.embeddedtool.view.custom.StupidChartView;
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidButtonReceive;
import me.stupideme.embeddedtool.view.custom.StupidButtonSend;
import me.stupideme.embeddedtool.view.custom.StupidTextView;

public class MainActivity extends AppCompatActivity implements IMainView {

    private MainPresenter mPresenter;
    private FrameLayout mFrameLayout;

    private View.OnTouchListener mTouchListener;
    private static int viewIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);
        initView();

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
        return super.onOptionsItemSelected(item);
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

    @Override
    public void addChartView() {

    }

    @Override
    public void removeChartView(StupidChartView view) {
        mFrameLayout.removeView(view);
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