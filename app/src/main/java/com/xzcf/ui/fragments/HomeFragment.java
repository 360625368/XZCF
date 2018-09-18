package com.xzcf.ui.fragments;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.xzcf.Constants;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.response.GetAdvertiseResponse;
import com.xzcf.data.data.socket.BaseSocketRes;
import com.xzcf.data.data.socket.SocketSub;
import com.xzcf.ui.WebActivity;
import com.xzcf.ui.adapters.HomeHotStockAdapter;
import com.xzcf.ui.adapters.HomeIndexViewPagerAdapter;
import com.xzcf.utils.DialogUtils;
import com.xzcf.utils.GlideImageLoader;
import com.xzcf.utils.UiUtils;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseSocketFragment<BaseSocketRes<String>> {


    @BindView(R.id.llContext)
    LinearLayout llContext;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.search_bar)
    ConstraintLayout searchBar;
    @BindView(R.id.status_bar_bg)
    View statusBar;
    private Listener listener;
    private List<GetAdvertiseResponse.AdvInfosBean> mAdvInfosBeans;
    private Banner bannerView;
    private HomeIndexViewPagerAdapter indexViewPagerAdapter;
    private ViewPager viewpagerIndex;
//    private HotStocksAdapter hotStocksAdapter;
    private RecyclerView rvHotStocks;
    private Disposable stockIndexDisposable;
    private Disposable stockRankListDisposable;
    private Disposable mGetAdvDisposable;
    private Disposable mIntervalUpdateDisposable;
    private Dialog mProgressDialog;
    boolean isCreate = true;


    private HomeHotStockAdapter mHomeHotStockAdapter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.listener = (Listener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        try{
            unbinder = ButterKnife.bind(this, view);
            statusBar.getLayoutParams().height = UiUtils.getStatusBarHeight(getContext());
            bindView(llContext);

            String qid = "1";
            String path = "/stkdata?obj=SH601519&field=ZuiXinJia,ZhangDie,ZhangFu&sub=1" + "&" + qid;

            addSocketSub(new SocketSub(qid,path));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        notiftSubChange();
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            if (bannerView != null) {
                bannerView.startAutoPlay();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        try{
            if (bannerView != null) {
                bannerView.stopAutoPlay();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onSocketReceive(BaseSocketRes<String> result, String rawJson) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try{
            isCreate = true;
            unbinder.unbind();
            if (stockIndexDisposable != null) {
                stockIndexDisposable.dispose();
                stockIndexDisposable = null;
            }
            if (stockRankListDisposable != null) {
                stockRankListDisposable.dispose();
                stockRankListDisposable = null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void bindView(ViewGroup parent) {
        try{
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            parent.removeAllViews();
            bindBannerView(inflater, parent);
            bindIndexView(inflater, parent);
            bindHotStockView(inflater, parent);

            smartRefreshLayout.setEnableLoadMore(false);
            smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
                loadData();
                smartRefreshLayout.finishRefresh();
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 广告
    private void bindBannerView(LayoutInflater inflater, ViewGroup parent) {
        try{
            View view = inflater.inflate(R.layout.include_home_banner, parent, false);
            parent.addView(view);
            this.bannerView = view.findViewById(R.id.banner);
            bannerView.setImageLoader(new GlideImageLoader());
            bannerView.setOnBannerListener(position -> {
                if (mAdvInfosBeans == null) return;
                GetAdvertiseResponse.AdvInfosBean advInfosBean = mAdvInfosBeans.get(position);
                if (advInfosBean == null) return;
                if (TextUtils.isEmpty(advInfosBean.getAdvUrl())) return;
                startActivity(WebActivity.startAction(getContext(), advInfosBean.getTitle(), advInfosBean.getAdvUrl()));
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 指数
    private void bindIndexView(LayoutInflater inflater, ViewGroup parent) {
        try{
            View view = inflater.inflate(R.layout.include_home_index, parent, false);
            if (indexViewPagerAdapter == null) {
                indexViewPagerAdapter = new HomeIndexViewPagerAdapter();
            }
            viewpagerIndex = view.findViewById(R.id.vpIndex);
            viewpagerIndex.setAdapter(indexViewPagerAdapter);
            parent.addView(view);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 热门股票
    private void bindHotStockView(LayoutInflater inflater, ViewGroup parent) {
        try{
            View view = inflater.inflate(R.layout.item_home_hot_stock, parent, false);
            rvHotStocks = view.findViewById(R.id.rvHotStocks);
            List<String[]> datas = new ArrayList<>();
            mHomeHotStockAdapter = new HomeHotStockAdapter(datas);

            rvHotStocks.setAdapter(mHomeHotStockAdapter);
            rvHotStocks.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rvHotStocks.setNestedScrollingEnabled(false);
            rvHotStocks.setFocusable(false);
            parent.addView(view);


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void loadData() {
//        if (stockIndexDisposable != null) {
//            stockIndexDisposable.dispose();
//            stockIndexDisposable = null;
//        }
//        stockIndexDisposable = StockDataManager.getInstance().getStockIndex()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        stockIndexResponse -> {
//                            indexViewPagerAdapter.setIndexStr(stockIndexResponse.getStockIndexsStr());
//                            if (smartRefreshLayout != null && smartRefreshLayout.isRefreshing()) {
//                                smartRefreshLayout.finishRefresh();
//                            }
//                        },
//                        throwable -> {});
//
//        if (stockRankListDisposable != null) {
//            stockRankListDisposable.dispose();
//            stockRankListDisposable = null;
//        }
//        stockRankListDisposable = DataManager.getInstance().getStockRankList("5")
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                        stockRankItemResponse -> {
//                            List<String[]> stockRankList = new ArrayList<>();
//                            stockRankList.addAll(stockRankItemResponse.getZhangFuArrs());
//                            stockRankList.addAll(stockRankItemResponse.getDieFuArrs());
//                            hotStocksAdapter.setStockRankList(stockRankList);
//                        }, throwable -> {})
//                );

        try{
            if (mGetAdvDisposable != null) {
                mGetAdvDisposable.dispose();
                mGetAdvDisposable = null;
            }
            mGetAdvDisposable = DataManager.getInstance().getAdvertise()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getAdvertiseResponse -> {
                        if (!getAdvertiseResponse.getCode().equals("0")) return;

                        mAdvInfosBeans = new ArrayList<>(getAdvertiseResponse.getAdvInfos());
                        if (mAdvInfosBeans.size() == 0) return;

                        List<String> bannerImgs = new ArrayList<>();
                        for (GetAdvertiseResponse.AdvInfosBean bean : mAdvInfosBeans) {
                            bannerImgs.add(Constants.IMAGES_URI_HOST + bean.getImgUrl());
                        }
                        bannerView.update(bannerImgs);
                    }, throwable -> {
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @OnClick(R.id.llSearchBox)
    public void onSearchBoxClicked() {
        if (listener != null) {
            listener.onSearch();
        }
    }

    public interface Listener {
        void onSearch();
    }

    private void updateData(){
        try{
//            if (stockIndexDisposable != null) {
//                stockIndexDisposable.dispose();
//                stockIndexDisposable = null;
//            }

            stockIndexDisposable = StockDataManager.getInstance().getStockIndex()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            stockIndexResponse -> {
                                indexViewPagerAdapter.setIndexStr(stockIndexResponse.getStockIndexsStr());
                                if (smartRefreshLayout != null && smartRefreshLayout.isRefreshing()) {
                                    smartRefreshLayout.finishRefresh();
                                }
                                if (mProgressDialog != null){
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            },
                            throwable -> {
                                if (mProgressDialog != null){
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            });

//            if (stockRankListDisposable != null) {
//                stockRankListDisposable.dispose();
//                stockRankListDisposable = null;
//            }
            stockRankListDisposable = DataManager.getInstance().getStockRankList("5")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            stockRankItemResponse -> {
                                List<String[]> stockRankList = new ArrayList<>();
                                stockRankList.addAll(stockRankItemResponse.getZhangFuArrs());
                                stockRankList.addAll(stockRankItemResponse.getDieFuArrs());
//                                hotStocksAdapter.setStockRankList(stockRankList);
                                mHomeHotStockAdapter.setNewData(stockRankList);
                                if (mProgressDialog != null){
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }
                            }, throwable -> {
                                if (mProgressDialog != null){
                                    mProgressDialog.dismiss();
                                    mProgressDialog = null;
                                }

                            }
                    );
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 间隔一秒刷新热门股票
    private void intervalUpdateData(){
        try{

            mIntervalUpdateDisposable = Flowable.interval(3, TimeUnit.SECONDS)
                    .subscribe(aLong -> updateData());

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (isCreate) {
            isCreate = false;
            mProgressDialog = DialogUtils.buildProgressDialogsDialog(getContext(), "加载中", "请稍候 " );
            mProgressDialog.show();
            updateData();
            intervalUpdateData();
        }

    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
//        if (stockIndexDisposable != null && !stockIndexDisposable.isDisposed()) {
//            stockIndexDisposable.dispose();
//            stockIndexDisposable = null;
//        }
//
//        if (stockRankListDisposable != null && !stockRankListDisposable.isDisposed()) {
//            stockRankListDisposable.dispose();
//            stockRankListDisposable = null;
//        }
//
//        if (mIntervalUpdateDisposable != null && !mIntervalUpdateDisposable.isDisposed()) {
//            mIntervalUpdateDisposable.dispose();
//            mIntervalUpdateDisposable = null;
//        }

        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
