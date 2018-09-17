package com.xzcf.ui.fragments.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guoziwei.klinelib.chart.TimeLineView;
import com.guoziwei.klinelib.model.HisData;
import com.xzcf.R;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.req.StockDayChartReq;
import com.xzcf.data.data.req.StockMarketDetailReq;
import com.xzcf.data.data.req.StockQuotesReq;
import com.xzcf.data.data.response.IntentBean;
import com.xzcf.data.data.response.KDayChartRes;
import com.xzcf.data.data.response.StockMarketDetailRes;
import com.xzcf.data.data.response.StockMarketFiveSpeedBean;
import com.xzcf.ui.StockKChartDetailActivity;
import com.xzcf.ui.StockMarketActivity;
import com.xzcf.ui.adapters.MarketDetailAdapter;
import com.xzcf.ui.adapters.MarketFiveSpeedAdapter;
import com.xzcf.ui.view.CustomLinearLayoutManager;
import com.xzcf.utils.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class KMinuteChartFragment extends BaseSuperFragment {

    @BindView(R.id.rl_market_speed)
    RelativeLayout rlMarketSpeed;
    @BindView(R.id.rl_market_detail)
    RelativeLayout rlMarketDetail;
    @BindView(R.id.time_line_view)
    TimeLineView mTimeLineView;
    @BindView(R.id.tv_market_speed)
    TextView tvMarketSpeed;
    @BindView(R.id.tv_market_detail)
    TextView tvMarketDetail;
//    @BindView(R.id.recycle_market_speed)
//    RecyclerView recycleMarketSpeed;
    @BindView(R.id.recycle_market_detail)
    RecyclerView recycleMarketDetail;
    @BindView(R.id.rl_time_line)
    RelativeLayout mLayout;
    @BindView(R.id.ll_market_detail)
    LinearLayout llMarketDetail;
    @BindView(R.id.ll_market_speed)
    LinearLayout llMarketSpeed;
    @BindView(R.id.recycle_market_speed_sell)
    RecyclerView recycleMarketSpeedSell;
    @BindView(R.id.recycle_market_speed_buy)
    RecyclerView recycleMarketSpeedBuy;

    private List<StockMarketFiveSpeedBean> mStockMarketFiveSpeedBeanList;
    private List<StockMarketDetailRes> mStockMarketDetailRes;
    private MarketFiveSpeedAdapter mSellFiveAdapter;
    private MarketFiveSpeedAdapter mBuyFiveAdapter;
    private MarketDetailAdapter mMarketDetailAdapter;
    private IntentBean mIntentBean;
    private Disposable mFiveSpeedDisposable;
    private Disposable mDetailDisposable;
    private Disposable mIntervalFiveDisposable;
    private HashMap<String, String> mFiveSpeedHashMap;
    private String mStockCode;
    // K线图数据
    private List<HisData> mKMinuteChartResList;
    public static final String Detail_Start = "0";
    public static final String Detail_Count = "10";


//    private SellFiveAdapter mSellFiveAdapter;
//    private BuyFiveAdapter mBuyFiveAdapter;

    private List<StockMarketFiveSpeedBean> mSellFiveList;
    private List<StockMarketFiveSpeedBean> mBuyFiveList;


    public static KMinuteChartFragment newInstance() {
        Bundle args = new Bundle();
        KMinuteChartFragment fragment = new KMinuteChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_k_chart_minute;
    }

    @Override
    protected void initView(View rootView) {
        mTimeLineView.setDateFormat("HH:mm");

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        try{
            FragmentActivity activity = getActivity();
            if (activity instanceof StockMarketActivity) {
                isDetailEntry = false;
                StockMarketActivity stockMarketActivity = (StockMarketActivity) getActivity();
                if (stockMarketActivity != null) {
                    mIntentBean = stockMarketActivity.mIntentBean;
                }
            } else if (activity instanceof StockKChartDetailActivity) {
                isDetailEntry = true;
                StockKChartDetailActivity stockKChartDetailActivity = (StockKChartDetailActivity) getActivity();
                if (stockKChartDetailActivity != null) {
                    mIntentBean = stockKChartDetailActivity.mIntentBean;
                }
            }
            if (mIntentBean != null) {
                mStockCode = mIntentBean.getStockCode();
                mKMinuteChartResList = new ArrayList<>();
                if (isDetailEntry) {
                    llMarketDetail.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT,0.7f));
                    mLayout.setVisibility(View.GONE);
                } else {
                    mLayout.setVisibility(View.VISIBLE);
                }
                initSpeedAdapter();
                initDetailAdapter();
                getKChartData();
                getFiveSpeedData();
                getDetailData();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    // 五档点击
    @OnClick(R.id.rl_market_speed)
    public void marketSpeedClick() {
        rlMarketSpeed.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.market_speed));
        rlMarketDetail.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.market_speed_focus));
        tvMarketSpeed.setTextColor(ContextCompat.getColor(_mActivity, R.color.white));
        tvMarketDetail.setTextColor(ContextCompat.getColor(_mActivity, R.color.stock_tab_txt));
        llMarketSpeed.setVisibility(View.VISIBLE);
        recycleMarketDetail.setVisibility(View.GONE);
    }

    // 明细点击
    @OnClick(R.id.rl_market_detail)
    public void marketDetailClick() {
        rlMarketSpeed.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.market_speed_focus));
        rlMarketDetail.setBackgroundColor(ContextCompat.getColor(_mActivity, R.color.market_speed));
        tvMarketSpeed.setTextColor(ContextCompat.getColor(_mActivity, R.color.stock_tab_txt));
        tvMarketDetail.setTextColor(ContextCompat.getColor(_mActivity, R.color.white));
        llMarketSpeed.setVisibility(View.GONE);
        recycleMarketDetail.setVisibility(View.VISIBLE);
    }

    // 设置五档列表
    private void initSpeedAdapter() {
        CustomLinearLayoutManager sellLinearLayoutManager = new CustomLinearLayoutManager(getContext());
        sellLinearLayoutManager.setScrollEnabled(false);

        CustomLinearLayoutManager buyLinearLayoutManager = new CustomLinearLayoutManager(getContext());
        buyLinearLayoutManager.setScrollEnabled(false);

        // 卖
        mSellFiveList = new ArrayList<>();
        recycleMarketSpeedSell.setLayoutManager(sellLinearLayoutManager);
        mSellFiveAdapter = new MarketFiveSpeedAdapter(mSellFiveList, mIntentBean.getTodayPrice());
        recycleMarketSpeedSell.setAdapter(mSellFiveAdapter);
        // 买
        mBuyFiveList = new ArrayList<>();
        recycleMarketSpeedBuy.setLayoutManager(buyLinearLayoutManager);
        mBuyFiveAdapter = new MarketFiveSpeedAdapter(mBuyFiveList, mIntentBean.getTodayPrice());
        recycleMarketSpeedBuy.setAdapter(mBuyFiveAdapter);
    }

    // 设置明细列表
    private void initDetailAdapter() {
        mStockMarketDetailRes = new ArrayList<>();
        recycleMarketDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMarketDetailAdapter = new MarketDetailAdapter(mStockMarketDetailRes, mIntentBean.getTodayPrice());
        recycleMarketDetail.setAdapter(mMarketDetailAdapter);
    }

    private void initKChart() {
        mTimeLineView.setLastClose(mIntentBean.getTodayPrice());
        mTimeLineView.initData(mKMinuteChartResList);
    }

    // 获取五档信息
    public void getFiveSpeedData() {
        try{
            List<StockQuotesReq> quotesReqList = new ArrayList<>();
            StockQuotesReq stockQuotesReq = new StockQuotesReq()
                    .setStockCode(mStockCode);
            quotesReqList.add(stockQuotesReq);
            mFiveSpeedDisposable = StockDataManager.getInstance().getStockQuotes(quotesReqList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stockQuotesResponse -> {
                        if (stockQuotesResponse.isOk()) {
                            mFiveSpeedHashMap = stockQuotesResponse.getData().getResultSingleSplit();
                            mStockMarketFiveSpeedBeanList = MathUtils.sortMarketFiveSpeed(mFiveSpeedHashMap);
                            //mMarketFiveSpeedAdapter.setNewData(mStockMarketFiveSpeedBeanList);
                            shiftFiveData();
                        }
                    }, throwable -> {
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 获取明细信息
    public void getDetailData() {
        try{
            StockMarketDetailReq stockMarketDetailReq = new StockMarketDetailReq()
                    .setStockCode(mStockCode).setStart(Detail_Start).setCount(Detail_Count);
            mDetailDisposable = StockDataManager.getInstance().getStockTransaction(stockMarketDetailReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((StockMarketDetailRes stockMarketDetailRes) -> {
                        // 初始化列表
                        if (stockMarketDetailRes.isOk()) {
                            mStockMarketDetailRes = stockMarketDetailRes.splitDetailData();
                            mMarketDetailAdapter.setNewData(mStockMarketDetailRes);
                        }
                    }, throwable -> {
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 获取K线图数据
    @Override
    protected void getKChartData() {
        super.getKChartData();
        try{
//            StockMinuteChartReq stockMinuteChartReq = new StockMinuteChartReq()
//                    .setStockCode(mStockCode);

            StockDayChartReq stockDayChartReq = new StockDayChartReq()
                    .setStockCode(mStockCode)
                    .setCategory("0")
                    .setCount("800")
                    .setStart("0")
                    .setIsFirst("1");

            mChartDisposable = StockDataManager.getInstance().getStockKDayChart(stockDayChartReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe((KDayChartRes kDayChartRes) -> {
                        if (kDayChartRes.isOk()) {
                            mKMinuteChartResList = kDayChartRes.getData().splitChartData();
                            initKChart();
                            intervalUpdateData();
                        }
                    }, throwable -> {
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 详情
     */
    @OnClick(R.id.rl_time_line)
    public void onDetailClick() {
        if (mIntentBean == null) {
            return;
        }
        startActivity(StockKChartDetailActivity.newIntent(getActivity(), mIntentBean, 0));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFiveSpeedDisposable != null && !mFiveSpeedDisposable.isDisposed()) {
            mFiveSpeedDisposable.dispose();
        }
        if (mDetailDisposable != null && !mDetailDisposable.isDisposed()) {
            mDetailDisposable.dispose();
        }
        if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
            mIntervalFiveDisposable.dispose();
        }
        if (mChartDisposable != null && !mChartDisposable.isDisposed()) {
            mChartDisposable.dispose();
        }
    }

    private void shiftFiveData(){
       try{
           mSellFiveList = new ArrayList<>();
           mBuyFiveList = new ArrayList<>();

           if (mStockMarketFiveSpeedBeanList != null) {
               for (StockMarketFiveSpeedBean item : mStockMarketFiveSpeedBeanList) {
                   // 卖
                   if (item.getTitle().contains("卖五")||
                           item.getTitle().contains("卖四")||
                           item.getTitle().contains("卖三")||
                           item.getTitle().contains("卖二")||
                           item.getTitle().contains("卖一")) {
                       mSellFiveList.add(item);
                   }

                   // 买
                   if (item.getTitle().contains("买一")||
                           item.getTitle().contains("买二")||
                           item.getTitle().contains("买三")||
                           item.getTitle().contains("买四")||
                           item.getTitle().contains("买五")) {
                       mBuyFiveList.add(item);
                   }
               }
           }

           mSellFiveAdapter.setNewData(mSellFiveList);

           mBuyFiveAdapter.setNewData(mBuyFiveList);
       }catch(Exception ex){
           ex.printStackTrace();
       }
    }

    /**
     * 间隔1秒刷新数据
     */
    private void intervalUpdateData(){
        try{
            mIntervalFiveDisposable = Observable.interval(2, TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        getFiveSpeedData();
                        getDetailData();
                    }, throwable -> {
                        if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                            mIntervalFiveDisposable.dispose();
                        }
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
