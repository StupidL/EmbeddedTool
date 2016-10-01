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
import me.stupideme.embeddedtool.view.custom.StupidEditText;
import me.stupideme.embeddedtool.view.custom.StupidReceiveButton;
import me.stupideme.embeddedtool.view.custom.StupidSendButton;
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
        StupidSendButton stupidSendButton = new StupidSendButton(MainActivity.this, mPresenter);
        stupidSendButton.setText("Button" + viewIndex);
        stupidSendButton.setId(viewIndex++);
        stupidSendButton.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(stupidSendButton);
        Log.i("view id: ", viewIndex + "");
    }

    @Override
    public void addReceiveButton() {
        StupidReceiveButton button = new StupidReceiveButton(MainActivity.this, mPresenter);
        button.setText("Button" + viewIndex);
        button.setId(viewIndex++);
        button.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(button);
        Log.i("view id: ", viewIndex + "");
    }

    @Override
    public void removeButton(Button view) {
        mFrameLayout.removeView(view);
    }

    @Override
    public void addTextView() {
        StupidTextView stupidTextView = new StupidTextView(MainActivity.this, mPresenter);
        stupidTextView.setId(viewIndex++);
        stupidTextView.setText("id: " + (viewIndex - 1));
        stupidTextView.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(stupidTextView);
        Log.i("text view id ", viewIndex - 1 + "");
    }

    @Override
    public void removeTextView(TextView view) {
        mFrameLayout.removeView(view);
    }

    @Override
    public TextView findTextViewById(int id) {
        return (TextView) mFrameLayout.findViewById(id);
    }

    @Override
    public void addEditText() {
        StupidEditText editText = new StupidEditText(this, mPresenter);
        editText.setOnTouchListener(mTouchListener);
        mFrameLayout.addView(editText);
        Log.v("StupidEditText","edit text added");
    }

    @Override
    public void removeEditText(StupidEditText view) {
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
            public void onClick(View view) {mPresenter.addReceiveButton();
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