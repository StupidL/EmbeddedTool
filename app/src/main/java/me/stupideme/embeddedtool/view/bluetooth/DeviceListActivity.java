package me.stupideme.embeddedtool.view.bluetooth;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.view.custom.SlidingTabLayout;


public class DeviceListActivity extends AppCompatActivity {

    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_list);

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceListActivity.super.onBackPressed();
            }
        });

        List<Fragment> list = new ArrayList<>();
        list.add(DevicePairedFragment.newInstance());
        list.add(DeviceNewFragment.newInstance());

        DevicePagerAdapter adapter = new DevicePagerAdapter(getSupportFragmentManager(), list);

        SlidingTabLayout slidingTabs = (SlidingTabLayout) findViewById(R.id.sliding_tab_layout);
        slidingTabs.setBackgroundColor(getResources().getColor(R.color.primary));
        slidingTabs.setSelectedIndicatorColors(Color.WHITE);
        slidingTabs.setCustomTabView(R.layout.item_sliding_tab, 0);
        slidingTabs.setDividerColors(getResources().getColor(R.color.primary));

        ViewPager pager = (ViewPager) findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(2);
        pager.setAdapter(adapter);
        slidingTabs.setViewPager(pager);
    }

}

