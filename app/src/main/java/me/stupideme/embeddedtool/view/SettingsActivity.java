package me.stupideme.embeddedtool.view;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.SettingsPresenter;
import me.stupideme.embeddedtool.view.fragment.DataProtocolFragment;
import me.stupideme.embeddedtool.view.fragment.DataTypeFragment;

public class SettingsActivity extends AppCompatActivity implements ISettingsView,
        DataProtocolFragment.OnDataProtocolChangedListener, DataTypeFragment.OnDataTypeChangedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public SettingsPresenter mPresenter;
    private DataProtocolFragment mProtocolFragment;
    private DataTypeFragment mTypeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("高级设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsActivity.super.onBackPressed();
            }
        });

        mPresenter = new SettingsPresenter(SettingsActivity.this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mProtocolFragment = DataProtocolFragment.newInstance();
        mTypeFragment = DataTypeFragment.newInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_advanced_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings_advanced) {
            return true;
        }
        if (id == R.id.action_recovery_advanced) {
            mPresenter.recoveryDefault();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveProtocol(Map<String, String> map) {
        mPresenter.saveDataProtocol(map);
    }

    @Override
    public List<Map<String, String>> loadAllProtocol() {
        return mPresenter.getDataProtocol();
    }

    @Override
    public void addDataType(Map<String, String> map) {
        mPresenter.addDataType(map);
    }

    @Override
    public void removeDataType(String name) {
        mPresenter.removeDataType(name);
    }

    @Override
    public List<Map<String, String>> loadAllTypes() {
        return mPresenter.getAllDataType();
    }

    @Override
    public void recoveryDefault(List<Map<String, String>> list) {
        mTypeFragment.recoveryDefault(list);
        mProtocolFragment.recoveryDefault(list);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return mTypeFragment;
            if (position == 1)
                return mProtocolFragment;
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
                    return "数据类型";
                case 1:
                    return "数据协议";
            }
            return null;
        }
    }
}
