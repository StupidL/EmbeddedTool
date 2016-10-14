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

    private StupidChartDialogListener mListener;
    private EditText xMax;
    private EditText yMax;
    private EditText xMin;
    private EditText yMin;
    private int typePos;
    private int colorPos;

    public StupidChartViewDialog(Context context, StupidChartDialogListener listener) {
        super(context);
        mListener = listener;
        initView();
    }

    private void initView() {
        setContentView(R.layout.stupid_chart_view_dialog);
        Spinner mType = (Spinner) findViewById(R.id.stupid_chart_view_dialog_spinner_type);
        Spinner mColor = (Spinner) findViewById(R.id.stupid_chart_view_dialog_spinner_color);
        xMax = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_x_max);
        yMax = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_y_max);
        xMin = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_x_min);
        yMin = (EditText) findViewById(R.id.stupid_chart_view_dialog_et_y_min);
        Button cancel = (Button) findViewById(R.id.stupid_chart_view_dialog_button_cancel);
        Button ok = (Button) findViewById(R.id.stupid_chart_view_dialog_button_ok);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                typePos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                colorPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.stupid_chart_view_dialog_button_cancel:
                mListener.onCancel();
                break;
            case R.id.stupid_chart_view_dialog_button_ok:
                Map<String, String> map = new HashMap<>();
                map.put(Constants.KEY_TYPE_POS, typePos + "");
                map.put(Constants.KEY_COLOR_POS, colorPos + "");
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

    public interface StupidChartDialogListener {

        void onCancel();

        void onSave(Map<String, String> map);
    }
}
