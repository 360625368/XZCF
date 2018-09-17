package com.xzcf.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.event.StockSelfEvent;
import com.xzcf.data.data.req.StockQuotesReq;
import com.xzcf.data.data.req.StockSelfReq;
import com.xzcf.data.data.response.IntentBean;
import com.xzcf.data.data.response.StockMarketTitleResponse;
import com.xzcf.data.data.response.StockSelfResponse;
import com.xzcf.ui.adapters.StockMarketTitleAdapter;
import com.xzcf.ui.adapters.StockPagerAdapter;
import com.xzcf.ui.fragments.stock.KDayChartFragment;
import com.xzcf.ui.fragments.stock.KFiveChartFragment;
import com.xzcf.ui.fragments.stock.KMinuteChartFragment;
import com.xzcf.ui.fragments.stock.KMonthChartFragment;
import com.xzcf.ui.fragments.stock.KWeekChartFragment;
import com.xzcf.ui.view.NoTouchScrollViewpager;
import com.xzcf.utils.DialogUtils;
import com.xzcf.utils.MathUtils;
import com.xzcf.utils.StockRuleUtils;
import com.xzcf.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.SupportFragment;


public class StockMarketActivity extends BaseActivity {


    private static final String STOCK_CODE = "stock_code";
    private static final String STOCK_NAME = "stock_name";


    private Disposable mTitleDisposable;
    private Disposable mSelfDisposable;
    private Disposable mSelfListDisposable;
    private Disposable mIntervalFiveDisposable;
    private String mStockCode;
    private String mStockName;
    private String mStockSelfId;
    private StockMarketTitleAdapter mStockMarketTitleAdapter;
    private List<StockMarketTitleResponse> mStockMarketTitleResponseList;
    public IntentBean mIntentBean;
    private boolean isFirstLoad = true;
    private StockPagerAdapter mStockPagerAdapter;

    //    @BindView(R.id.swipeLayout_stock_market)
//    SmartRefreshLayout swipeLayoutStockMarket;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    NoTouchScrollViewpager viewPager;
    @BindView(R.id.tv_stock_market_name)
    TextView tvStockMarketName;
    @BindView(R.id.tv_stock_market_code)
    TextView tvStockMarketCode;
    @BindView(R.id.chk_stock_market_control)
    CheckBox chkStockMarketControl;
    @BindView(R.id.recycle_stock_market)
    RecyclerView recycleStockMarket;
    @BindView(R.id.tv_market_today)
    TextView tvMarketToday;
    @BindView(R.id.tv_market_yesterday)
    TextView tvMarketYesterday;
    @BindView(R.id.tv_stock_market_index)
    TextView tvStockMarketIndex;
    @BindView(R.id.tv_stock_point)
    TextView tvStockPoint;
    @BindView(R.id.iv_stock_point_arrow)
    ImageView ivStockPointArrow;
    @BindView(R.id.tv_stock_rate)
    TextView tvStockRate;
    @BindView(R.id.tv_stock_rate_arrow)
    ImageView tvStockRateArrow;
    @BindView(R.id.ll_market_control)
    LinearLayout llMarketControl;
    private Dialog mProgressDialog;

