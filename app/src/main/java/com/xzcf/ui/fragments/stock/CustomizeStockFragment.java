package com.xzcf.ui.fragments.stock;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.event.StockSelfEvent;
import com.xzcf.data.data.req.StockQuotesReq;
import com.xzcf.data.data.req.StockSelfReq;
import com.xzcf.data.data.response.CustomizeStockResponse;
import com.xzcf.data.data.response.StockSelfResponse;
import com.xzcf.ui.SearchActivity;
import com.xzcf.ui.adapters.CustomizeStockAdapter;
import com.xzcf.ui.view.StockIndexSortView;
import com.xzcf.ui.view.StockIndexView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CustomizeStockFragment extends BaseSuperFragment  {


    @BindView(R.id.recycle_customize_list)
    RecyclerView recycleCustomizeList;
    @BindView(R.id.swipeLayout_customize)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;

    CustomizeStockAdapter mCustomizeStockAdapter;
    StockIndexView mStockIndexView;
    StockIndexSortView mStockIndexSortView;

    View mCustomizeNoneView;

    List<StockSelfResponse.InfosBean> mInfosBeanList;
    List<CustomizeStockResponse> mCustomizeStockResponseList;

    Disposable stockSelfDisposable;
    Disposable stockQuoteDisposable;
    Disposable stockIntervalDisposable;

    public static CustomizeStockFragment newInstance() {
        Bundle args = new Bundle();
        CustomizeStockFragment fragment = new CustomizeStockFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_customize;
    }

    @Override
    protected void initView(View rootView) {
        try{
            unbinder = ButterKnife.bind(this, rootView);
            // 指数
            mStockIndexView = new StockIndexView(getActivity(), recycleCustomizeList);
            View headView = mStockIndexView.initView();
            // 排序
            mStockIndexSortView = new StockIndexSortView(getActivity(), recycleCustomizeList);
            View headSortView = mStockIndexSortView.initView();
            // 无内容
            mCustomizeNoneView = LayoutInflater.from(getActivity()).inflate(R.layout.view_cumstomize_none,(ViewGroup)recycleCustomizeList.getParent(),false);
            LinearLayout viewNone = mCustomizeNoneView.findViewById(R.id.ll_customize_none);
            viewNone.setOnClickListener(l ->{
                // 添加自选
                startActivity(SearchActivity.startIntent(getActivity()));
            });
            initAdapter();

            mCustomizeStockAdapter.addHeaderView(headView);
            mCustomizeStockAdapter.addHeaderView(headSortView);
            mCustomizeStockAdapter.addHeaderView(mCustomizeNoneView);

            smartRefreshLayout.setEnableLoadMore(false);
            smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
                if (mStockIndexView != null) {
                    mStockIndexView.initData();
                }
                initData();
                smartRefreshLayout.finishRefresh();

            });
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    private void initAdapter() {
        try{
            List<CustomizeStockResponse> customizeStockResponseList = new ArrayList<>();
            recycleCustomizeList.setLayoutManager(new LinearLayoutManager(getActivity()));
            mCustomizeStockAdapter = new CustomizeStockAdapter(customizeStockResponseList);
            recycleCustomizeList.setAdapter(mCustomizeStockAdapter);
            isShowNoneLayout();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void isShowNoneLayout() {
        try{
            int itemCount = mCustomizeStockAdapter.getItemCount();
            if (itemCount <= 0 || !App.context().isLogged()) {
                mCustomizeNoneView.setVisibility(View.VISIBLE);
            } else if(itemCount > 0) {
                mCustomizeNoneView.setVisibility(View.GONE);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void isShowNoneByLogger(){
        try{
            if (!App.context().isLogged()) {
                mCustomizeNoneView.setVisibility(View.VISIBLE);
                mCustomizeStockAdapter.setNewData(new ArrayList<>());
            }else{
                mCustomizeNoneView.setVisibility(View.GONE);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 加载数据
     * 自选列表
     */
    private void initData() {
        try{
            if (App.context().isLogged()) {
                StockSelfReq stockSelfReq = new StockSelfReq().setQueryCommand().setMemberId(App.context().getLoginInfo().getMemberId());
                stockSelfDisposable = DataManager.getInstance().getStockSelfList(stockSelfReq)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(stockSelfResponse -> {
                                    smartRefreshLayout.finishRefresh();
                                    mInfosBeanList = stockSelfResponse.getInfos();
                                    initDataDetail();
                                },
                                throwable -> {});
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void initDataDetail(){
        try{
            mCustomizeStockResponseList = new ArrayList<>();
            if (mInfosBeanList == null) {
                mCustomizeStockAdapter.setNewData(mCustomizeStockResponseList);
                mCustomizeNoneView.setVisibility(View.VISIBLE);
                return;
            }
            List<StockQuotesReq> quotesReqList = new ArrayList<>();
            // 请求单个自选涨跌幅数据
            Disposable subscribe = Observable.fromIterable(mInfosBeanList)
                    .subscribe(infosBean -> {
                        // 自选列表
                        CustomizeStockResponse customizeStockResponse = new CustomizeStockResponse();
                        customizeStockResponse.setStockCode(infosBean.getStockCode())
                                .setStockName(infosBean.getStockName());
                        mCustomizeStockResponseList.add(customizeStockResponse);
                        // 请求 参数
                        StockQuotesReq stockQuotesReq = new StockQuotesReq().setStockCode(infosBean.getStockCode());
                        quotesReqList.add(stockQuotesReq);

                    }, throwable -> {
                    });


            // 自选列表详情
            stockQuoteDisposable = StockDataManager.getInstance().getStockQuotes(quotesReqList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stockQuotesResponse -> {
                        if (stockQuotesResponse.isOk()) {
                            // 自选列表 涨跌幅列表
                            for (int i = 0; i < mCustomizeStockResponseList.size(); i++) {
                                String resultTodayPrice = stockQuotesResponse.getData().getResultTodayPrice(i + 1);
                                String resultYesterdayPrice = stockQuotesResponse.getData().getResultYesterdayPrice(i + 1);
                                CustomizeStockResponse customizeStockRes = mCustomizeStockResponseList.get(i);
                                customizeStockRes.setTodayPrice(resultTodayPrice).setYesterdayPrice(resultYesterdayPrice);

                            }
                            mCustomizeStockAdapter.setNewData(mCustomizeStockResponseList);
                        }

                    }, throwable -> {});


            isShowNoneLayout();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (mStockIndexView != null) {
            mStockIndexView.initData();
        }
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (stockSelfDisposable != null && !stockSelfDisposable.isDisposed()) {
            stockSelfDisposable.dispose();
        }

        if (stockQuoteDisposable != null && !stockQuoteDisposable.isDisposed()) {
            stockQuoteDisposable.dispose();
        }

        if (stockIntervalDisposable != null && !stockIntervalDisposable.isDisposed()) {
            stockIntervalDisposable.dispose();
        }
    }

    @Subscribe(sticky = true)
    public void OnStockSelfEvent(StockSelfEvent stockSelfEvent){
        initData();
    }

    /**
     * 间隔1秒更新数据
     */
    private void intervalUpdateData(){
        stockIntervalDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(aLong -> {
                    System.out.println("刷新");
                    initData();
                });
    }

    private void updateSelfList(){
        if (App.context().isLogged()) {
            StockSelfReq stockSelfReq = new StockSelfReq().setQueryCommand().setMemberId(App.context().getLoginInfo().getMemberId());
            stockSelfDisposable = DataManager.getInstance().getStockSelfList(stockSelfReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(stockSelfResponse -> {
                                mInfosBeanList = stockSelfResponse.getInfos();
                                initDataDetail();
                            },
                            throwable -> {});
        }
    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        isShowNoneByLogger();
        if (mStockIndexView != null) {
            mStockIndexView.intervalUpdateData();
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        if (mStockIndexView != null) {
            mStockIndexView.onDisposable();
        }
    }

}
