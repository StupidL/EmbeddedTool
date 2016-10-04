package me.stupideme.embeddedtool.view.custom;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.MainPresenter;

/**
 * Created by StupidL on 2016/10/1.
 */

public class StupidEditText extends EditText implements StupidEditTextDialog.StupidEditTextDialogListener {

    private MainPresenter mPresenter;
    private StupidEditTextDialog mDialog;

    public StupidEditText(final Context context, MainPresenter presenter) {
        super(context);
        mPresenter = presenter;
        mDialog = new StupidEditTextDialog(context, this);

        setMaxLines(10);
        setLines(1);
        setTextColor(Color.WHITE);
        setBackgroundColor(getResources().getColor(R.color.Gray));
        setWidth(800);
        setHeight(100);
        setTextSize(18);
        setPadding(16, 16, 16, 16);
        setGravity(Gravity.NO_GRAVITY);
        setLayoutParams(new LinearLayoutCompat.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.showEditTextWidth(getWidth());
                mDialog.showEditTextHeight(getHeight());
                mDialog.showEditTextId(getId());
                mDialog.show();
                return true;
            }
        });

    }

    public StupidEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDelete() {
        mDialog.dismiss();
        mPresenter.removeEditText(this);
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void onSave(Map<String, String> map) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (map.containsKey("width")) {
            params.width = Integer.parseInt(map.get("width"));
            setLayoutParams(params);
        }
        if (map.containsKey("height")) {
            params.height = Integer.parseInt(map.get("height"));
            setLayoutParams(params);
        }
        if (map.containsKey("color")) {
            setBackgroundColor(getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]));
        }
        if (map.containsKey("id"))
            setId(Integer.parseInt(map.get("id")));
        mDialog.dismiss();
    }

    @Override
    public void bindEditTextById(int id) {
        if (0 == mPresenter.bindEditTextById(id, getId())) {
            Toast.makeText(getContext(), "编辑框只能绑定发送按钮", Toast.LENGTH_SHORT).show();
        }
    }

}
