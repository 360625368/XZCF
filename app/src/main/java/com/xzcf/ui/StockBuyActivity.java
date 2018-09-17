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
import android.widget.RadioGroup;
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


public class StockBuyActivity extends BaseActivity {


    public static final String STOCK_CODE = "stock_code";
    public static final String STOCK_NAME = "stock_name";
    @BindView(R.id.recycle_buy_five)
    RecyclerView recycleBuyFive;
    @BindView(R.id.tv_stock_buy_phone)
    TextView tvStockBuyPhone;
    @BindView(R.id.tv_xianjia)
    TextView tvXianjia;
    @BindView(R.id.edt_zhengquan_input)
    EditText edtZhengquanInput;
    @BindView(R.id.tv_zhengquan_name)
    TextView tvZhengquanName;
    @BindView(R.id.edt_xianjia_input)
    EditText edtXianjiaInput;
    @BindView(R.id.edt_shuliang_input)
    EditText edtShuliangInput;
    @BindView(R.id.rg_buy)
    RadioGroup rgBuy;
    @BindView(R.id.tv_kemai)
    TextView tvKemai;
    @BindView(R.id.recycle_buy_list)
    RecyclerView recycleBuyList;
    @BindView(R.id.tv_zhangting)
    TextView tvZhangting;
    @BindView(R.id.tv_dieting)
    TextView tvDieting;
    @BindView(R.id.recycle_sell_five)
    RecyclerView recycleSellFive;

    private String mStockCode;
    private String mStockName;
    private String mTakeBlance;
    private int selectNumIndex;
    private Double[] mIntervalPrice;// 价格区间
    private Double mTodayPrice;
    private HavingAdapter mHavingAdapter;
    private List<StockMarketFiveSpeedBean> mStockMarketFiveSpeedBeanList;
    private HashMap<String, String> mFiveSpeedHashMap;
    private List<HavingLogResponse.LogInfosBean> mLogInfosBeanList;
    private List<StockListResponse.StockBean> stockBeanAllList;
    private Disposable mMoneyDisposable;
    private Disposable mFiveDisposable;
    private Disposable mHavingDisposable;
    private Disposable mBuyDoDisposable;
    private Disposable mIntervalFiveDisposable;
    private Disposable mIntervalBuyListDisposable;
    private Disposable mAllStockDisposable;

    private SellFiveAdapter mSellFiveAdapter;
    private BuyFiveAdapter mBuyFiveAdapter;

    private List<StockMarketFiveSpeedBean> mSellFiveList;
    private List<StockMarketFiveSpeedBean> mBuyFiveList;

