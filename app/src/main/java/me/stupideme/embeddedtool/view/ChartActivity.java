package me.stupideme.embeddedtool.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.DataType;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.ChartPresenter;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;
import me.stupideme.embeddedtool.view.custom.StupidChartViewDialog;

/**
 * Created by StupidL on 2016/10/3.
 */

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener,
        StupidChartViewDialog.StupidChartDialogListener, IChartView {

    private static final String TAG = ChartActivity.class.getSimpleName();
    private StupidChartViewDialog mDialog;
    private ImageButton mButton;
    private boolean isPlaying = true;
    private FrameLayout mFrameLayout;
    private ChartPresenter mPresenter;
    private OnSendMessageListener mListener;
    private int mColorPos = 0;
    private int mTypePos = 0;
    private DataType mDataType;

    private LineChart mChart;
    private XAxis xAxis;
    private YAxis yAxis;
    private int xMax = 500;
    private int xMin = 0;
    private int yMax = 100;
    private int yMin = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_linechart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("图表工具");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChartActivity.super.onBackPressed();
            }
        });

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        mButton = (ImageButton) findViewById(R.id.control_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    mButton.setImageResource(R.drawable.ic_pause_circle_filled_teal_500_48dp);
                    addEntry();
                } else {
                    mButton.setImageResource(R.drawable.ic_play_circle_filled_teal_500_48dp);
                }
                isPlaying = !isPlaying;
            }
        });
        mDialog = new StupidChartViewDialog(this, this);
        initChart();

        mChart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mDialog.setColorPos(mColorPos);
                mDialog.setTypePos(mTypePos);
                mDialog.showMaxX(xMax);
                mDialog.showMinX(xMin);
                mDialog.showMaxY(yMax);
                mDialog.showMinY(yMin);
                mDialog.show();
                return false;
            }
        });

        mPresenter = new ChartPresenter(this);
        mPresenter.setSendMessageListener();
        mPresenter.attachObserver(this);
    }

    @Override
    public void setOnSendMessageListener(OnSendMessageListener listener) {
        mListener = listener;
    }

    @Override
    public void receiveMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachObserver(this);
    }

    void initChart() {
        mChart = (LineChart) findViewById(R.id.line_chart);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.GRAY);

        mFrameLayout.setBackgroundColor(Color.GRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        mChart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        xAxis = mChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGridLineWidth(2f);
        xAxis.setDrawGridLines(false);
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setEnabled(true);

        yAxis = mChart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setGridLineWidth(2f);
        yAxis.setAxisMaxValue(100f);
        yAxis.setAxisMinValue(0f);
        yAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
    }

    @Override
    public void onCancel() {
        mDialog.dismiss();
    }

    @Override
    public void onSave(Map<String, String> map) {
        mDialog.dismiss();
        int color = getResources().getColor(Constants.mColors[Integer.parseInt(map.get("color"))]);
        mChart.setBackgroundColor(color);
        mFrameLayout.setBackgroundColor(color);
        if (map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
        }
        if (map.containsKey(Constants.KEY_TYPE_POS)) {
            mTypePos = Integer.parseInt(map.get(Constants.KEY_TYPE_POS));
            mDataType = Constants.mDataTypes[mTypePos];
        }
        if (map.containsKey(Constants.KEY_MAX_X)) {
            xMax = Integer.parseInt(map.get(Constants.KEY_MAX_X));
            xAxis.setAxisMaxValue(xMax);
        }
        if (map.containsKey(Constants.KEY_MAX_Y)) {
            yMax = Integer.parseInt(map.get(Constants.KEY_MAX_Y));
            yAxis.setAxisMaxValue(yMax);
        }
        if (map.containsKey(Constants.KEY_MIN_X)) {
            xMin = Integer.parseInt(map.get(Constants.KEY_MIN_X));
            xAxis.setAxisMinValue(xMin);
        }
        if (map.containsKey(Constants.KEY_MIN_Y)) {
            yMin = Integer.parseInt(map.get(Constants.KEY_MIN_Y));
            yAxis.setAxisMinValue(yMin);
        }
        mChart.notifyDataSetChanged();

        mListener.onSendMessage(Constants.REQUEST_CODE_CHART, mDataType,
                String.valueOf(Constants.MESSAGE_BODY_EMPTY));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.realtime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionAdd: {
                addEntry();
                break;
            }
            case R.id.actionClear: {
                mChart.clearValues();
                Toast.makeText(this, "Chart cleared!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.actionFeedMultiple: {
                feedMultiple();
                break;
            }
        }
        return true;
    }

    private void addEntry() {

        LineData data = mChart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 40) + 30f), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            mChart.notifyDataSetChanged();

            // limit the number of visible entries
            mChart.setVisibleXRangeMaximum(120);
            // mChart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            mChart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // mChart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(6f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        set.setDrawValues(false);
        return set;
    }

    private Thread thread;

    private void feedMultiple() {

        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                addEntry();
            }
        };

        thread = new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {

                    // Don't generate garbage runnables inside the loop.
                    runOnUiThread(runnable);

                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
    }

}
