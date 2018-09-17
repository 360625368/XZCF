package com.xzcf.ui.fragments.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.StockRankItemResponse;
import com.xzcf.data.data.response.StockRankItemTitle;
import com.xzcf.ui.adapters.StockRankAdapter;
import com.xzcf.ui.view.StockIndexView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ShenzhenStockFragment extends BaseSuperFragment {


    @BindView(R.id.recycle_stock_list)
    RecyclerView recycleStockList;
    @BindView(R.id.swipeLayout_stock)
    SmartRefreshLayout swipeLayoutStock;
    private final String stockType= "2";

    public static ShenzhenStockFragment newInstance() {
        Bundle args = new Bundle();
        ShenzhenStockFragment fragment = new ShenzhenStockFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_hs;
    }

    @Override
    protected void initView(View rootView) {
        try{
            unbinder = ButterKnife.bind(this, rootView);
            // 指数
            mStockIndexView = new StockIndexView(getActivity(), recycleStockList);
            View headView = mStockIndexView.initView();
            initAdapter();
            mStockRankAdapter.addHeaderView(headView);
            swipeLayoutStock.setEnableLoadMore(false);
            swipeLayoutStock.setOnRefreshListener(refreshLayout -> {
                swipeLayoutStock.finishRefresh();
//            if (mStockIndexView != null) {
//                mStockIndexView.initData();
//            }
//            initRankData();
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        System.out.println("onLazyInitView");
        try{
            if (mStockIndexView != null) {
                mStockIndexView.initData();
            }
            // 涨跌幅
            initRankData();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    private void initRankData(){
        try{
            disposable = DataManager.getInstance().getStockRankList(stockType)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(stockRankItemResponse -> {
                                swipeLayoutStock.finishRefresh();
                                // 处理数据
                                disposeRankData(stockRankItemResponse);
                            },
                            throwable -> {
                            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void initAdapter(){
        try{
            List<StockRankItemTitle> multiItemEntityList = new ArrayList<>();
            recycleStockList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mStockRankAdapter = new StockRankAdapter(multiItemEntityList);
            recycleStockList.setAdapter(mStockRankAdapter);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void disposeRankData(StockRankItemResponse stockRankItemResponse){
        try{
            List<StockRankItemTitle> stockRankItemTitles = new ArrayList<>();
            StockRankItemTitle stockRankItemTitle = new StockRankItemTitle();
            stockRankItemTitle.setUpList(stockRankItemResponse.getZhangFuSplit());
            stockRankItemTitle.setDownList(stockRankItemResponse.getDieFuSplit());
            stockRankItemTitles.add(stockRankItemTitle);
            mStockRankAdapter.setNewData(stockRankItemTitles);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void intervalUpdateData(){
        try{
            mIntervalDisposable = Observable.interval(INTERVAL_TIME, TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        // 涨跌幅
                        initRankData();
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (mStockIndexView != null) {
            mStockIndexView.intervalUpdateData();
        }
        intervalUpdateData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (mStockIndexView != null) {
            mStockIndexView.onDisposable();
        }
        if (mIntervalDisposable != null && !mIntervalDisposable.isDisposed()) {
            mIntervalDisposable.dispose();
            mIntervalDisposable = null;
        }
    }
}
