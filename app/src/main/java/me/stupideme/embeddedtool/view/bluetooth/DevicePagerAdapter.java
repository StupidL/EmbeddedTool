package me.stupideme.embeddedtool.view.bluetooth;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by StupidL on 2016/8/12.
 */

public class DevicePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private String[] tabTitles = new String[]{"已配对设备","未配对设备"};

    DevicePagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        fragmentList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position){
        return tabTitles[position];
    }

}
