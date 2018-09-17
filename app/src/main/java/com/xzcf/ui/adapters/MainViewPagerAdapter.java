package com.xzcf.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xzcf.ui.fragments.BaseFragment;
import com.xzcf.ui.fragments.HomeFragment;
import com.xzcf.ui.fragments.InfoFragment;
import com.xzcf.ui.fragments.MineFragment;
import com.xzcf.ui.fragments.StockFragment;

import java.util.ArrayList;
import java.util.List;


public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    List<BaseFragment> fragments = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(HomeFragment.newInstance());
        fragments.add(StockFragment.newInstance());
        fragments.add(InfoFragment.newInstance());
        fragments.add(MineFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
