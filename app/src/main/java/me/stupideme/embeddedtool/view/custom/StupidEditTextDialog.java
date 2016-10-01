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

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/10/1.
 */

public class StupidEditTextDialog extends Dialog implements View.OnClickListener {

    private StupidEditTextDialogListener mListener;
    private Map<String, String> map = new HashMap<>();
    private EditText width;
    private EditText height;

    public StupidEditTextDialog(final Context context, StupidEditTextDialogListener listener) {
        super(context);
        setContentView(R.layout.stupid_edit_text_dialog);
        mListener = listener;
        width = (EditText) findViewById(R.id.stupid_et_dialog_et_width);
        height = (EditText) findViewById(R.id.stupid_et_dialog_et_height);
        Spinner type = (Spinner) findViewById(R.id.stupid_et_dialog_spinner_type);
        final Spinner color = (Spinner) findViewById(R.id.stupid_et_dialog_spinner_color);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.setViewType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mListener.setViewType(0);
            }
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.setBgColor(context.getResources().getColor(Constants.mColors[i]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mListener.setBgColor(context.getResources().getColor(Constants.mColors[0]));
            }
        });

        Button delete = (Button) findViewById(R.id.stupid_et_dialog_button_delete);
        Button cancel = (Button) findViewById(R.id.stupid_et_dialog_button_cancel);
        Button ok = (Button) findViewById(R.id.stupid_et_dialog_button_ok);

        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    public StupidEditTextDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public void showEditTextWidth(int w) {
        width.setText(w + "");
    }

    public void showEditTextHeight(int h) {
        height.setText(h + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stupid_et_dialog_button_delete:
                mListener.onDelete();
                break;
            case R.id.stupid_et_dialog_button_cancel:
                mListener.onCancel();
                break;
            case R.id.stupid_et_dialog_button_ok:
                if (!width.getText().toString().isEmpty())
                    map.put("width", width.getText().toString());
                if (!height.getText().toString().isEmpty())
                    map.put("height", height.getText().toString());
                mListener.onSave(map);
                break;
        }
    }

    interface StupidEditTextDialogListener {
        void setViewType(int i);

        void onDelete();

        void onCancel();

        void onSave(Map<String, String> map);

        void setBgColor(int color);
    }
}
