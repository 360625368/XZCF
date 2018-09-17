package com.xzcf.ui.fragments.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.guoziwei.klinelib.chart.KLineView;
import com.guoziwei.klinelib.model.HisData;
import com.xzcf.R;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.req.StockDayChartReq;
import com.xzcf.data.data.response.IntentBean;
import com.xzcf.ui.StockKChartDetailActivity;
import com.xzcf.ui.StockMarketActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class KWeekChartFragment extends BaseSuperFragment {


    @BindView(R.id.time_line_view)
    KLineView mKLineView;
    @BindView(R.id.rl_time_line)
    RelativeLayout mLayout;
    private IntentBean mIntentBean;
    private String mStockCode;
    // K线图数据
    private List<HisData> mKDayChartResList;


    public static KWeekChartFragment newInstance() {
        Bundle args = new Bundle();
        KWeekChartFragment fragment = new KWeekChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_k_chart_week;
    }

    @Override
    protected void initView(View rootView) {

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
                if (isDetailEntry) {
                    mLayout.setVisibility(View.GONE);
                } else {
                    mLayout.setVisibility(View.VISIBLE);
                }
                mStockCode = mIntentBean.getStockCode();
                getKChartData();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void getKChartData() {
        super.getKChartData();
        try{
            StockDayChartReq stockDayChartReq = new StockDayChartReq()
                    .setStockCode(mStockCode)
                    .setCategory("5")
                    .setCount("700")
                    .setStart("0")
                    .setIsFirst("1");
            mChartDisposable = StockDataManager.getInstance().getStockKDayChart(stockDayChartReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(kDayChartRes -> {
                        mKDayChartResList = kDayChartRes.getData().splitChartData();
                        initKChart();

                    }, throwable -> {});
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
        startActivity(StockKChartDetailActivity.newIntent(getActivity(), mIntentBean, 3));
    }

    private void initKChart() {
        mKLineView.showVolume();
        mKLineView.initData(mKDayChartResList);
        mKLineView.setLimitLine();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChartDisposable != null && !mChartDisposable.isDisposed()) {
            mChartDisposable.dispose();
        }
    }

}
