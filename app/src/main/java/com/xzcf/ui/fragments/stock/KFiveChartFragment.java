package com.xzcf.ui.fragments.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.guoziwei.klinelib.chart.TimeLineView;
import com.guoziwei.klinelib.model.HisData;
import com.xzcf.R;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.req.StockDayChartReq;
import com.xzcf.data.data.response.IntentBean;
import com.xzcf.data.data.response.KDayChartRes;
import com.xzcf.ui.StockKChartDetailActivity;
import com.xzcf.ui.StockMarketActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class KFiveChartFragment extends BaseSuperFragment {

    @BindView(R.id.time_line_view)
    TimeLineView timeLineView;
    @BindView(R.id.rl_time_line)
    RelativeLayout mLayout;
    private IntentBean mIntentBean;
    private String mStockCode;
    // K线图数据
    private List<HisData> mKMinuteChartResList;

    public static KFiveChartFragment newInstance() {
        Bundle args = new Bundle();
        KFiveChartFragment fragment = new KFiveChartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_k_chart_five;
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
        startActivity(StockKChartDetailActivity.newIntent(getActivity(), mIntentBean, 1));
    }

    private void initKChart() {
        timeLineView.setLastClose(mIntentBean.getTodayPrice());
        timeLineView.initData(mKMinuteChartResList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChartDisposable != null && !mChartDisposable.isDisposed()) {
            mChartDisposable.dispose();
        }
    }
}
