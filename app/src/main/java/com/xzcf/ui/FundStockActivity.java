package com.xzcf.ui;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.HavingLogResponse;
import com.xzcf.data.data.response.MemMoneyResponse;
import com.xzcf.ui.adapters.FundStockAdapter;
import com.xzcf.utils.DialogUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FundStockActivity extends BaseActivity {


    @BindView(R.id.rvFundStock)
    RecyclerView rvFundStock;
    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvAssetsAll)
    TextView tvAssetsAll;
    @BindView(R.id.tvAssetsReference)
    TextView tvAssetsReference;
    @BindView(R.id.tvProfitLossAmount)
    TextView tvProfitLossAmount;
    @BindView(R.id.tvBalanceUse)
    TextView tvBalanceUse;
    @BindView(R.id.tvBalanceAll)
    TextView tvBalanceAll;
    @BindView(R.id.tvBalanceTake)
    TextView tvBalanceTake;
    @BindView(R.id.iBtnRefresh)
    ImageButton btnRefresh;
    private FundStockAdapter mFundStockAdapter;
    private Dialog mProgressDialog;
    private MemMoneyResponse.LogInfosBean mMoneyInfo;
    private List<HavingLogResponse.LogInfosBean> mHavingnLogs;
    private Disposable mDisposable;
    private Disposable mIntervalDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_stock);
        ButterKnife.bind(this);

        iBtnBack.setVisibility(View.VISIBLE);
        btnRefresh.setVisibility(View.VISIBLE);
        tvTitle.setText("资产股份" );


        mFundStockAdapter = new FundStockAdapter();
        rvFundStock.setAdapter(mFundStockAdapter);
        rvFundStock.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.divider_shape);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(drawable);
        rvFundStock.addItemDecoration(dividerItemDecoration);
        loadData();
        intervalRefreshHavingLog();
    }

    private void updateView() {
        if (mMoneyInfo == null) {
            return;
        }
        tvAssetsAll.setText(mMoneyInfo.getAssetsAll());
        tvAssetsReference.setText(mMoneyInfo.getAssetsReference());
        tvProfitLossAmount.setText(mMoneyInfo.getProfitLossAmount());
        tvBalanceUse.setText(mMoneyInfo.getBalanceUse());
        tvBalanceTake.setText(mMoneyInfo.getBalanceTake());
        tvBalanceAll.setText(mMoneyInfo.getBalanceAll());
        if (mMoneyInfo.getProfitLossAmount().contains("-" )) {
            tvProfitLossAmount.setTextColor(getResources().getColor(R.color.stock_index_down));
        } else {
            tvProfitLossAmount.setTextColor(getResources().getColor(R.color.stock_index_up));
        }

        mFundStockAdapter.setHavingLogs(mHavingnLogs);
    }

    @OnClick(R.id.iBtnBack)
    public void onIBtnBackClicked() {
        finish();
    }

    @OnClick(R.id.iBtnRefresh)
    public void onIBtnRefreshClicked() {
        loadData();
    }


    private void loadData() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = DialogUtils.buildProgressDialogsDialog(getContext(), "加载中", "请稍候 " );
        mProgressDialog.show();

        String memberId = App.context().getLoginInfo().getMemberId();

        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }

        mDisposable = Observable
                .merge(
                        DataManager.getInstance().getMoney(memberId),
                        DataManager.getInstance().getHavingLog(memberId, "1" )
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    if (mProgressDialog != null) mProgressDialog.dismiss();
                    if (res instanceof MemMoneyResponse) {
                        mMoneyInfo = ((MemMoneyResponse) res).getLogInfo();
                    } else if (res instanceof HavingLogResponse) {
                        mHavingnLogs = ((HavingLogResponse) res).getLogInfos();
                    }
                }, throwable -> {
                    if (mProgressDialog != null) mProgressDialog.dismiss();
                }, this::updateView);

    }


    /**
     * 间隔2秒刷新一次 资产列表
     */
    private void intervalRefreshHavingLog(){
        String memberId = App.context().getLoginInfo().getMemberId();
        mIntervalDisposable = Observable.interval(2, TimeUnit.SECONDS)
                .subscribe(aLong -> DataManager.getInstance().getHavingLog(memberId, "1")
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(havingLogResponse -> mHavingnLogs = havingLogResponse.getLogInfos(),
                                throwable -> {},
                                () -> mFundStockAdapter.setHavingLogs(mHavingnLogs)));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }

        if (mIntervalDisposable != null) {
            mIntervalDisposable.dispose();
        }
    }
}
