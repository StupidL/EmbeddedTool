package me.stupideme.embeddedtool.view.activity;

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

import java.util.Map;

import me.stupideme.embeddedtool.Constants;
import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.ChartPresenter;
import me.stupideme.embeddedtool.view.custom.OnSendMessageListener;
import me.stupideme.embeddedtool.view.custom.StupidChartViewDialog;
import me.stupideme.embeddedtool.view.interfaces.IChartView;

/**
 * Created by StupidL on 2016/10/3.
 */

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener,
        StupidChartViewDialog.StupidChartDialogListener, IChartView {

    //debug
    private static final java.lang.String TAG = ChartActivity.class.getSimpleName();

    /**
     * settings dialog
     */
    private StupidChartViewDialog mDialog;

    /**
     * a image button to control receive data or not
     */
    private ImageButton mButton;

    /**
     * is image button running
     */
    private boolean isPlaying = true;

    /**
     * background of chart view
     */
    private FrameLayout mFrameLayout;

    /**
     * presenter for chart view
     */
    private ChartPresenter mPresenter;

    /**
     * send message listener
     */
    private OnSendMessageListener mListener;

    /**
     * color pos in dialog
     */
    private int mColorPos = 0;

    /**
     * type pos in dialog
     */
    private int mTypePos = 0;

    /**
     * name of data type
     */
    private String mDataType;

    /**
     * Linear type of chart
     */
    private LineChart mChart;

    /**
     * x axis
     */
    private XAxis xAxis;

    /**
     * y axis
     */
    private YAxis yAxis;

    /**
     * max x axis value
     */
    private int xMax = 500;

    /**
     * min x axis value
     */
    private int xMin = 0;

    /**
     * max y axis value
     */
    private int yMax = 100;

    /**
     * min y axis value
     */
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
                    if (getDataType() != null)
                        mListener.onSendMessage(Constants.REQUEST_CODE_CHART,
                                getDataType(),
                                Constants.MESSAGE_BODY_EMPTY);
                    else
                        Toast.makeText(ChartActivity.this, "请设置数据类型～", Toast.LENGTH_SHORT).show();
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
    public void receiveMessage(java.lang.String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }

    /**
     * set data type for chart
     *
     * @param type name of type
     */
    public void setDataType(String type) {
        mDataType = type;
    }

    /**
     * get data type of chart
     *
     * @return name of type
     */
    public String getDataType() {
        return mDataType;
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachObserver(this);
    }

    /**
     * init linear chart
     */
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
        //dismiss dialog
        mDialog.dismiss();

        //set color for chart view and frame layout
        if (map.containsKey(Constants.KEY_COLOR_POS)) {
            mColorPos = Integer.parseInt(map.get(Constants.KEY_COLOR_POS));
            int color = getResources().getColor(Constants.mColors[mColorPos]);
            mChart.setBackgroundColor(color);
            mFrameLayout.setBackgroundColor(color);
        }
        //set pos of data type for chart view
        if (map.containsKey(Constants.KEY_TYPE_POS)) {
            mTypePos = Integer.parseInt(map.get(Constants.KEY_TYPE_POS));
        }
        //set data type for chart view
        if (map.containsKey(Constants.KEY_TYPE_STRING)) {
            setDataType(map.get(Constants.KEY_TYPE_STRING));
        }
        //set max X value
        if (map.containsKey(Constants.KEY_MAX_X)) {
            xMax = Integer.parseInt(map.get(Constants.KEY_MAX_X));
            xAxis.setAxisMaxValue(xMax);
        }
        //set max Y value
        if (map.containsKey(Constants.KEY_MAX_Y)) {
            yMax = Integer.parseInt(map.get(Constants.KEY_MAX_Y));
            yAxis.setAxisMaxValue(yMax);
        }
        //set min X value
        if (map.containsKey(Constants.KEY_MIN_X)) {
            xMin = Integer.parseInt(map.get(Constants.KEY_MIN_X));
            xAxis.setAxisMinValue(xMin);
        }
        //set min Y value
        if (map.containsKey(Constants.KEY_MIN_Y)) {
            yMin = Integer.parseInt(map.get(Constants.KEY_MIN_Y));
            yAxis.setAxisMinValue(yMin);
        }
        //notify
        mChart.notifyDataSetChanged();

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

    /**
     * add an random entry
     */
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

    /**
     * create linear type data set
     *
     * @return set
     */
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
        Toast.makeText(ChartActivity.this,
                "X = " + e.getX() + " Y = " + e.getY(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

}
