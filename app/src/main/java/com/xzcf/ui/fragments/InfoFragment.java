package com.xzcf.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.ui.adapters.InfoFragmentAdapter;
import com.xzcf.utils.UiUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends BaseFragment {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.status_bar_bg)
    View statusBarBg;
    private InfoFragmentAdapter fragmentAdapter;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        unbinder = ButterKnife.bind(this, view);
        statusBarBg.getLayoutParams().height = UiUtils.getStatusBarHeight(getContext());
        initView();
        return view;
    }

    private void initView() {
        tvTitle.setText(R.string.info_title);

        tabLayout.setupWithViewPager(viewPager);
        fragmentAdapter = new InfoFragmentAdapter(getChildFragmentManager());
        viewPager.setOffscreenPageLimit(fragmentAdapter.getCount() - 1);
        viewPager.setAdapter(fragmentAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
