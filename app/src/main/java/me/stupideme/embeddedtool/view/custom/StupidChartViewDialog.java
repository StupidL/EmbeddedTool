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
 * Created by StupidL on 2016/10/2.
 */

public class StupidChartViewDialog extends Dialog implements View.OnClickListener {

    /**
     * listener to listen change of attrs
     */
    private StupidChartDialogListener mListener;

    /**
     * edit text for max x
     */
    private EditText xMax;

    /**
     * edit text for max y
     */
    private EditText yMax;

    /**
     * edit text for min x
     */
    private EditText xMin;

    /**
     * edit text for min y
     */
    private EditText yMin;

    /**
     * color spinner
     */
    private Spinner mColorSpinner;

    /**
     * data type spinner
     */
    private Spinner mTypeSpinner;

    /**
     * data type position
     */
    private int mTypePos;

    /**
     * color position
     */
    private int colorPos;

    /**
     * constructor
     * @param context context
     * @param listener stupid chart dialog listener
     */
    public StupidChartViewDialog(Context context, StupidChartDialogListener listener) {
        super(context);
        mListener = listener;
        initView();
    }

    /**
     * init dialog view
     */
    private void initView() {
        //set layout
        setContentView(R.layout.stupid_chart_view_dialog);
        //find views bu id
        mTypeSpinner = (Spinner) findViewById(R.id.stupid_chart_view_dialog_spinner_type);
        mColorSpinner = (Spinner) findViewById(R.id.stupid_chart_view_dialog_spinner_color);
        xMax = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_x_max);
        yMax = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_y_max);
        xMin = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_x_min);
        yMin = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_y_min);
        Button cancel = (Button) findViewById(R.id.stupid_chart_view_dialog_button_cancel);
        Button ok = (Button) findViewById(R.id.stupid_chart_view_dialog_button_ok);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mTypePos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colorPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    /**
     * set color pos when dialog showing
     * @param pos position of color spinner
     */
    public void setColorPos(int pos) {
        mColorSpinner.setSelection(pos, true);
    }

    /**
     * set data type pos when dialog dismiss
     * @param pos pos of type spinner
     */
    public void setTypePos(int pos) {
        mTypeSpinner.setSelection(pos, true);
    }

    /**
     * show max X value when dialog showing
     * @param max max value
     */
    public void showMaxX(int max) {
        xMax.setText(max + "");
    }

    /**
     * show max Y value when dialog showing
     * @param max max value
     */
    public void showMaxY(int max) {
        yMax.setText(max + "");
    }

    /**
     * show min X value when dialog showing
     * @param min min value
     */
    public void showMinX(int min) {
        xMin.setText(min + "");
    }

    /**
     * show min Y value when dialog showing
     * @param min min value
     */
    public void showMinY(int min) {
        yMin.setText(min + "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stupid_chart_view_dialog_button_cancel:
                mListener.onCancel();
                break;
            case R.id.stupid_chart_view_dialog_button_ok:
                Map<String, String> map = new HashMap<>();
                map.put(Constants.KEY_TYPE_POS, mTypePos + "");
                map.put(Constants.KEY_COLOR_POS, colorPos + "");
                map.put(Constants.KEY_TYPE_STRING, (String) mTypeSpinner.getItemAtPosition(mTypePos));
                if (!xMax.getText().toString().isEmpty())
                    map.put(Constants.KEY_MAX_X, xMax.getText().toString());
                if (!yMax.getText().toString().isEmpty())
                    map.put(Constants.KEY_MAX_Y, yMax.getText().toString());
                if (!yMin.getText().toString().isEmpty())
                    map.put(Constants.KEY_MIN_Y, yMin.getText().toString());
                if (!xMin.getText().toString().isEmpty())
                    map.put(Constants.KEY_MIN_X, xMin.getText().toString());
                mListener.onSave(map);
                break;
        }
    }

    /**
     * stupid chart dialog listener for call back
     */
    public interface StupidChartDialogListener {

        /**
         * on cancel button clicked
         */
        void onCancel();

        /**
         * on save button clicked
         * @param map map that contains attrs for chart view from dialog
         */
        void onSave(Map<String, String> map);
    }
}
