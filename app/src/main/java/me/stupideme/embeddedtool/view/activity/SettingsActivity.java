package me.stupideme.embeddedtool.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.List;
import java.util.Map;

import me.stupideme.embeddedtool.R;
import me.stupideme.embeddedtool.presenter.SettingsPresenter;
import me.stupideme.embeddedtool.view.fragment.DataProtocolFragment;
import me.stupideme.embeddedtool.view.fragment.DataTypeFragment;
import me.stupideme.embeddedtool.view.interfaces.ISettingsView;

/**
 * Created by StupidL on 2016/9/30.
 * advanced settings activity where you can custom data type and protocol
 */
public class SettingsActivity extends AppCompatActivity implements ISettingsView,
        DataProtocolFragment.OnDataProtocolChangedListener, DataTypeFragment.OnDataTypeChangedListener {

    //debug
    private static final String TAG = SettingsActivity.class.getSimpleName();

    /**
     * adapter of view pager
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * view pager
     */
    private ViewPager mViewPager;

    /**
     * the presenter
     */
    public SettingsPresenter mPresenter;

    /**
     * data protocol fragment
     */
    private DataProtocolFragment mProtocolFragment;

    /**
     * data type fragment
     */
    private DataTypeFragment mTypeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //init toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("高级设置");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_OK);
                SettingsActivity.super.onBackPressed();
            }
        });

        //init presenter
        mPresenter = new SettingsPresenter(SettingsActivity.this);

        //init adapter
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //init tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //init fragments
        mProtocolFragment = DataProtocolFragment.newInstance();
        mProtocolFragment.setOnDataProtocolChangedListener(this);
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
            //do nothing
            return true;
        }
        if (id == R.id.action_recovery_advanced) {
            //recovery default data types and protocol
            mPresenter.recoveryTypeDefault();
            mPresenter.recoveryProtocolDefault();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }

    /**
     * add a custom data type
     * @param map a map contains the name of type and code of type
     */
    @Override
    public void addDataType(Map<String, String> map) {
        mPresenter.addDataType(map);
    }

    /**
     * remove a custom data type
     * @param name name of the data type
     */
    @Override
    public void removeDataType(String name) {
        mPresenter.removeDataType(name);
    }

    /**
     * save custom protocol
     * @param map a map contains header and tail of protocol
     */
    @Override
    public void onSaveProtocol(Map<String, String> map) {
        mPresenter.saveDataProtocol(map);
    }

    /**
     * get data protocol
     * @return a list contains protocols
     */
    @Override
    public List<Map<String, String>> getDataProtocol() {
        return mPresenter.getDataProtocol();
    }

    /**
     * get data type
     * @return a list contains data types
     */
    @Override
    public List<Map<String, String>> getDataType() {
        return mPresenter.getDataType();
    }

    /**
     * recovery default data type
     * @param list a list contains default types
     */
    @Override
    public void recoveryTypeDefault(List<Map<String, String>> list) {
        mTypeFragment.recoveryDefault(list);
    }

    /**
     * recovery default protocol
     * @param list a list contains default protocol
     */
    @Override
    public void recoveryProtocolDefault(List<Map<String, String>> list) {
        mProtocolFragment.recoveryDefault(list);
    }

    /**
     * a adapter of view pager
     */
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
