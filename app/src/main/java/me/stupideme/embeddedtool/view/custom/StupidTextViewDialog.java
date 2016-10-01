package me.stupideme.embeddedtool.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.Map;

import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.ViewType;

/**
 * Created by StupidL on 2016/10/1.
 */

public class StupidTextViewDialog extends Dialog implements View.OnClickListener {

    private StupidTextViewListener mListener;
    private EditText id;
    private EditText width;
    private EditText height;

    private Map<String, String> map = new HashMap<>();

    private ViewType[] mTextViewTypes = {ViewType.TEXT_0, ViewType.TEXT_1,
            ViewType.TEXT_2, ViewType.TEXT_3, ViewType.TEXT_4};

    public StupidTextViewDialog(Context context, StupidTextViewListener listener) {
        super(context);
        mListener = listener;
        setContentView(R.layout.stupid_text_view_dialog);
        id = (EditText) findViewById(R.id.stupid_text_view_dialog_et_id);
        width = (EditText) findViewById(R.id.stupid_text_view_dialog_et_width);
        height = (EditText) findViewById(R.id.stupid_text_view_dialog_et_height);
        Spinner type = (Spinner) findViewById(R.id.stupid_text_view_dialog_spinner_type);
        Button delete = (Button) findViewById(R.id.stupid_text_view_dialog_button_delete);
        Button cancel = (Button) findViewById(R.id.stupid_text_view_dialog_button_cancel);
        Button ok = (Button) findViewById(R.id.stupid_text_view_dialog_button_ok);

        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.setViewType(mTextViewTypes[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mListener.setViewType(mTextViewTypes[0]);
            }
        });
    }

    public void showTextViewId(int viewId) {
        id.setText(viewId + "");
    }

    public void showTextViewWidth(int w) {
        width.setText(w + "");
    }

    public void showTextViewHeight(int h) {
        height.setText(h + "");
    }

    public StupidTextViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stupid_text_view_dialog_button_delete:
                mListener.onDelete();
                break;
            case R.id.stupid_text_view_dialog_button_cancel:
                mListener.onCancel();
                break;
            case R.id.stupid_text_view_dialog_button_ok:
                if (!id.getText().toString().isEmpty()) {
                    map.put("id", id.getText().toString());
                }
                if (!width.getText().toString().isEmpty())
                    map.put("width", width.getText().toString());
                if (!height.getText().toString().isEmpty())
                    map.put("height", height.getText().toString());
                mListener.onSave(map);
                break;
        }
    }

    interface StupidTextViewListener {
        void setViewType(ViewType type);

        void onDelete();

        void onCancel();

        void onSave(Map<String, String> map);

        void setHeight(int height);

    }
}
