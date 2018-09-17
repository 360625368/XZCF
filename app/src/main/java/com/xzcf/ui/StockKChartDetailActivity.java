package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.data.data.response.IntentBean;
import com.xzcf.data.data.response.StockMarketTitleResponse;
import com.xzcf.ui.adapters.StockPagerAdapter;
import com.xzcf.ui.fragments.stock.KDayChartFragment;
import com.xzcf.ui.fragments.stock.KFiveChartFragment;
import com.xzcf.ui.fragments.stock.KMinuteChartFragment;
import com.xzcf.ui.fragments.stock.KMonthChartFragment;
import com.xzcf.ui.fragments.stock.KWeekChartFragment;
import com.xzcf.ui.view.NoTouchScrollViewpager;
import com.xzcf.utils.MathUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.yokeyword.fragmentation.SupportFragment;


public class StockKChartDetailActivity extends BaseActivity {

    public static final String INTENT_BEAN_FLAG = "INTENT_BEAN_FLAG";
    public static final String INTENT_SELECT_FLAG = "INTENT_SELECT_FLAG";
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    NoTouchScrollViewpager viewPager;
    @BindView(R.id.tv_stock_name)
    TextView tvStockName;
    @BindView(R.id.tv_stock_code)
    TextView tvStockCode;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_diefu)
    TextView tvDiefu;
    @BindView(R.id.tv_dielv)
    TextView tvDielv;
    @BindView(R.id.tv_yesterday_price)
    TextView tvYesterdayPrice;
    @BindView(R.id.tv_today_price)
    TextView tvTodayPrice;
    @BindView(R.id.tv_height_price)
    TextView tvHeightPrice;
    @BindView(R.id.tv_low_price)
    TextView tvLowPrice;
    @BindView(R.id.tv_chengjiaoe)
    TextView tvChengjiaoe;
    @BindView(R.id.tv_chengjiaoliang)
    TextView tvChengjiaoliang;

    public IntentBean mIntentBean;
    private int mSelectFlag;
    private StockPagerAdapter mStockPagerAdapter;

    public static Intent newIntent(Context context, IntentBean intentBean, int selectFlag) {
        Intent intent = new Intent(context, StockKChartDetailActivity.class);
        intent.putExtra(INTENT_BEAN_FLAG, intentBean);
        intent.putExtra(INTENT_SELECT_FLAG, selectFlag);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.act_stock_k_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mIntentBean = (IntentBean) intent.getSerializableExtra(INTENT_BEAN_FLAG);
        mSelectFlag = intent.getIntExtra(INTENT_SELECT_FLAG, 0);
        initView();
    }

    private void initView() {
        setTabContent();
        setTitleInfo();
    }


    // set viewpager
    private void setAdapter(final PagerAdapter adapter) {
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    // init chart Fragment list
    private void setTabContent() {
        ArrayList<SupportFragment> fragments = new ArrayList<>();
        fragments.add(KMinuteChartFragment.newInstance());
        fragments.add(KFiveChartFragment.newInstance());
        fragments.add(KDayChartFragment.newInstance());
        fragments.add(KWeekChartFragment.newInstance());
        fragments.add(KMonthChartFragment.newInstance());

        String[] titles = {getString(R.string.stock_market_k_minute),
                getString(R.string.stock_market_k_five),
                getString(R.string.stock_market_k_day),
                getString(R.string.stock_market_k_week),
                getString(R.string.stock_market_k_month)};

        if (viewPager.getAdapter() == null) {
            mStockPagerAdapter = new StockPagerAdapter(getSupportFragmentManager());
            mStockPagerAdapter.setFragmentPages(fragments);
            mStockPagerAdapter.setPageTitles(titles);
            setAdapter(mStockPagerAdapter);
        } else {
            mStockPagerAdapter = (StockPagerAdapter) viewPager.getAdapter();
            mStockPagerAdapter.setFragmentPages(fragments);
            mStockPagerAdapter.setPageTitles(titles);
            viewPager.setOffscreenPageLimit(mStockPagerAdapter.getCount() - 1);
            mStockPagerAdapter.notifyDataSetChanged();
        }

        viewPager.setCurrentItem(mSelectFlag);

    }


    private void setTitleInfo(){
        try{
            if (mIntentBean != null) {
                if (mIntentBean.isUp()) {
                    // 红色
                    tvPrice.setTextColor(ContextCompat.getColor(getContext(),R.color.stock_index_up));
                    tvDiefu.setTextColor(ContextCompat.getColor(getContext(),R.color.stock_index_up));
                    tvDielv.setTextColor(ContextCompat.getColor(getContext(),R.color.stock_index_up));
                }else{
                    // 绿色
                    tvPrice.setTextColor(ContextCompat.getColor(getContext(),R.color.stock_index_down));
                    tvDiefu.setTextColor(ContextCompat.getColor(getContext(),R.color.stock_index_down));
                    tvDielv.setTextColor(ContextCompat.getColor(getContext(),R.color.stock_index_down));
                }

                tvStockName.setText(mIntentBean.getStockName());
                tvStockCode.setText(mIntentBean.getStockCode());
                tvPrice.setText(mIntentBean.getIndex());
                tvDiefu.setText(mIntentBean.getDiefu());
                tvDielv.setText(mIntentBean.getDielv()+"%");
                tvYesterdayPrice.setText(MathUtils.doubleToString(mIntentBean.getTodayPrice()));
                tvTodayPrice.setText(MathUtils.doubleToString((mIntentBean.getYesterdayPrice())));
                List<StockMarketTitleResponse> stockMarketTitleResponseList = mIntentBean.getStockMarketTitleResponseList();
                if (stockMarketTitleResponseList == null) {
                    return;
                }

                for (StockMarketTitleResponse stockMarketTitleResponse : stockMarketTitleResponseList) {
                    // 最高
                    if (TextUtils.equals(stockMarketTitleResponse.getTitle(),"最高")){
                        tvHeightPrice.setText(stockMarketTitleResponse.getContent());
                    }
                    // 最低
                    if (TextUtils.equals(stockMarketTitleResponse.getTitle(),"最低")){
                        tvLowPrice.setText(stockMarketTitleResponse.getContent());
                    }
                    // 总量
                    if (TextUtils.equals(stockMarketTitleResponse.getTitle(),"总金额")){
                        tvChengjiaoe.setText(stockMarketTitleResponse.getContent());
                    }
                    // 总金额
                    if (TextUtils.equals(stockMarketTitleResponse.getTitle(),"总量")){
                        tvChengjiaoliang.setText(stockMarketTitleResponse.getContent());
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    @OnClick(R.id.rl_close)
    public void close() {
        finish();
    }


}