    public static Intent newIntent(Context context, String stockCode, String stockName) {
        Intent intent = new Intent(context, StockBuyActivity.class);
        intent.putExtra(STOCK_CODE, stockCode);
        intent.putExtra(STOCK_NAME, stockName);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_stock_buy);
        ButterKnife.bind(this);
        try{
            mStockCode = getIntent().getStringExtra(STOCK_CODE);
            mStockName = getIntent().getStringExtra(STOCK_NAME);
            rgBuy.setOnCheckedChangeListener(mOnCheckedChangeListener);
            setEdtChangedListen();
            initView();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void initView() {
        try{
            setPhone();
            initHavingAdapter();
            getTakeBlanceData();
            setZhengQuanInfo();
            if (!TextUtils.isEmpty(mStockCode)) {
                getFiveSpeedData();
                intervalFiveSpeedData();
                intervalBuyList();
            }
            initBuyList();
            setStockCodeEditChangeSearchListen();
            getAllStockInfo();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 手动刷新5档列表
    @OnClick(R.id.iv_stock_buy_refresh)
    public void refreshFiveSpeedData(){
        try{
            if (!TextUtils.isEmpty(mStockCode)) {
                getFiveSpeedData();
                initBuyList();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void itemClickInitView(){
       try{
           getTakeBlanceData();
           setZhengQuanInfo();
       }catch(Exception ex){
           ex.printStackTrace();
       }
    }

    // 设置手机号
    private void setPhone() {
        try{
            String phone = App.context().getLoginInfo().getPhone();
            System.out.println(phone);
            if (PhoneUtils.isMobileNum(phone)) {
                String maskNumber = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                tvStockBuyPhone.setText(maskNumber);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 设置证券信息
    private void setZhengQuanInfo() {
        if (!TextUtils.isEmpty(mStockCode)) {
            edtZhengquanInput.setText(mStockCode);
            tvZhengquanName.setText(mStockName);
        }
    }

    // 设置现价
    private void setXianJiaInfo(int index) {
        try{
            if (index == 0) {
                if (mFiveSpeedHashMap != null && mFiveSpeedHashMap.get("现价") != null) {
                    String sXianJia = MathUtils.doubleToString(Double.valueOf(mFiveSpeedHashMap.get("现价")));
                    edtXianjiaInput.setText(sXianJia);
                    setShuLiangInfo(-1);
                    String xianjia = getString(R.string.stock_buy_xianjia_dialog_xianjia);
                    tvXianjia.setText(xianjia);
                }
            } else {
                edtXianjiaInput.setText("0");
            }
            // 涨停/跌停
            if (mFiveSpeedHashMap != null && mFiveSpeedHashMap.get("昨收") != null) {
                String yesterday = mFiveSpeedHashMap.get("昨收");
                mTodayPrice = Double.valueOf(yesterday);
                String zhangTing = StockRuleUtils.calculateZhangTing(mStockName,yesterday);
                String dieTing = StockRuleUtils.calculateDieTing(mStockName,yesterday);
                tvZhangting.setText(zhangTing);
                tvDieting.setText(dieTing);
                // 价格区间
                mIntervalPrice =  StockRuleUtils.buyPriceCheckInterval(mStockName,yesterday);
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
                tvZhangting.setText(zhangTing);
                tvDieting.setText(dieTing);
                // 价格区间
                mIntervalPrice =  StockRuleUtils.buyPriceCheckInterval(mStockName,yesterday);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    // 设置数量
    private void setShuLiangInfo(int index) {
        try{
            // 可用余额
            if (!TextUtils.isEmpty(mTakeBlance)) {
                selectNumIndex = index;
                String balanceTake = mTakeBlance;
                String jiaGeStr = edtXianjiaInput.getText().toString().trim();
                if (TextUtils.isEmpty(jiaGeStr)||TextUtils.equals(jiaGeStr,"0")) {
                    return;
                }
                Double blanceDouble = Double.valueOf(balanceTake);
                Double jisGeDouble = Double.valueOf(jiaGeStr);
                double num = Math.floor(blanceDouble / jisGeDouble);
                setKeMaiInfo(num);
                if (index==-1) {
                    // 点击下方查询余额
                    edtShuliangInput.setText("");
                    return;
                }
                // 总金额除以单价就是数量
                if (index == 0) {
                    // 全部
                    double fetch = num % 100;
                    double all = num - fetch;
                    String s = MathUtils.doubleToStringByZero(all);
                    if (TextUtils.equals(s,"NaN")) {
                        edtShuliangInput.setText("0");
                        return;
                    }
                    edtShuliangInput.setText(s);
                } else if (index == 1) {
                    // 1/2
                    double half = num * 0.5;
                    double fetch = half % 100;
                    half -= fetch;
                    String s = MathUtils.doubleToStringByZero(half);
                    if (TextUtils.equals(s,"NaN")) {
                        edtShuliangInput.setText("0");
                        return;
                    }
                    edtShuliangInput.setText(s);
                } else if (index == 2) {
                    // 1/3
                    double half = num * 0.33;
                    double fetch = half % 100;
                    half -= fetch;
                    String s = MathUtils.doubleToStringByZero(half);
                    if (TextUtils.equals(s,"NaN")) {
                        edtShuliangInput.setText("0");
                        return;
                    }
                    edtShuliangInput.setText(MathUtils.doubleToStringByZero(half));
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    // 设置可买数量
    private void setKeMaiInfo(double num) {
        try{
//            String zhangting = tvZhangting.getText().toString().trim();
//            String dieting = tvDieting.getText().toString().trim();
//
//            if (TextUtils.isEmpty(zhangting)|| TextUtils.isEmpty(dieting)) {
//                tvKemai.setText("");
//                return;
//            }

            // 全部
            double fetch = num % 100;
            double all = num - fetch;
            String s = MathUtils.doubleToStringByZero(all);
            System.out.println("可买:"+s);
            if (TextUtils.equals(s,"NaN")) {
                tvKemai.setText("0");
                return;
            }
            tvKemai.setText(s);
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

            if (mSellFiveAdapter == null) {
                mSellFiveList = new ArrayList<>();
                recycleSellFive.setLayoutManager(sellLinearLayoutManager);
                mSellFiveAdapter = new SellFiveAdapter(mSellFiveList, mTodayPrice);
                recycleSellFive.setAdapter(mSellFiveAdapter);
            }

            // 五档点击 卖
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
                            edtXianjiaInput.setText(MathUtils.doubleToString(Double.valueOf(price)));
                        }

                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            });

            if (mBuyFiveAdapter == null) {
                mBuyFiveList = new ArrayList<>();
                recycleBuyFive.setLayoutManager(buyLinearLayoutManager);
                mBuyFiveAdapter = new BuyFiveAdapter(mBuyFiveList, mTodayPrice);
                recycleBuyFive.setAdapter(mBuyFiveAdapter);
            }

            // 五档点击 买
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
                            edtXianjiaInput.setText(MathUtils.doubleToString(Double.valueOf(price)));
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
     * 2秒钟刷新一次 买入列表
     */
    private void intervalBuyList(){
        mIntervalBuyListDisposable = Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(aLong -> initBuyList());
    }


    // 设置买入列表
    private void initHavingAdapter() {
        try{
            mLogInfosBeanList = new ArrayList<>();
            mHavingAdapter = new HavingAdapter(mLogInfosBeanList);
            recycleBuyList.setAdapter(mHavingAdapter);
            recycleBuyList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.divider_shape);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
            if (drawable != null) {
                dividerItemDecoration.setDrawable(drawable);
            }
            recycleBuyList.addItemDecoration(dividerItemDecoration);
            setHavingItemClick();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 获取用户资金
     */
    public void getTakeBlanceData() {
        try{
            String memberId = App.context().getLoginInfo().getMemberId();
            mMoneyDisposable = DataManager.getInstance().getMoney(memberId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(memMoneyResponse -> {
                        mTakeBlance = memMoneyResponse.getLogInfo().getBalanceTake();
                        setShuLiangInfo(-1);
                    }, throwable -> {
                    });
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
                            shiftFiveData();
                        }
                    }, throwable -> {
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    /**
     * 买入记录
     */
    private void initBuyList() {
        try{
            String memberId = App.context().getLoginInfo().getMemberId();
            mHavingDisposable = DataManager.getInstance().getHavingLog(memberId, "1")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(havingLogResponse -> {
                        mLogInfosBeanList = havingLogResponse.getLogInfos();
                        mHavingAdapter.setNewData(havingLogResponse.getLogInfos());
                    }, throwable -> {});
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

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
    @OnClick(R.id.iv_zhengquan_search)
    public void onXianJiaSearchClick() {
        startActivityForResult(SearchActivity.newIntent(this, true), 0);
    }

    /**
     * 限价/现价
     */
    @OnClick(R.id.ll_xianjia)
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
                    tvXianjia.setText(xianjia);
                } else {
                    tvXianjia.setText(xianjia2);
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
     * 提交购买
     */
    private void putBuyDoInfo(){
        try{
            String jiaGeStr = edtXianjiaInput.getText().toString().trim();
            String shuLiangStr = edtShuliangInput.getText().toString().trim();
            String memberId = App.context().getLoginInfo().getMemberId();
            StockBuyDoReq stockBuyDoReq = new StockBuyDoReq()
                    .setMemberId(memberId)
                    .setStockCode(mStockCode)
                    .setStockName(mStockName)
                    .setAllPrice(jiaGeStr)
                    .setAllCount(shuLiangStr)
                    .setType("0");
            mBuyDoDisposable = DataManager.getInstance().putBuyDo(stockBuyDoReq)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(baseRes -> DialogUtils.
                                    buildBuyInfoDialog(StockBuyActivity.this, getString(R.string.stock_buy_info_title),
                                            baseRes.getMsg(), (dialog, which) -> initBuyList()).show(),
                            throwable -> {});
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    /**
     * 价格输入监听
     */
    private void setEdtChangedListen() {
        try{
            edtXianjiaInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setShuLiangInfo(selectNumIndex);
                }
            });
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
                case R.id.rb_buy_all:
                    setShuLiangInfo(0);
                    break;
                case R.id.rb_buy_half:
                    setShuLiangInfo(1);
                    break;
                case R.id.rb_buy_third:
                    setShuLiangInfo(2);
                    break;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    };


    /**
     * 现价减 -
     * 减幅 0.01
     */
    @OnClick(R.id.rl_xianjia_reduce)
    public void onXianJiaReduceClick() {
        try {
            String xianjiaStr = edtXianjiaInput.getText().toString().trim();
            if (TextUtils.isEmpty(xianjiaStr)) {
                return;
            }
            Double xianjiaDouble = Double.valueOf(xianjiaStr);
            if (xianjiaDouble > 0) {
                xianjiaDouble -= 0.01;
                edtXianjiaInput.setText(MathUtils.doubleToString(xianjiaDouble));
            }
        }catch (Exception ex){
         ex.printStackTrace();
        }
    }

    /**
     * 现价加 -
     * 增幅 0.01
     */
    @OnClick(R.id.rl_xianjia_add)
    public void onXianJiaAddClick() {
        try {
            String xianjiaStr = edtXianjiaInput.getText().toString().trim();
            if (TextUtils.isEmpty(xianjiaStr)) {
                return;
            }
            Double xianjiaDouble = Double.valueOf(xianjiaStr);
            xianjiaDouble += 0.01;
            edtXianjiaInput.setText(MathUtils.doubleToString(xianjiaDouble));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 数量减 -
     * 减幅 100
     */
    @OnClick(R.id.rl_shuliang_reduce)
    public void onShuLiangReduceClick() {

        try {
            String shuliangStr = edtShuliangInput.getText().toString().trim();
            if (TextUtils.isEmpty(shuliangStr)||TextUtils.equals(shuliangStr,"NaN")) {
                return;
            }
            if (shuliangStr.startsWith("0")) {
                edtShuliangInput.setText("0");
                return;
            }
            Integer shuliangInt = Integer.valueOf(shuliangStr);
            if (shuliangInt >100) {
                shuliangInt -= 100;
                edtShuliangInput.setText(String.valueOf(shuliangInt));
            }else{
                edtShuliangInput.setText("0");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 数量加 +
     * 增幅 100
     */
    @OnClick(R.id.rl_shuliang_add)
    public void onShuLiangAddClick() {

        try {
            String shuliangStr = edtShuliangInput.getText().toString().trim();
            if (TextUtils.isEmpty(shuliangStr)||TextUtils.equals(shuliangStr,"NaN")) {
                return;
            }
            Integer shuliangInt = Integer.valueOf(shuliangStr);
            shuliangInt += 100;
            edtShuliangInput.setText(String.valueOf(shuliangInt));
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
     * 买入
     */
    @OnClick(R.id.ll_buy)
    public void onBuyClick() {
        try{
            // 检查是否登录
            if (!App.context().isLogged()) {
                // 提示登录
                DialogUtils.buildBuyInfoDialog(this, "您未登录", "请先登录", (dialog, which) -> {
                    dialog.dismiss();
                    startActivity(LoginActivity.startAction(StockBuyActivity.this));
                    finish();
                }).show();

                return;
            }

            // 检查输入
            // 股票代码是否正确、价格是否输入、买入数量是否大于可买数、输入价格是否在价格区间内
            String jiaGeStr = edtXianjiaInput.getText().toString().trim();
            String kemaiStr = tvKemai.getText().toString().trim();
            String shuLiangStr = edtShuliangInput.getText().toString().trim();
            String xianjiaFangshi = tvXianjia.getText().toString().trim();
            String codeStr = edtZhengquanInput.getText().toString();
            String zhangting = tvZhangting.getText().toString().trim();
            String dieting = tvDieting.getText().toString().trim();
            if (!checkPriceBetween(jiaGeStr) || TextUtils.isEmpty(zhangting) || TextUtils.isEmpty(dieting)) {
                ToastUtil.showToast("买入价格不得超过涨停~跌停价格,请重新输入！");
                return;
            }
            if (TextUtils.isEmpty(shuLiangStr)||
                    TextUtils.isEmpty(kemaiStr)) {
                ToastUtil.showToast("请检查输入");
                return;
            }

            if (StockRuleUtils.isTrueStockCode(codeStr)
                    && !TextUtils.isEmpty(jiaGeStr)
                    && !TextUtils.equals(jiaGeStr,"0")
                    && Double.valueOf(shuLiangStr)>0
                    && Double.valueOf(kemaiStr)>=Double.valueOf(shuLiangStr)
                    ) {
                // 弹窗提示
                String title = getString(R.string.stock_buy_dialog_title);
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
                            // 提交购买
                            putBuyDoInfo();

                        }).show();
            }else{
                // 提示输入有误！
                ToastUtil.showToast("请检查内容输入是否正确！");
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean checkPriceBetween(String inputPrice) {
        if (TextUtils.isEmpty(inputPrice)) {
            return false;
        }
        Double priceDouble = Double.valueOf(inputPrice);
        return !TextUtils.isEmpty(inputPrice) && mIntervalPrice != null && priceDouble >= mIntervalPrice[0] && priceDouble <= mIntervalPrice[1];
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if (resultCode == 0) {
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

        if (mIntervalBuyListDisposable != null && !mIntervalBuyListDisposable.isDisposed()) {
            mIntervalBuyListDisposable.dispose();
        }
    }

    // 输入证券号码搜索指定股票
    private void setStockCodeEditChangeSearchListen(){
        try{
            edtZhengquanInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                        mIntervalFiveDisposable.dispose();
                    }

                    if (mIntervalBuyListDisposable != null && !mIntervalBuyListDisposable.isDisposed()) {
                        mIntervalBuyListDisposable.dispose();
                    }
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                        mIntervalFiveDisposable.dispose();
                    }

                    if (mIntervalBuyListDisposable != null && !mIntervalBuyListDisposable.isDisposed()) {
                        mIntervalBuyListDisposable.dispose();
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                        mIntervalFiveDisposable.dispose();
                    }

                    if (mIntervalBuyListDisposable != null && !mIntervalBuyListDisposable.isDisposed()) {
                        mIntervalBuyListDisposable.dispose();
                    }

                    if (!TextUtils.isEmpty(editable)) {
                        String code = editable.toString();
                        if (TextUtils.isEmpty(code)) {
                            return;
                        }
                        if (code.length()==6) {
                            if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
                                mIntervalFiveDisposable.dispose();
                            }
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
                        tvZhengquanName.setText(mStockName);
                        if (!TextUtils.isEmpty(mStockCode)) {
                            getFiveSpeedData();
                        }

                        intervalFiveSpeedData();
                        intervalBuyList();
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




    private void clearView(){

        if (mIntervalFiveDisposable != null && !mIntervalFiveDisposable.isDisposed()) {
            mIntervalFiveDisposable.dispose();
        }

        if (mIntervalBuyListDisposable != null && !mIntervalBuyListDisposable.isDisposed()) {
            mIntervalBuyListDisposable.dispose();
        }

        Disposable subscribe = Flowable.just("")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    // 股票名称
                    tvZhengquanName.setText("");
                    // 清空现价
                    edtXianjiaInput.setText("");
                    // .. 涨停 跌停
                    tvZhangting.setText("");
                    tvDieting.setText("");
                    // .. 数量
                    edtShuliangInput.setText("");
                    // .. 可买
                    tvKemai.setText("");
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
