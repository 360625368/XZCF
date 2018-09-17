package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xzcf.ui.fragments.BaseFragment;
import com.xzcf.ui.fragments.InfoTabFragment;

import java.util.ArrayList;
import java.util.List;

public class InfoFragmentAdapter extends FragmentPagerAdapter {

    List<BaseFragment> fragments = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    public InfoFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(InfoTabFragment.newInstance(InfoTabFragment.InfoType.ZHIBO));
        fragments.add(InfoTabFragment.newInstance(InfoTabFragment.InfoType.YIDONG));
        fragments.add(InfoTabFragment.newInstance(InfoTabFragment.InfoType.YAOWEN));
        fragments.add(InfoTabFragment.newInstance(InfoTabFragment.InfoType.TUIJIAN));
        fragments.add(InfoTabFragment.newInstance(InfoTabFragment.InfoType.SHIKUANG));

        titles.add("直播");
        titles.add("异动");
        titles.add("要闻");
        titles.add("推荐");
        titles.add("市况");
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }
}
