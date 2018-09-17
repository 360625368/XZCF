package com.xzcf.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.BargainRecordResponse;
import com.xzcf.ui.adapters.DataTableAdapter;
import com.xzcf.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BargainRecordActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.table_view)
    TableView mTableView;
    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.iBtnRefresh)
    ImageButton iBtnRefresh;
    private DataTableAdapter mTableAdapter;
    private List<String> mColumnHeaders;
    private List<Integer> mGravity;
    private List<String> mTitles;
    private Disposable mDisposable;

    public static Intent startAction(Context context) {
        return new Intent(context, BargainRecordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrust_record);
        ButterKnife.bind(this);
        iBtnBack.setVisibility(View.VISIBLE);
        iBtnRefresh.setVisibility(View.VISIBLE);

        mColumnHeaders = new ArrayList<>();
        mColumnHeaders.add("证券名称" );
        mColumnHeaders.add("成交日期" );
        mColumnHeaders.add("成交时间" );
        mColumnHeaders.add("买卖标志" );
        mColumnHeaders.add("成交价格" );
        mColumnHeaders.add("成交数量" );
        mColumnHeaders.add("成交金额" );
        mColumnHeaders.add("成交编号" );
        mColumnHeaders.add("委托编号" );
        mColumnHeaders.add("证券代码" );
        mColumnHeaders.add("股东代码" );

        mGravity = new ArrayList<>();
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
        mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);


        mTitles = new ArrayList<>();
        mTitles.add("今天" );
        mTitles.add("一周内" );
        mTitles.add("一月内" );
        mTitles.add("三月内" );
        mTitles.add("全部" );

        initView();
    }

    private void initView() {
        mTableAdapter = new DataTableAdapter(getContext());
        mTableView.setIgnoreSelectionColors(true);
        mTableView.setAdapter(mTableAdapter);
        mTableAdapter.setGravity(mGravity);
        mTableAdapter.setColumnHeaderItems(mColumnHeaders);

        tvTitle.setText("成交查询" );

        for (String title : mTitles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }

        Date createEnd = new Date(System.currentTimeMillis());
        loadCells(DateTimeUtils.addDate(createEnd, Calendar.DATE, -1), createEnd);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try{
                    refresh(tab.getPosition());
                }catch(Exception ex){
                    ex.printStackTrace();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void refresh(int position) {
        Date createEnd = new Date(System.currentTimeMillis());

        switch (position) {
            case 0:
                loadCells(DateTimeUtils.addDate(createEnd, Calendar.DATE, -1), createEnd);
                break;
            case 1:
                loadCells(DateTimeUtils.addDate(createEnd, Calendar.DATE, -7), createEnd);
                break;
            case 2:
                loadCells(DateTimeUtils.addDate(createEnd, Calendar.MONTH, -1), createEnd);
                break;
            case 3:
                loadCells(DateTimeUtils.addDate(createEnd, Calendar.MONTH, -3), createEnd);
                break;
            case 4:
                loadCells(null, null);
                break;
        }
    }

    private List<List<String>> bargainRecord2Cells(List<BargainRecordResponse.LogInfosBean> logInfos) {
        List<List<String>> ret = new ArrayList<>();
        if (logInfos == null || logInfos.size() == 0) {
            return ret;
        }
        List<String> rows = new ArrayList<>();
        int i = 0;
        List<String> cells;
        for (BargainRecordResponse.LogInfosBean logInfo : logInfos) {
            cells = new ArrayList<>();
            cells.add(logInfo.getStockName());
            cells.add(logInfo.getRealSaleTime().split(" " )[0]);
            cells.add(logInfo.getRealSaleTime().split(" " )[1]);
            cells.add(logInfo.getType());
            cells.add(logInfo.getSalePrice());
            cells.add(logInfo.getSaleCount());
            cells.add(logInfo.getSaleAmount());
            cells.add(logInfo.getCodeX());
            cells.add(logInfo.getTdxEntrustCode());
            cells.add(logInfo.getStockCode());
            cells.add(logInfo.getStockholderCode());
            ret.add(cells);
            rows.add("" + i++);
        }
        ret.add(0, rows);
        return ret;
    }

    public void loadCells(Date start, Date end) {
        try{
            String memberId = App.context().getLoginInfo().getMemberId();
            if (mDisposable != null) {
                mDisposable.dispose();
            }
            mDisposable = DataManager.getInstance()
                    .getBargainRecord(memberId, "1", start, end)
                    .map(bargainRecordResponse -> bargainRecord2Cells(bargainRecordResponse.getLogInfos()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lists -> {
                        if (lists == null || lists.size() == 0) mTableView.setVisibility(View.GONE);
                        else {
                            mTableView.setVisibility(View.VISIBLE);
                            mTableAdapter.setRowHeaderItems(lists.get(0));
                            mTableAdapter.setCellItems(lists.subList(1, lists.size()));
                        }
                    }, throwable -> {
                        mTableView.setVisibility(View.GONE);
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @OnClick(R.id.iBtnBack)
    public void onIBtnBackClicked() {
        finish();
    }

    @OnClick(R.id.iBtnRefresh)
    public void onIBtnRefreshClicked() {
        try{
            refresh(tabLayout.getSelectedTabPosition());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
