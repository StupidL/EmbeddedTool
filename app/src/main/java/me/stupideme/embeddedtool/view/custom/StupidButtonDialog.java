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
 * Created by StupidL on 2016/9/29.
 */

public class StupidButtonDialog extends Dialog implements View.OnClickListener {

    /**
     * listener to listen attrs' changes
     */
    private StupidButtonDialogListener mListener;

    /**
     * edit name of this button
     */
    private EditText mEditText;

    /**
     * edit width of this button
     */
    private EditText mWidth;

    /**
     * edit height of this button
     */
    private EditText mHeight;

    /**
     * edit id of this button
     */
    private EditText mId;

    private Spinner mColorSpinner;

    private Spinner mTypeSpinner;

    /**
     * save color of this button
     */
    private int mColorPos;

    /**
     * save attrs of this button to a map
     */
    private Map<String, String> map = new HashMap<>();

    /**
     * save data type of this button
     */
    private int mDataType;

    StupidButtonDialog(final Context context, StupidButtonDialogListener listener) {
        super(context);
        //set listener
        mListener = listener;
        setContentView(R.layout.stupid_button_dialog);
        //find views
        mEditText = (EditText) findViewById(R.id.stupid_button_dialog_name);
        mTypeSpinner = (Spinner) findViewById(R.id.stupid_button_dialog_spinner);
        mColorSpinner = (Spinner) findViewById(R.id.stupid_button_dialog_spinner_color);
        Button cancel = (Button) findViewById(R.id.stupid_button_dialog_cancel);
        Button ok = (Button) findViewById(R.id.stupid_button_dialog_ok);
        Button delete = (Button) findViewById(R.id.stupid_button_dialog_delete);
        mId = (EditText) findViewById(R.id.stupid_button_dialog_bind_et);
        mWidth = (EditText) findViewById(R.id.stupid_button_dialog_width_et);
        mHeight = (EditText) findViewById(R.id.stupid_button_dialog_height_et);

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDataType = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mColorPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mDataType = 0;
            }
        });

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        delete.setOnClickListener(this);

    }

    /**
     * set name for the button in dialog
     *
     * @param name name of the button
     */
    void showButtonName(String name) {
        mEditText.setText(name);
    }

    /**
     * set width for the button in dialog
     * @param w width of the button
     */
    void showButtonWidth(int w) {
        mWidth.setText(w + "");
    }

    /**
     * set height for the button in dialog
     * @param h height of the button
     */
    void showButtonHeight(int h) {
        mHeight.setText(h + "");
    }

    /**
     * set button id in dialog
     * @param i id of the button
     */
    void showButtonId(int i) {
        mId.setText(i + "");
    }

    /**
     * set color for button in spinner
     * @param i position
     */
    void showSpinnerColor(int i) {
        mColorSpinner.setSelection(i, true);
    }

    /**
     * set data type for button in spinner
     * @param i position
     */
    void showSpinnerType(int i){
        mTypeSpinner.setSelection(i,true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stupid_button_dialog_cancel:
                mListener.onCancel();
                break;
            case R.id.stupid_button_dialog_ok:
                if (!mEditText.getText().toString().isEmpty())
                    map.put(Constants.KEY_NAME, mEditText.getText().toString());
                if (!mWidth.getText().toString().isEmpty())
                    map.put(Constants.KEY_WIDTH, mWidth.getText().toString());
                if (!mHeight.getText().toString().isEmpty())
                    map.put(Constants.KEY_HEIGHT, mHeight.getText().toString());
                if (!mId.getText().toString().isEmpty())
                    map.put(Constants.KEY_ID, mId.getText().toString());
                map.put(Constants.KEY_COLOR_POS, mColorPos + "");
                map.put(Constants.KEY_TYPE_POS, mDataType + "");
                mListener.onSave(map);
                break;
            case R.id.stupid_button_dialog_delete:
                mListener.onDelete();
                break;
        }
    }

    interface StupidButtonDialogListener {

        /**
         * callback method. It will be called when the "delete" button in the dialog clicked.
         */
        void onDelete();

        /**
         * callback method. It will called when the "OK" button clicked in the dialog.
         * To save attrs for button
         *
         * @param map
         */
        void onSave(Map<String, String> map);

        /**
         * callback method. It will called when the "CANCEL" button clicked in the dialog.
         */
        void onCancel();
    }
}