    public static Intent newIntent(Context context, String stockCode, String stockName) {
        Intent intent = new Intent(context, StockMarketActivity.class);
        intent.putExtra(STOCK_CODE, stockCode);
        intent.putExtra(STOCK_NAME, stockName);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_stock_market);
        ButterKnife.bind(this);
//        swipeLayoutStockMarket.setEnableRefresh(false);
//        swipeLayoutStockMarket.setEnableLoadMore(false);
//        swipeLayoutStockMarket.setOnRefreshListener(refreshLayout -> {
//            swipeLayoutStockMarket.finishRefresh();
//            getMarketTitleInfo();
//            refreshKFragmentData();
//        });
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMarketControlView();
    }

    private void initView() {
        Intent intent = getIntent();
        mStockCode = intent.getStringExtra(STOCK_CODE);
        mStockName = intent.getStringExtra(STOCK_NAME);
        tvStockMarketName.setText(mStockName);
        tvStockMarketCode.setText(mStockCode);
        mProgressDialog = DialogUtils.buildProgressDialogsDialog(getContext(), "加载中", "请稍候 " );
        mProgressDialog.show();
        initMarketControlView();
        initMarketTitleAdapter();
        getStockSelfList();
        getMarketTitleInfo();

    }

    // 操作栏
    private void initMarketControlView() {
        try {
            if (App.context().isLogged()) {
                if (StockRuleUtils.isTestAccount()) {
                    llMarketControl.setVisibility(View.GONE);
                    return;
                }
                llMarketControl.setVisibility(View.VISIBLE);
            } else {
                llMarketControl.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 顶部信息
    private void initMarketTitleAdapter() {
        mStockMarketTitleResponseList = new ArrayList<>();
        mStockMarketTitleAdapter = new StockMarketTitleAdapter(mStockMarketTitleResponseList);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        recycleStockMarket.setLayoutManager(manager);
        recycleStockMarket.setAdapter(mStockMarketTitleAdapter);
    }

    // set viewpager
    private void setAdapter(final PagerAdapter adapter) {
        viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    // init chart Fragment list
    private void setTabContent() {
        try {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 判断是否显示自选操作按钮
     */
    private void checkSelfControlVisible() {
        try {
            if (App.context().isLogged()) {
                chkStockMarketControl.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(mStockSelfId)) {
                    chkStockMarketControl.setChecked(false);
                } else {
                    chkStockMarketControl.setChecked(true);
                }
            } else {
                chkStockMarketControl.setVisibility(View.GONE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 更新顶部信息
     *
     * @param resultSingleSplit 顶部信息
     */
    private void notifyTitleAdapter(HashMap<String, String> resultSingleSplit) {
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        try {
            mStockMarketTitleResponseList = new ArrayList<>();
            // 顶部列表
            for (Map.Entry<String, String> stringStringEntry : resultSingleSplit.entrySet()) {
                StockMarketTitleResponse stockMarketTitleResponse = new StockMarketTitleResponse();
                if (stockMarketTitleResponse.getFiveSpeedConstant().contains(stringStringEntry.getKey())) {
                    // 五档
                } else if (stockMarketTitleResponse.getReserveConstant().contains(stringStringEntry.getKey())) {
                    // 保留字段
                } else if (stockMarketTitleResponse.getStockInfoConstant().contains(stringStringEntry.getKey())) {
                    // 股票信息
                } else {
                    // 顶部信息
                    mStockMarketTitleResponseList.add(stockMarketTitleResponse
                            .setTitle(stringStringEntry.getKey())
                            .setContent(stringStringEntry.getValue()));
                }
            }
            // 昨收/今开
            Double toDay = Double.valueOf(resultSingleSplit.get("开盘"));
            Double yesterday = Double.valueOf(resultSingleSplit.get("昨收"));
            // 现价/涨幅1/涨幅2
            String xianjia = resultSingleSplit.get("现价");
            Double xianjiaD = Double.valueOf(xianjia);
            String xianjiaStr = MathUtils.doubleToString(xianjiaD);
            tvMarketToday.setText(MathUtils.doubleToString(toDay));
            tvMarketYesterday.setText(MathUtils.doubleToString(yesterday));
            double zhangDie = MathUtils.getZhangDie(xianjiaD, yesterday);
            double zhangFu = MathUtils.getZhangFu(xianjiaD, yesterday);
            String zhangDieStr = MathUtils.doubleToString(zhangDie);
            String zhangFuStr = MathUtils.doubleToString(zhangFu);
            boolean isUp;
            // tip:涨跌幅有-负数 绿色跌/红色涨
            if (zhangDie < 0) {
                isUp = false;
                tvStockMarketIndex.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_down));
                tvStockPoint.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_down));
                tvStockRate.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_down));
                ivStockPointArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_down));
                tvStockRateArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_down));
            } else {
                isUp = true;
                tvStockMarketIndex.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_up));
                tvStockPoint.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_up));
                tvStockRate.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_up));
                ivStockPointArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_up));
                tvStockRateArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_up));
            }

            tvStockMarketIndex.setText(xianjiaStr);
            tvStockPoint.setText(zhangDieStr);
            tvStockRate.setText(zhangFuStr + "%");

            // 传递五档信息
            mIntentBean = new IntentBean().setUp(isUp)
                    .setStockCode(mStockCode)
                    .setTodayPrice(yesterday)
                    .setStockName(mStockName)
                    .setIndex(xianjiaStr)
                    .setYesterdayPrice(toDay)
                    .setDiefu(zhangDieStr)
                    .setDielv(zhangFuStr)
                    .setUp(isUp)
                    .setStockMarketTitleResponseList(mStockMarketTitleResponseList);

            // 更新顶部信息
            mStockMarketTitleAdapter.setNewData(mStockMarketTitleResponseList);

            // 加载K线图
            if (isFirstLoad) {
                isFirstLoad = false;
                setTabContent();
            }
            // 开启间隔刷新
            intervalUpdateData();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 获取自选股票列表
     * 匹配当前股票是否已添加到自选
     * 更新界面自选状态
     */
    private void getStockSelfList() {
        try {
            checkSelfControlVisible();
            if (App.context().isLogged()) {
                StockSelfReq stockSelfReq = new StockSelfReq().setQueryCommand().setMemberId(App.context().getLoginInfo().getMemberId());
                mSelfListDisposable = DataManager.getInstance().getStockSelfList(stockSelfReq)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stockSelfResponse -> {
                                    if (stockSelfResponse != null && stockSelfResponse.getInfos() != null) {
                                        for (StockSelfResponse.InfosBean infosBean : stockSelfResponse.getInfos()) {
                                            if (TextUtils.equals(mStockCode, infosBean.getStockCode())) {
                                                // 对应股票在自选中的id
                                                mStockSelfId = infosBean.getSelfSelectionId();
                                                break;
                                            }
                                        }
                                    }
                                    checkSelfControlVisible();
                                },
                                throwable -> {});
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取顶部信息
     */
    private void getMarketTitleInfo() {
        List<StockQuotesReq> quotesReqList = new ArrayList<>();
        StockQuotesReq stockQuotesReq = new StockQuotesReq()
                .setStockCode(mStockCode);
        quotesReqList.add(stockQuotesReq);
        mTitleDisposable = StockDataManager.getInstance().getStockQuotes(quotesReqList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockQuotesResponse -> {
                    if (stockQuotesResponse.isOk()) {
                        HashMap<String, String> resultSingleSplit = stockQuotesResponse.getData().getResultSingleSplit();
                        notifyTitleAdapter(resultSingleSplit);
                    }
                }, throwable -> {});
    }

    /**
     * 刷新分时Fragment的数据
     */
    private void refreshKFragmentData() {
        if (mStockPagerAdapter != null) {
            // 仅刷新当前显示Fragment
            int currentItem = viewPager.getCurrentItem();
            switch (currentItem) {
                case 0:
                    // 分时
                    KMinuteChartFragment kMinuteChartFragment = (KMinuteChartFragment) mStockPagerAdapter.getItem(0);
                    kMinuteChartFragment.getFiveSpeedData();
                    kMinuteChartFragment.getDetailData();
                    break;
            }
        }
    }


    @OnClick(R.id.iBtnBack)
    public void onBackListen() {
        finish();
    }


    @OnClick(R.id.chk_stock_market_control)
    public void onAddStockSelfListen() {
        try {
            boolean isChecked = chkStockMarketControl.isChecked();
            if (isChecked) {
                // 添加
                StockSelfReq stockSelfReq = new StockSelfReq()
                        .setAddCommand().setMemberId(App.context().getLoginInfo().getMemberId())
                        .setStockCode(mStockCode)
                        .setStockName(mStockName);
                mSelfDisposable = DataManager.getInstance().getStockSelfList(stockSelfReq)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stockSelfResponse -> {
                            if (stockSelfResponse.isOk()) {
                                // 通知刷新
                                ToastUtil.showToast(getString(R.string.stock_self_add_tip));
                                EventBus.getDefault().postSticky(new StockSelfEvent());
                            }
                        }, throwable -> {
                        });
            } else {
                // 删除
                StockSelfReq stockSelfReq = new StockSelfReq()
                        .setDelCommand().setMemberId(App.context().getLoginInfo().getMemberId())
                        .setSelfSelectionId(mStockSelfId);
                mSelfDisposable = DataManager.getInstance().getStockSelfList(stockSelfReq)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(stockSelfResponse -> {
                            if (stockSelfResponse.isOk()) {
                                // 通知刷新
                                ToastUtil.showToast(getString(R.string.stock_self_del_tip));
                                EventBus.getDefault().postSticky(new StockSelfEvent());
                            }
                        }, throwable -> {
                        });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 买入
     */
    @OnClick(R.id.ll_market_control_buy)
    public void marketBuyClick() {
        // 检查是否登录
        if (!App.context().isLogged()) {
            // 提示登录
            DialogUtils.buildBuyInfoDialog(this, "您未登录", "请先登录", (dialog, which) -> {
                dialog.dismiss();
                startActivity(LoginActivity.startAction(StockMarketActivity.this));
            }).show();

            return;
        }
        startActivity(StockBuyActivity.newIntent(StockMarketActivity.this, mStockCode, mStockName));
    }

    /**
     * 卖出
     */
    @OnClick(R.id.ll_market_control_sell)
    public void marketSellClick() {
        // 检查是否登录
        if (!App.context().isLogged()) {
            // 提示登录
            DialogUtils.buildBuyInfoDialog(this, "您未登录", "请先登录", (dialog, which) -> {
                dialog.dismiss();
                startActivity(LoginActivity.startAction(StockMarketActivity.this));
            }).show();

            return;
        }
        startActivity(StockSellActivity.newIntent(StockMarketActivity.this, mStockCode, mStockName));
    }

    /**
     * 撤单
     */
    @OnClick(R.id.ll_market_control_undo)
    public void marketUndoClick() {
        // 检查是否登录
        if (!App.context().isLogged()) {
            // 提示登录
            DialogUtils.buildBuyInfoDialog(this, "您未登录", "请先登录", (dialog, which) -> {
                dialog.dismiss();
                startActivity(LoginActivity.startAction(StockMarketActivity.this));
            }).show();

            return;
        }
        startActivity(CancelEntrustActivity.startAction(getContext()));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        isFirstLoad = true;

        if (mTitleDisposable != null && !mTitleDisposable.isDisposed()) {
            mTitleDisposable.dispose();
        }

        if (mSelfListDisposable != null && !mSelfListDisposable.isDisposed()) {
            mSelfListDisposable.dispose();
        }

        if (mSelfDisposable != null && !mSelfDisposable.isDisposed()) {
            mSelfDisposable.dispose();
        }

        if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
            mIntervalFiveDisposable.dispose();
        }
    }


    /**
     * 间隔1秒刷新数据
     */
    private void intervalUpdateData() {
        try {
            mIntervalFiveDisposable = Observable.interval(5, TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        System.out.println("***********************************刷新数据");
                        List<StockQuotesReq> quotesReqList = new ArrayList<>();
                        StockQuotesReq stockQuotesReq = new StockQuotesReq()
                                .setStockCode(mStockCode);
                        quotesReqList.add(stockQuotesReq);
                        mTitleDisposable = StockDataManager.getInstance().getStockQuotes(quotesReqList)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(stockQuotesResponse -> {
                                    if (stockQuotesResponse.isOk()) {
                                        HashMap<String, String> resultSingleSplit = stockQuotesResponse.getData().getResultSingleSplit();
                                        updateTitleInfo(resultSingleSplit);
                                    }
                                }, throwable ->{
//                                    // 异常-终止
                                    if (mIntervalFiveDisposable != null&&!mIntervalFiveDisposable.isDisposed()) {
                                        mIntervalFiveDisposable.dispose();
                                    }
                                });
                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 更新顶部信息
    private void updateTitleInfo(HashMap<String, String> resultSingleSplit) {

        try {
            mStockMarketTitleResponseList = new ArrayList<>();
            // 顶部列表
            for (Map.Entry<String, String> stringStringEntry : resultSingleSplit.entrySet()) {
                StockMarketTitleResponse stockMarketTitleResponse = new StockMarketTitleResponse();
                if (stockMarketTitleResponse.getFiveSpeedConstant().contains(stringStringEntry.getKey())) {
                    // 五档
                } else if (stockMarketTitleResponse.getReserveConstant().contains(stringStringEntry.getKey())) {
                    // 保留字段
                } else if (stockMarketTitleResponse.getStockInfoConstant().contains(stringStringEntry.getKey())) {
                    // 股票信息
                } else {
                    // 顶部信息
                    mStockMarketTitleResponseList.add(stockMarketTitleResponse
                            .setTitle(stringStringEntry.getKey())
                            .setContent(stringStringEntry.getValue()));
                }
            }
            // 昨收/今开
            Double toDay = Double.valueOf(resultSingleSplit.get("开盘"));
            Double yesterday = Double.valueOf(resultSingleSplit.get("昨收"));
            // 现价/涨幅1/涨幅2
            String xianjia = resultSingleSplit.get("现价");
            Double xianjiaD = Double.valueOf(xianjia);
            String xianjiaStr = MathUtils.doubleToString(xianjiaD);
            tvMarketToday.setText(MathUtils.doubleToString(toDay));
            tvMarketYesterday.setText(MathUtils.doubleToString(yesterday));
            double zhangDie = MathUtils.getZhangDie(xianjiaD, yesterday);
            double zhangFu = MathUtils.getZhangFu(xianjiaD, yesterday);
            String zhangDieStr = MathUtils.doubleToString(zhangDie);
            String zhangFuStr = MathUtils.doubleToString(zhangFu);
            // tip:涨跌幅有-负数 绿色跌/红色涨
            if (zhangDie < 0) {
                tvStockMarketIndex.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_down));
                tvStockPoint.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_down));
                tvStockRate.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_down));
                ivStockPointArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_down));
                tvStockRateArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_down));
            } else {
                tvStockMarketIndex.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_up));
                tvStockPoint.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_up));
                tvStockRate.setTextColor(ContextCompat.getColor(StockMarketActivity.this, R.color.stock_index_up));
                ivStockPointArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_up));
                tvStockRateArrow.setBackground(ContextCompat.getDrawable(StockMarketActivity.this, R.drawable.ic_stock_index_up));
            }

            tvStockMarketIndex.setText(xianjiaStr);
            tvStockPoint.setText(zhangDieStr);
            tvStockRate.setText(zhangFuStr + "%");

            // 更新顶部信息
            mStockMarketTitleAdapter.setNewData(mStockMarketTitleResponseList);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

}
