package me.stupideme.embeddedtool.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.view.fragment.DeviceNewFragment;
import me.stupideme.embeddedtool.view.fragment.DevicePairedFragment;

/**
 * A activity to show bluetooth devices and select one to connect
 */
public class DeviceListActivity extends AppCompatActivity {

    /**
     * device new fragment
     */
    private DeviceNewFragment mNewFragment;

    /**
     * device paired fragment
     */
    private DevicePairedFragment mPairedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        //init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("蓝牙设备");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeviceListActivity.super.onBackPressed();
            }
        });
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mNewFragment = DeviceNewFragment.newInstance();
        mPairedFragment = DevicePairedFragment.newInstance();

    }

    /**
     * adapter fro view pager
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return mPairedFragment;
            if (position == 1)
                return mNewFragment;
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "已配对设备";
                case 1:
                    return "未配对设备";
            }
            return null;
        }
    }
}
