package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/9/28.
 */

public class StupidButtonSend extends StupidButton implements StupidButtonDialog.StupidButtonDialogListener {

    private MainPresenter mPresenter;
    private StupidButtonDialog mDialog;

    public StupidButtonSend(final Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
        mDialog = new StupidButtonDialog(context, this);

        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(200);
        setHeight(100);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showButtonName(getText().toString());
                mDialog.showButtonWidth(getWidth());
                mDialog.showButtonHeight(getHeight());
                mDialog.showButtonId(getId());
                mDialog.show();
                return false;
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getDataType() != null) {
                    if (mBindEditText != null) {
                        mPresenter.sendDataOverButton(getDataType(), mBindEditText.getText().toString());
                        if (mBindTextView != null) {
                            mBindTextView.append("\n" + mBindEditText.getText().toString());
                        }
                        Toast.makeText(getContext(), "数据发送成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "请设置一个编辑框并且输入要发送的信息", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "请先设置要操作的类型", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public String toString() {
        return "I am Stupid Send Button";
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        mBindTextView = null;
        mBindEditText = null;
        mPresenter.removeButton(this);
        Log.v("StupidSendButton ", "button removed");
    }

    @Override
    public void onSave(Map<String, String> map) {
        mDialog.dismiss();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey("name"))
            setText(map.get("name"));
        if (map.containsKey("width")) {
            params.width = Integer.parseInt(map.get("width"));
            setLayoutParams(params);
        }
        if (map.containsKey("height")) {
            params.height = Integer.parseInt(map.get("height"));
            setLayoutParams(params);
        }
        if (map.containsKey("id")) {
            setId(Integer.parseInt(map.get("id")));
        }
        if(map.containsKey("type")){
            setDataType(Constants.mButtonTypes[Integer.parseInt(map.get("type"))]);
        }
        setBackgroundColor(getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]));

    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
        Log.v("StupidSendButton ", "dialog canceled");
    }

}
