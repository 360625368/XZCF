package com.xzcf.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.ui.adapters.StockPagerAdapter;
import com.xzcf.ui.fragments.stock.CustomizeStockFragment;
import com.xzcf.ui.fragments.stock.EntrepreneurshipStockFragment;
import com.xzcf.ui.fragments.stock.ShanghaiStockFragment;
import com.xzcf.ui.fragments.stock.ShenzhenStockFragment;
import com.xzcf.ui.fragments.stock.SmallStockFragment;
import com.xzcf.utils.UiUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class StockFragment extends BaseFragment {


    @BindView(R.id.iBtnSearch)
    ImageButton iBtnSearch;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    Unbinder unbinder;
    @BindView(R.id.status_bar_bg)
    View statusBarBg;
    private Listener listener;

    public static StockFragment newInstance() {
        return new StockFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock, container, false);
        unbinder = ButterKnife.bind(this, view);
        statusBarBg.getLayoutParams().height = UiUtils.getStatusBarHeight(getContext());
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }


    private void initView() {
        tvTitle.setText(R.string.stock_title);
        iBtnSearch.setVisibility(View.VISIBLE);
        setTabContent();
    }

    // set viewpager
    private void setAdapter(final PagerAdapter adapter) {
        mViewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    // init stock Fragment list
    private void setTabContent() {
        ArrayList<SupportFragment> fragments = new ArrayList<>();
        fragments.add(CustomizeStockFragment.newInstance());
        fragments.add(ShenzhenStockFragment.newInstance());
        fragments.add(ShanghaiStockFragment.newInstance());
        fragments.add(EntrepreneurshipStockFragment.newInstance());
        fragments.add(SmallStockFragment.newInstance());

        String[] titles = {getString(R.string.customize_stock),
                getString(R.string.hs_stock),
                getString(R.string.sh_stock),
                getString(R.string.entrepreneurship_stock),
                getString(R.string.small_stock)};


        if (mViewPager.getAdapter() == null) {
            StockPagerAdapter adapter = new StockPagerAdapter(getChildFragmentManager());
            adapter.setFragmentPages(fragments);
            adapter.setPageTitles(titles);
            setAdapter(adapter);
        } else {
            StockPagerAdapter adapter = (StockPagerAdapter) mViewPager.getAdapter();
            adapter.setFragmentPages(fragments);
            adapter.setPageTitles(titles);
            mViewPager.setOffscreenPageLimit(adapter.getCount() - 1);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 搜索按钮
     */
    @OnClick(R.id.iBtnSearch)
    public void onIBtnSearchClicked() {
        if (listener != null) {
            listener.onSearch();
        }
    }


    public interface Listener {
        void onSearch();
    }

}
