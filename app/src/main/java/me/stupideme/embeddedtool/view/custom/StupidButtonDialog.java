package me.stupideme.embeddedtool.view.custom;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import me.stupideme.embeddedtool.R;

/**
 * Created by StupidL on 2016/9/29.
 */

public class StupidButtonDialog extends Dialog implements View.OnClickListener {

    private StupidButtonDialogListener mListener;

    EditText editText;
    EditText width;
    EditText height;
    EditText bindId;
    Spinner type;
    Button cancel;
    Button ok;
    Button delete;
    private Map<String, String> map = new HashMap<>();

    public StupidButtonDialog(Context context, StupidButtonDialogListener listener) {
        super(context);
        mListener = listener;
        setContentView(R.layout.stupid_button_dialog);
        editText = (EditText) findViewById(R.id.stupid_button_dialog_name);
        type = (Spinner) findViewById(R.id.stupid_button_dialog_spinner);
        cancel = (Button) findViewById(R.id.stupid_button_dialog_cancel);
        ok = (Button) findViewById(R.id.stupid_button_dialog_ok);
        delete = (Button) findViewById(R.id.stupid_button_dialog_delete);
        bindId = (EditText) findViewById(R.id.stupid_button_dialog_bind_et);
        width = (EditText) findViewById(R.id.stupid_button_dialog_width_et);
        height = (EditText) findViewById(R.id.stupid_button_dialog_height_et);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mListener.onSetViewType(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mListener.onSetViewType(0);
            }
        });

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    /**
     * set text for the button.
     *
     * @param name name of the button
     */
    void showButtonName(String name) {
        editText.setText(name);
    }

    void showButtonWidth(int w) {
        width.setText(w + "");
    }

    void showButtonHeight(int h) {
        height.setText(h + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stupid_button_dialog_cancel:
                mListener.onCancel();
                break;
            case R.id.stupid_button_dialog_ok:
                if (!editText.getText().toString().isEmpty())
                    map.put("name", editText.getText().toString());
                if (!width.getText().toString().isEmpty())
                    map.put("width", width.getText().toString());
                if (!height.getText().toString().isEmpty())
                    map.put("height", height.getText().toString());
                String id = bindId.getText().toString();
                if (!id.isEmpty()) {
                    mListener.onBindTextView(Integer.parseInt(id));
                }
                mListener.onSave(map);
                break;
            case R.id.stupid_button_dialog_delete:
                mListener.onDelete();
                break;
        }
    }

    interface StupidButtonDialogListener {

        /**
         * callback methos. set a view type to button
         *
         * @param type the index of ViewType array, so we can find out the button's type
         */
        void onSetViewType(int type);

        /**
         * callback method. It will be called when the "delete" button in the dialog clicked.
         */
        void onDelete();

        /**
         * callback method. It will called when the "OK" button clicked in the dialog.
         *
         * @param map
         */
        void onSave(Map<String, String> map);

        /**
         * callback method. It will called when the "CANCEL" button clicked in the dialog.
         */
        void onCancel();

        /**
         * callback method. It will called when the "OK" button clicked in the dialog.
         * the purpose of this method is to save the id which the button want to bind.
         *
         * @param id id of the text view which this button want to bind.
         */
        void onBindTextView(int id);
    }
}
