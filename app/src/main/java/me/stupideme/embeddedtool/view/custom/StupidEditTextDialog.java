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

    /**
     * dialog listener, a call abck interface
     */
    private StupidEditTextDialogListener mListener;

    /**
     * a map to save attrs
     */
    private Map<String, String> map = new HashMap<>();

    /**
     * width edit text
     */
    private EditText width;

    /**
     * height edit text
     */
    private EditText height;

    /**
     * bind view id edit text
     */
    private EditText bindId;

    /**
     * id edit text
     */
    private EditText id;

    /**
     * mColorSpinner spinner
     */
    private Spinner mColorSpinner;

    /**
     * position of color spinner
     */
    private int mColorPos;

    /**
     * constructor
     *
     * @param context  context
     * @param listener listener
     */
    public StupidEditTextDialog(final Context context, StupidEditTextDialogListener listener) {
        super(context);
        setContentView(R.layout.stupid_edit_text_dialog);
        //init listener
        mListener = listener;
        //find views by id
        width = (EditText) findViewById(R.id.stupid_et_dialog_et_width);
        height = (EditText) findViewById(R.id.stupid_et_dialog_et_height);
        bindId = (EditText) findViewById(R.id.stupid_et_dialog_et_bind);
        id = (EditText) findViewById(R.id.stupid_et_dialog_et_id);
        mColorSpinner = (Spinner) findViewById(R.id.stupid_et_dialog_spinner_color);

        //set on item selected listener
        mColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //record position of spinner
                mColorPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //find views by id
        Button delete = (Button) findViewById(R.id.stupid_et_dialog_button_delete);
        Button cancel = (Button) findViewById(R.id.stupid_et_dialog_button_cancel);
        Button ok = (Button) findViewById(R.id.stupid_et_dialog_button_ok);
        //set on click listener
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    /**
     * show width in dialog
     *
     * @param w width
     */
    void showEditTextWidth(int w) {
        width.setText(w + "");
    }

    /**
     * show height in dialog
     *
     * @param h height
     */
    void showEditTextHeight(int h) {
        height.setText(h + "");
    }

    /**
     * show id in dialog
     *
     * @param i id
     */
    void showEditTextId(int i) {
        id.setText(i + "");
    }

    /**
     * set position of color spinner
     *
     * @param i position
     */
    void showSpinnerColor(int i) {
        mColorSpinner.setSelection(i, true);
    }

    /**
     * show bind view id in dialog
     *
     * @param id id of bind view
     */
    void showBindViewId(int id) {
        bindId.setText(id + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //button delete clicked
            case R.id.stupid_et_dialog_button_delete:
                mListener.onDelete();
                break;
            //button cancel clicked
            case R.id.stupid_et_dialog_button_cancel:
                mListener.onCancel();
                break;
            //button ok clicked
            case R.id.stupid_et_dialog_button_ok:
                if (!width.getText().toString().isEmpty()) {
                    //save width
                    map.put(Constants.KEY_WIDTH, width.getText().toString());
                }
                if (!height.getText().toString().isEmpty()) {
                    //save height
                    map.put(Constants.KEY_HEIGHT, height.getText().toString());
                }
                if (!bindId.getText().toString().isEmpty()) {
                    //save bind view id
                    map.put(Constants.KEY_BIND_VIEW_ID, bindId.getText().toString());
                }
                if (!id.getText().toString().isEmpty()) {
                    //save id
                    map.put(Constants.KEY_ID, id.getText().toString());
                }
                //save color position
                map.put(Constants.KEY_COLOR_POS, mColorPos + "");
                //save to listener
                mListener.onSave(map);
                break;
        }
    }

    /**
     * a callback interface for save attrs
     */
    interface StupidEditTextDialogListener {

        /**
         * delete edit text itself
         */
        void onDelete();

        /**
         * dismiss dialog
         */
        void onCancel();

        /**
         * transfer attrs to listener
         * @param map a map contains attrs
         */
        void onSave(Map<String, String> map);

    }
}
