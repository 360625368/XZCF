package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.req.StockBuyDoReq;
import com.xzcf.data.data.req.StockQuotesReq;
import com.xzcf.data.data.response.HavingLogResponse;
import com.xzcf.data.data.response.StockListResponse;
import com.xzcf.data.data.response.StockMarketFiveSpeedBean;
import com.xzcf.ui.adapters.BuyFiveAdapter;
import com.xzcf.ui.adapters.HavingAdapter;
import com.xzcf.ui.adapters.SellFiveAdapter;
import com.xzcf.ui.view.CustomLinearLayoutManager;
import com.xzcf.utils.ClickUtils;
import com.xzcf.utils.DialogUtils;
import com.xzcf.utils.MathUtils;
import com.xzcf.utils.PhoneUtils;
import com.xzcf.utils.StockRuleUtils;
import com.xzcf.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class StockSellActivity extends BaseActivity {


    public static final String STOCK_CODE = "stock_code";
    public static final String STOCK_NAME = "stock_name";
    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tv_stock_title)
    TextView tvStockTitle;
    @BindView(R.id.tv_stock_sell_phone)
    TextView tvStockSellPhone;
    @BindView(R.id.iv_stock_sell_refresh)
    ImageView ivStockSellRefresh;
    @BindView(R.id.edt_sell_zhengquan_input)
    EditText edtSellZhengquanInput;
    @BindView(R.id.tv_sell_zhengquan_name)
    TextView tvSellZhengquanName;
    @BindView(R.id.iv_sell_zhengquan_search)
    ImageView ivSellZhengquanSearch;
    @BindView(R.id.tv_sell_xianjia)
    TextView tvSellXianjia;
    @BindView(R.id.ll_sell_xianjia)
    LinearLayout llSellXianjia;
    @BindView(R.id.edt_sell_xianjia_input)
    EditText edtSellXianjiaInput;
    @BindView(R.id.rl_sell_xianjia_reduce)
    RelativeLayout rlSellXianjiaReduce;
    @BindView(R.id.rl_sell_xianjia_add)
    RelativeLayout rlSellXianjiaAdd;
    @BindView(R.id.tv_sell_zhangting)
    TextView tvSellZhangting;
    @BindView(R.id.tv_sell_dieting)
    TextView tvSellDieting;
    @BindView(R.id.edt_sell_shuliang_input)
    EditText edtSellShuliangInput;
    @BindView(R.id.rl_sell_shuliang_reduce)
    RelativeLayout rlSellShuliangReduce;
    @BindView(R.id.rl_sell_shuliang_add)
    RelativeLayout rlSellShuliangAdd;
    @BindView(R.id.tv_sell_kemai)
    TextView tvSellKemai;
    @BindView(R.id.rb_sell_all)
    RadioButton rbSellAll;
    @BindView(R.id.rb_sell_half)
    RadioButton rbSellHalf;
    @BindView(R.id.rb_sell_third)
    RadioButton rbSellThird;
    @BindView(R.id.rg_sell)
    RadioGroup rgSell;
    @BindView(R.id.ll_sell)
    LinearLayout llSell;
    @BindView(R.id.recycle_sell_five)
    RecyclerView recycleSellFive;
    @BindView(R.id.recycle_sell_list)
    RecyclerView recycleSellList;
    @BindView(R.id.recycle_buy_five)
    RecyclerView recycleBuyFive;

    private String mStockCode;
    private String mStockName;
    private int selectNumIndex;
    private Double[] mIntervalPrice;// 价格区间
    private HavingAdapter mHavingAdapter;
    private Double mTodayPrice;
    //private MarketFiveSpeedAdapter mMarketFiveSpeedAdapter;
    private List<StockMarketFiveSpeedBean> mStockMarketFiveSpeedBeanList;
    private HashMap<String, String> mFiveSpeedHashMap;
    private List<HavingLogResponse.LogInfosBean> mLogInfosBeanList;
    private List<StockListResponse.StockBean> stockBeanAllList;
    private Disposable mMoneyDisposable;
    private Disposable mFiveDisposable;
    private Disposable mHavingDisposable;
    private Disposable mBuyDoDisposable;
    private Disposable mIntervalFiveDisposable;
    private Disposable mIntervalSellListDisposable;
    private Disposable mAllStockDisposable;

    private SellFiveAdapter mSellFiveAdapter;
    private BuyFiveAdapter mBuyFiveAdapter;

    private List<StockMarketFiveSpeedBean> mSellFiveList;
    private List<StockMarketFiveSpeedBean> mBuyFiveList;

    public static Intent newIntent(Context context, String stockCode, String stockName) {
        Intent intent = new Intent(context, StockSellActivity.class);
        intent.putExtra(STOCK_CODE, stockCode);
        intent.putExtra(STOCK_NAME, stockName);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_stock_sell);
        ButterKnife.bind(this);
        try{
            mStockCode = getIntent().getStringExtra(STOCK_CODE);
            mStockName = getIntent().getStringExtra(STOCK_NAME);
            rgSell.setOnCheckedChangeListener(mOnCheckedChangeListener);
            initView();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void initView() {
        try{
            setPhone();
            initHavingAdapter();
            setZhengQuanInfo();
            if (!TextUtils.isEmpty(mStockCode)) {
                getFiveSpeedData();
                intervalFiveSpeedData();
                intervalSellList();
            }
            initSellList();
            setStockCodeEditChangeSearchListen();
            getAllStockInfo();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 手动刷新5档列表
    @OnClick(R.id.iv_stock_sell_refresh)
    public void refreshFiveSpeedData(){
        try{
            if (!TextUtils.isEmpty(mStockCode)) {
                getFiveSpeedData();
                initSellList();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void itemClickInitView(){
        setZhengQuanInfo();
    }

    // 设置手机号
    private void setPhone() {
        try{
            String phone = App.context().getLoginInfo().getPhone();
            if (PhoneUtils.isMobileNum(phone)) {
                String maskNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                tvStockSellPhone.setText(maskNumber);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 设置五档列表
    private void initSpeedAdapter() {

        try{
            CustomLinearLayoutManager sellLinearLayoutManager = new CustomLinearLayoutManager(getContext());
            sellLinearLayoutManager.setScrollEnabled(false);

            CustomLinearLayoutManager buyLinearLayoutManager = new CustomLinearLayoutManager(getContext());
            buyLinearLayoutManager.setScrollEnabled(false);

            // 卖
            if (mSellFiveAdapter == null) {
                mSellFiveList = new ArrayList<>();
                recycleSellFive.setLayoutManager(sellLinearLayoutManager);
                mSellFiveAdapter = new SellFiveAdapter(mSellFiveList, mTodayPrice);
                recycleSellFive.setAdapter(mSellFiveAdapter);
            }

            // 五档点击
            mSellFiveAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                try{
                    if (!ClickUtils.isFastClick()) {
                        return;
                    }
                    if (mSellFiveList.size()>0) {
                        StockMarketFiveSpeedBean stockMarketFiveSpeedBean = mSellFiveList.get(position);
                        String price = stockMarketFiveSpeedBean.getPrice();
                        if (!TextUtils.isEmpty(price)) {
                            // 填充价格到输入框
                            edtSellXianjiaInput.setText(MathUtils.doubleToString(Double.valueOf(price)));
                        }

                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            });

            // 买
            if (mBuyFiveAdapter == null) {
                mBuyFiveList = new ArrayList<>();
                recycleBuyFive.setLayoutManager(buyLinearLayoutManager);
                mBuyFiveAdapter = new BuyFiveAdapter(mBuyFiveList, mTodayPrice);
                recycleBuyFive.setAdapter(mBuyFiveAdapter);
            }

            // 卖五档点击
            mBuyFiveAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                try{
                    if (!ClickUtils.isFastClick()) {
                        return;
                    }
                    if (mBuyFiveList.size()>0) {
                        StockMarketFiveSpeedBean stockMarketFiveSpeedBean = mBuyFiveList.get(position);
                        String price = stockMarketFiveSpeedBean.getPrice();
                        if (!TextUtils.isEmpty(price)) {
                            // 填充价格到输入框
                            edtSellXianjiaInput.setText(MathUtils.doubleToString(Double.valueOf(price)));
                        }

                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }


    }

    // 设置买入列表
    private void initHavingAdapter() {
        try{
            mLogInfosBeanList = new ArrayList<>();
            mHavingAdapter = new HavingAdapter(mLogInfosBeanList);
            recycleSellList.setAdapter(mHavingAdapter);
            recycleSellList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.divider_shape);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            if (drawable != null) {
                dividerItemDecoration.setDrawable(drawable);
            }
            recycleSellList.addItemDecoration(dividerItemDecoration);
            setHavingItemClick();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 获取五档信息
     */
    public void getFiveSpeedData() {
        try{
            List<StockQuotesReq> quotesReqList = new ArrayList<>();
            StockQuotesReq stockQuotesReq = new StockQuotesReq()
                    .setStockCode(mStockCode);
            quotesReqList.add(stockQuotesReq);
            mFiveDisposable = StockDataManager.getInstance().getStockQuotes(quotesReqList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stockQuotesResponse -> {
                        if (stockQuotesResponse.isOk()) {
                            mFiveSpeedHashMap = stockQuotesResponse.getData().getResultSingleSplit();
                            setXianJiaInfo(0);
                            mStockMarketFiveSpeedBeanList = MathUtils.sortMarketFiveSpeed(mFiveSpeedHashMap);
                            // 设置五档列表
                            shiftFiveData();
                        }
                    }, throwable -> {
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 每秒刷新一次五档信息
     */
    public void intervalFiveSpeedData(){
        try{
            mIntervalFiveDisposable = Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        List<StockQuotesReq> quotesReqList = new ArrayList<>();
                        StockQuotesReq stockQuotesReq = new StockQuotesReq()
                                .setStockCode(mStockCode);
                        quotesReqList.add(stockQuotesReq);
                        mFiveDisposable = StockDataManager.getInstance().getStockQuotes(quotesReqList)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(stockQuotesResponse -> {
                                    if (stockQuotesResponse.isOk()) {
                                        mFiveSpeedHashMap = stockQuotesResponse.getData().getResultSingleSplit();
                                        mStockMarketFiveSpeedBeanList = MathUtils.sortMarketFiveSpeed(mFiveSpeedHashMap);
                                        // 设置五档列表
                                        shiftFiveData();
                                        // 更新涨跌
                                        updateZhangDie();
                                    }
                                }, throwable -> {
                                });
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 2秒刷新一次 持仓列表
     */
    private void intervalSellList(){
        try{
            mIntervalSellListDisposable = Observable.interval(2, TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        String memberId = App.context().getLoginInfo().getMemberId();
                        mHavingDisposable = DataManager.getInstance().getHavingLog(memberId, "1")
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(havingLogResponse -> {
                                    mLogInfosBeanList = havingLogResponse.getLogInfos();
                                    mHavingAdapter.setNewData(mLogInfosBeanList);
                                }, throwable -> {});
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 卖出记录
     */
    private void initSellList() {
        try{
            String memberId = App.context().getLoginInfo().getMemberId();
            mHavingDisposable = DataManager.getInstance().getHavingLog(memberId, "1")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(havingLogResponse -> {
                        mLogInfosBeanList = havingLogResponse.getLogInfos();
                        setShuLiangInfo(0);
                        mHavingAdapter.setNewData(mLogInfosBeanList);
                    }, throwable -> {});
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }




    // 设置数量
    private void setShuLiangInfo(int index) {
        try{
            if (TextUtils.isEmpty(mStockCode)) {
                return;
            }

//            String zhangting = tvSellZhangting.getText().toString().trim();
//            String dieting = tvSellDieting.getText().toString().trim();
//            if (TextUtils.isEmpty(zhangting)|| TextUtils.isEmpty(dieting)) {
//                tvSellKemai.setText("");
//                return;
//            }

            String countCan = "0";
            for (HavingLogResponse.LogInfosBean logInfosBean : mLogInfosBeanList) {
                if (TextUtils.equals(logInfosBean.getStockCode(),mStockCode)) {
                    countCan = logInfosBean.getCountCan();
                    break;
                }
            }
            tvSellKemai.setText(countCan);
            if (index == 0) {
                // 全部
                if (TextUtils.equals(countCan,"0")) {
                    edtSellShuliangInput.setText(countCan);
                    return;
                }
//                double num = Math.floor(Double.valueOf(countCan));
//                double fetch = num % 100;
//                double all = num - fetch;
//                edtSellShuliangInput.setText(MathUtils.doubleToStringByZero(all));
                edtSellShuliangInput.setText(countCan);
            } else if (index == 1) {
                // 1/2
                if (TextUtils.equals(countCan,"0")) {
                    return;
                }
                double num = Math.floor(Double.valueOf(countCan));
                double half = num * 0.5;
                double fetch = half % 100;
                half -= fetch;
                edtSellShuliangInput.setText(MathUtils.doubleToStringByZero(half));
            } else if (index == 2) {
                // 1/3
                if (TextUtils.equals(countCan,"0")) {
                    return;
                }
                double num = Math.floor(Double.valueOf(countCan));
                double half = num * 0.33;
                double fetch = half % 100;
                half -= fetch;
                edtSellShuliangInput.setText(MathUtils.doubleToStringByZero(half));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 设置证券信息
    private void setZhengQuanInfo() {
        if (!TextUtils.isEmpty(mStockCode)&& !TextUtils.isEmpty(mStockName)) {
            edtSellZhengquanInput.setText(mStockCode);
            tvSellZhengquanName.setText(mStockName);
        }
    }

    // 设置现价
    private void setXianJiaInfo(int index) {
        try{
            if (index == 0) {
                if (mFiveSpeedHashMap != null && mFiveSpeedHashMap.get("现价") != null) {
                    String sXianJia = MathUtils.doubleToString(Double.valueOf(mFiveSpeedHashMap.get("现价")));
                    edtSellXianjiaInput.setText(sXianJia);
                    String xianjia = getString(R.string.stock_buy_xianjia_dialog_xianjia);
                    tvSellXianjia.setText(xianjia);
                }
            } else {
                edtSellXianjiaInput.setText("0");
            }
            // 涨停/跌停
            if (mFiveSpeedHashMap != null && mFiveSpeedHashMap.get("昨收") != null) {
                String yesterday = mFiveSpeedHashMap.get("昨收");
                mTodayPrice = Double.valueOf(yesterday);
                String zhangTing = StockRuleUtils.calculateZhangTing(mStockName,yesterday);
                String dieTing = StockRuleUtils.calculateDieTing(mStockName,yesterday);
                tvSellZhangting.setText(zhangTing);
                tvSellDieting.setText(dieTing);
                // 价格区间
                String s = MathUtils.doubleToString(Double.valueOf(yesterday));
                mIntervalPrice =  StockRuleUtils.buyPriceCheckInterval(mStockName,s);
                // 五档
                initSpeedAdapter();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    private void updateZhangDie(){
        try{
            // 涨停/跌停
            if (mFiveSpeedHashMap != null && mFiveSpeedHashMap.get("昨收") != null) {
                String yesterday = mFiveSpeedHashMap.get("昨收");
                mTodayPrice = Double.valueOf(yesterday);
                String zhangTing = StockRuleUtils.calculateZhangTing(mStockName,yesterday);
                String dieTing = StockRuleUtils.calculateDieTing(mStockName,yesterday);
                tvSellZhangting.setText(zhangTing);
                tvSellDieting.setText(dieTing);
                // 价格区间
                String s = MathUtils.doubleToString(Double.valueOf(yesterday));
                mIntervalPrice =  StockRuleUtils.buyPriceCheckInterval(mStockName,s);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 数量选择 radiogroup
     */
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = (radioGroup, checkedId) -> {
        try{
            int id = radioGroup.getCheckedRadioButtonId();
            switch (id) {
                case R.id.rb_sell_all:
                    setShuLiangInfo(0);
                    break;
                case R.id.rb_sell_half:
                    setShuLiangInfo(1);
                    break;
                case R.id.rb_sell_third:
                    setShuLiangInfo(2);
                    break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    };

    private void getAllStockInfo(){
        try{
            mAllStockDisposable = StockDataManager.getInstance().getMemHomeList().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stockListResponse -> {
                                stockBeanAllList = stockListResponse.getData();
                            },
                            throwable -> {});
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



    /**
     * 返回
     */
    @OnClick(R.id.iBtnBack)
    public void onBackClick() {
        finish();
    }

    /**
     * 购票搜索
     */
    @OnClick(R.id.iv_sell_zhengquan_search)
    public void onXianJiaSearchClick() {
        startActivityForResult(SearchActivity.newIntent(this, true), 1);
    }

    /**
     * 限价/现价
     */
    @OnClick(R.id.ll_sell_xianjia)
    public void onXianJiaClick() {

        try{
            String title = getString(R.string.stock_buy_xianjia_dialog_title);
            String xianjia = getString(R.string.stock_buy_xianjia_dialog_xianjia);
            String xianjia2 = getString(R.string.stock_buy_xianjia_dialog_xianjia2);
            String cancel = getString(R.string.stock_buy_xianjia_dialog_cancel);
            List<String> titleList = new ArrayList<>();
            titleList.add(xianjia);
            titleList.add(xianjia2);

            MaterialDialog.ListCallback listCallback = (dialog, itemView, position, text) -> {
                if (position == 0) {
                    tvSellXianjia.setText(xianjia);
                } else {
                    tvSellXianjia.setText(xianjia2);
                }
                setXianJiaInfo(position);
            };
            MaterialDialog materialDialog = DialogUtils.buildXianJiaChoiceDialog(this, title, cancel, titleList, listCallback);
            materialDialog.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 现价减 -
     * 减幅 0.01
     */
    @OnClick(R.id.rl_sell_xianjia_reduce)
    public void onXianJiaReduceClick() {
        try {
            String xianjiaStr = edtSellXianjiaInput.getText().toString().trim();
            if (TextUtils.isEmpty(xianjiaStr)) {
                return;
            }
            Double xianjiaDouble = Double.valueOf(xianjiaStr);
            if (xianjiaDouble > 0) {
                xianjiaDouble -= 0.01;
                edtSellXianjiaInput.setText(MathUtils.doubleToString(xianjiaDouble));
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 现价加 -
     * 增幅 0.01
     */
    @OnClick(R.id.rl_sell_xianjia_add)
    public void onXianJiaAddClick() {
        try {
            String xianjiaStr = edtSellXianjiaInput.getText().toString().trim();
            if (TextUtils.isEmpty(xianjiaStr)) {
                return;
            }
            Double xianjiaDouble = Double.valueOf(xianjiaStr);
            xianjiaDouble += 0.01;
            edtSellXianjiaInput.setText(MathUtils.doubleToString(xianjiaDouble));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 数量减 -
     * 减幅 100
     */
    @OnClick(R.id.rl_sell_shuliang_reduce)
    public void onShuLiangReduceClick() {
        try {
            String shuliangStr = edtSellShuliangInput.getText().toString().trim();
            if (shuliangStr.startsWith("0")) {
                edtSellShuliangInput.setText("0");
                return;
            }
            if (TextUtils.isEmpty(shuliangStr)||TextUtils.equals(shuliangStr,"NaN")) {
                return;
            }
            Integer shuliangInt = Integer.valueOf(shuliangStr);
            if (shuliangInt > 100) {
                shuliangInt -= 100;
                edtSellShuliangInput.setText(String.valueOf(shuliangInt));
            }else{
                edtSellShuliangInput.setText("0");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 数量加 +
     * 增幅 100
     */
    @OnClick(R.id.rl_sell_shuliang_add)
    public void onShuLiangAddClick() {
        try {
            String shuliangStr = edtSellShuliangInput.getText().toString().trim();
            if (TextUtils.isEmpty(shuliangStr)||TextUtils.equals(shuliangStr,"NaN")) {
                return;
            }
            Integer shuliangInt = Integer.valueOf(shuliangStr);
            shuliangInt += 100;
            edtSellShuliangInput.setText(String.valueOf(shuliangInt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 买入记录点击
     */
    private void setHavingItemClick(){
        try{
            mHavingAdapter.setOnItemChildClickListener((adapter, view, position) -> {
                if (!ClickUtils.isFastClick()) {
                    return;
                }
                HavingLogResponse.LogInfosBean logInfosBean = mLogInfosBeanList.get(position);
                // 查询对应的股票
                mStockName = logInfosBean.getStockName();
                mStockCode = logInfosBean.getStockCode();
                itemClickInitView();
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 提交卖出
     */
    private void putSellDoInfo(){
        try{
            String jiaGeStr = edtSellXianjiaInput.getText().toString().trim();
            String shuLiangStr = edtSellShuliangInput.getText().toString().trim();
            String memberId = App.context().getLoginInfo().getMemberId();
            StockBuyDoReq stockBuyDoReq = new StockBuyDoReq()
                    .setMemberId(memberId)
                    .setStockCode(mStockCode)
                    .setStockName(mStockName)
                    .setAllPrice(jiaGeStr)
                    .setAllCount(shuLiangStr)
                    .setType("1");
            mBuyDoDisposable = DataManager.getInstance().putBuyDo(stockBuyDoReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(baseRes -> DialogUtils.
                                    buildBuyInfoDialog(StockSellActivity.this, getString(R.string.stock_buy_info_title),
                                            baseRes.getMsg(), (dialog, which) ->initSellList()).show(),
                            throwable -> {});

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 卖出
     */
    @OnClick(R.id.ll_sell)
    public void onSellClick() {
        try{
            // 检查是否登录
            if (!App.context().isLogged()) {
                // 提示登录
                DialogUtils.buildBuyInfoDialog(this, "您未登录", "请先登录", (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(LoginActivity.startAction(StockSellActivity.this));
                    finish();
                }).show();

                return;
            }
            // 检查输入
            // 股票代码是否正确、价格是否输入、买入数量是否大于可买数、输入价格是否在价格区间内
            String jiaGeStr = edtSellXianjiaInput.getText().toString().trim();
            String kemaiStr = tvSellKemai.getText().toString().trim();
            String shuLiangStr = edtSellShuliangInput.getText().toString().trim();
            String xianjiaFangshi = tvSellXianjia.getText().toString().trim();
            String codeStr = edtSellZhengquanInput.getText().toString();
            String zhangting = tvSellZhangting.getText().toString().trim();
            String dieting = tvSellDieting.getText().toString().trim();

            if (!checkPriceBetween(jiaGeStr) || TextUtils.isEmpty(zhangting) || TextUtils.isEmpty(dieting)) {
                ToastUtil.showToast("卖出价格不得超过涨停~跌停价格,请重新输入！");
                return;
            }
            if (!StockRuleUtils.isTrueStockCode(codeStr)) {
                ToastUtil.showToast("请选择正确股票代码！");
                return;
            }
            if (TextUtils.isEmpty(shuLiangStr)||
                    TextUtils.isEmpty(kemaiStr)) {
                ToastUtil.showToast("请检查输入");
                return;
            }

            if (!TextUtils.isEmpty(jiaGeStr)
                    && !TextUtils.equals(jiaGeStr,"0")
                    && Double.valueOf(shuLiangStr)>0
                    && Double.valueOf(kemaiStr)>=Double.valueOf(shuLiangStr)
                    ) {
                // 弹窗提示
                String title = getString(R.string.stock_sell_dialog_title);
                String confirm = getString(R.string.stock_buy_xianjia_dialog_confirm);
                String cancel = getString(R.string.stock_buy_xianjia_dialog_cancel);
                DialogUtils.buildBuyDialog(this, title,
                        mStockName,
                        mStockCode,
                        xianjiaFangshi,
                        jiaGeStr,
                        shuLiangStr,
                        cancel,
                        confirm, (dialog, which) -> {
                            // 提交卖出
                            putSellDoInfo();
                        }).show();
            }else{
                // 提示输入有误！
                ToastUtil.showToast("卖出数量需小于可卖数量！");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkPriceBetween(String inputPrice) {
        if (TextUtils.isEmpty(inputPrice)) {
            return false;
        }
        Double priceDouble = 0d;
        try {
            priceDouble = Double.valueOf(inputPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return !TextUtils.isEmpty(inputPrice) && mIntervalPrice != null && priceDouble >= mIntervalPrice[0] && priceDouble <= mIntervalPrice[1];
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (requestCode == 1) {
                if (data == null) {
                    return;
                }
                mStockCode = data.getStringExtra(STOCK_CODE);
                mStockName = data.getStringExtra(STOCK_NAME);
                setZhengQuanInfo();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMoneyDisposable != null && !mMoneyDisposable.isDisposed()) {
            mMoneyDisposable.dispose();
        }

        if (mFiveDisposable != null && !mFiveDisposable.isDisposed()) {
            mFiveDisposable.dispose();
        }

        if (mHavingDisposable != null && !mHavingDisposable.isDisposed()) {
            mHavingDisposable.dispose();
        }

        if (mBuyDoDisposable != null && !mBuyDoDisposable.isDisposed()) {
            mBuyDoDisposable.dispose();
        }

        if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
            mIntervalFiveDisposable.dispose();
        }

        if (mAllStockDisposable != null && !mAllStockDisposable.isDisposed()) {
            mAllStockDisposable.dispose();
        }

        if (mIntervalSellListDisposable != null && !mIntervalSellListDisposable.isDisposed()) {
            mIntervalSellListDisposable.dispose();
        }
    }


    // 输入证券号码搜索指定股票
    private void setStockCodeEditChangeSearchListen(){
        try{
            edtSellZhengquanInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                        mIntervalFiveDisposable.dispose();
                    }

                    if (mIntervalSellListDisposable != null && !mIntervalSellListDisposable.isDisposed()) {
                        mIntervalSellListDisposable.dispose();
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                        mIntervalFiveDisposable.dispose();
                    }

                    if (mIntervalSellListDisposable != null && !mIntervalSellListDisposable.isDisposed()) {
                        mIntervalSellListDisposable.dispose();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                        mIntervalFiveDisposable.dispose();
                    }

                    if (mIntervalSellListDisposable != null && !mIntervalSellListDisposable.isDisposed()) {
                        mIntervalSellListDisposable.dispose();
                    }
                    if (!TextUtils.isEmpty(editable)) {
                        String code = editable.toString();
                        if (TextUtils.isEmpty(code)) {
                            return;
                        }
                        if (code.length()==6) {
                            // 查询
                            searchStockInfoByStockCode(code);
                        }
                    }
                }
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 查询股票信息 根据股票代码
    private void searchStockInfoByStockCode(String stockCodeStr){
        try{
            mStockCode = stockCodeStr;
            final int size = stockBeanAllList.size();
            List<Boolean> booleanList = new ArrayList<>();
            Disposable subscribe = Observable.fromIterable(stockBeanAllList)
                    .filter(stockBean -> {
                        String code = stockBean.getStockCode();
                        boolean b = !TextUtils.isEmpty(code) && code.contains(stockCodeStr);
                        booleanList.add(b);
                        if (booleanList.size()==size && !booleanList.contains(true)) {
                            // 清空界面
                            clearView();
                        }
                        return b;
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(stockBean -> {
                        // 查询五档信息
                        mStockCode = stockBean.getStockCode();
                        mStockName = stockBean.getStockName();
                        if (TextUtils.isEmpty(mStockName)) {
                            return;
                        }
                        tvSellZhengquanName.setText(mStockName);
                        if (!TextUtils.isEmpty(mStockCode)) {
                            getFiveSpeedData();
                        }
                        setShuLiangInfo(0);

                        intervalFiveSpeedData();
                        intervalSellList();

                    }, throwable -> {});
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 筛选排序 五档信息
    private void shiftFiveData(){
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

        mSellFiveAdapter.setTodayPrice(mTodayPrice);
        mSellFiveAdapter.setNewData(mSellFiveList);

        mBuyFiveAdapter.setTodayPrice(mTodayPrice);
        mBuyFiveAdapter.setNewData(mBuyFiveList);
    }


    private void clearView() {

        if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
            mIntervalFiveDisposable.dispose();
        }

        if (mIntervalSellListDisposable != null && !mIntervalSellListDisposable.isDisposed()) {
            mIntervalSellListDisposable.dispose();
        }

        Disposable subscribe = Flowable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    // 股票名称
                    tvSellZhengquanName.setText("");
                    // 清空现价
                    edtSellXianjiaInput.setText("");
                    // .. 涨停 跌停
                    tvSellZhangting.setText("");
                    tvSellDieting.setText("");
                    // .. 数量
                    edtSellShuliangInput.setText("");
                    // .. 可买
                    tvSellKemai.setText("");
                    // .. 买卖
                    mSellFiveList = new ArrayList<>();
                    mBuyFiveList = new ArrayList<>();
                    if (mSellFiveAdapter != null) {
                        mSellFiveAdapter.setNewData(mSellFiveList);
                    }
                    if (mBuyFiveAdapter != null) {
                        mBuyFiveAdapter.setNewData(mBuyFiveList);
                    }
                });
    }

}
