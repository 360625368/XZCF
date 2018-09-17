package com.xzcf.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.xzcf.App;
import com.xzcf.R;
import com.xzcf.data.data.DataManager;
import com.xzcf.data.data.response.EntrustRecordResponse;
import com.xzcf.ui.adapters.DataTableAdapter;
import com.xzcf.utils.DialogUtils;
import com.xzcf.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CancelEntrustActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.table_view)
    TableView mTableView;
    @BindView(R.id.iBtnBack)
    ImageButton iBtnBack;
    @BindView(R.id.iBtnRefresh)
    ImageButton iBtnRefresh;
    private DataTableAdapter mTableAdapter;
    private List<String> mColumnHeaders;
    private List<Integer> mGravity;
    private Disposable mLoadDataDisposable;
    private List<EntrustRecordResponse.LogInfosBean> mLogInfo;
    private Dialog mPromptDialog;
    private Disposable mCancelEntrustDisposable;

    public static Intent startAction(Context context) {
        return new Intent(context, CancelEntrustActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_entrust);
        ButterKnife.bind(this);
        try{
            iBtnBack.setVisibility(View.VISIBLE);
            iBtnRefresh.setVisibility(View.VISIBLE);

            mColumnHeaders = new ArrayList<>();
            mColumnHeaders.add("证券名称");
            mColumnHeaders.add("买卖标志");
            mColumnHeaders.add("委托价格");
            mColumnHeaders.add("委托数量");
            mColumnHeaders.add("成交价格");
            mColumnHeaders.add("成交数量");
            mColumnHeaders.add("状态说明");
            mColumnHeaders.add("委托时间");
            mColumnHeaders.add("委托编号");
            mColumnHeaders.add("证券代码");
            mColumnHeaders.add("股东代码");
            mColumnHeaders.add("报价方式");
            mColumnHeaders.add("委托方式");

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
            mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);
            mGravity.add(Gravity.END | Gravity.CENTER_VERTICAL);


            initView();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadDataDisposable != null) {
            mLoadDataDisposable.dispose();
            mLoadDataDisposable = null;
        }
        if (mCancelEntrustDisposable != null) {
            mCancelEntrustDisposable.dispose();
            mCancelEntrustDisposable = null;
        }
        if (mPromptDialog != null) {
            mPromptDialog.dismiss();
            mPromptDialog = null;
        }
    }

    private void initView() {
        try{
            mTableAdapter = new DataTableAdapter(getContext());
            mTableView.setIgnoreSelectionColors(true);
            mTableView.setAdapter(mTableAdapter);
            mTableAdapter.setGravity(mGravity);
            mTableAdapter.setColumnHeaderItems(mColumnHeaders);

            tvTitle.setText("撤单");
            mTableView.setTableViewListener(new TableViewListener().setOnCellClickListener((View v, int row, int column) -> {
                EntrustRecordResponse.LogInfosBean logInfosBean = mLogInfo.get(row);

                showPromptDialog(logInfosBean);

            }));
            refresh();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void showPromptDialog(EntrustRecordResponse.LogInfosBean logInfosBean) {
        try{
            if (mPromptDialog != null) {
                mPromptDialog.dismiss();
                mPromptDialog = null;
            }
            mPromptDialog = DialogUtils.buildDialog(
                    getContext(),
                    "撤单提示",
                    String.format(
                            Locale.getDefault(),
                            "委托编号：%s\n证券名称：%s\n证券代码：%s\n报价方式：%s\n委托价格：%s\n委托数量：%s",
                            logInfosBean.getCodeX(),
                            logInfosBean.getStockName(),
                            logInfosBean.getStockCode(),
                            "限价",
                            logInfosBean.getAllPrice(),
                            logInfosBean.getAllCount()),
                    (dialog, which) -> cancelEntrust(logInfosBean),
                    (dialog, which) -> dialog.dismiss()
            );
            mPromptDialog.show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void cancelEntrust(EntrustRecordResponse.LogInfosBean logInfosBean) {
        try{
            String memberId = App.context().getLoginInfo().getMemberId();
            if (mCancelEntrustDisposable != null) {
                mCancelEntrustDisposable.dispose();
                mCancelEntrustDisposable = null;
            }
            mCancelEntrustDisposable = DataManager.getInstance()
                    .cancelEntrust(memberId, logInfosBean.getId())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                                if (res.getCode().contains("0")) {
                                    ToastUtil.showToast("撤单成功");
                                    if (mLogInfo != null&&mLogInfo.contains(logInfosBean)) {
                                        mLogInfo.remove(logInfosBean);
                                    }
                                    refresh();
                                } else {
                                    ToastUtil.showToast("撤单失败");
                                }
                            },
                            throwable -> {},
                            mPromptDialog::dismiss);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void refresh() {
        loadCells();
    }

    private List<List<String>> entrustRecord2Cells(List<EntrustRecordResponse.LogInfosBean> logInfos) {
        List<List<String>> ret = new ArrayList<>();
        try{
            if (logInfos == null || logInfos.size() == 0) {
                return ret;
            }
            mLogInfo = logInfos;
            List<String> rows = new ArrayList<>();
            List<EntrustRecordResponse.LogInfosBean> newLogInfo = new ArrayList<>();
            int i = 0;
            List<String> cells;
            for (EntrustRecordResponse.LogInfosBean logInfo : logInfos) {
                if (!logInfo.getState().contains("委托中")){
                    newLogInfo.add(logInfo);
                    continue;
                }
                cells = new ArrayList<>();
                cells.add(logInfo.getStockName());
                cells.add(logInfo.getType());
                cells.add(logInfo.getAllPrice());
                cells.add(logInfo.getAllCount());
                cells.add(logInfo.getAvgAmount());
                cells.add(logInfo.getSaleCount());
                cells.add(logInfo.getState());
                cells.add(logInfo.getTimeEntrust().split(" ")[1]);
                cells.add(logInfo.getCodeX());
                cells.add(logInfo.getStockCode());
                cells.add(logInfo.getStockholderCode());
                cells.add("限价");
                cells.add("网上委托");
                ret.add(cells);
                rows.add("" + i++);
            }
            mLogInfo.removeAll(newLogInfo);
            if (ret.size() != 0) {
                ret.add(0, rows);
            }
            return ret;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return ret;
    }

    public void loadCells() {
        try{
            String memberId = App.context().getLoginInfo().getMemberId();
            if (mLoadDataDisposable != null) {
                mLoadDataDisposable.dispose();
            }
            mLoadDataDisposable = DataManager.getInstance()
                    .getEntrustRecord(memberId, "1", null, null,"1")
                    .map(entrustRecordResponse -> entrustRecord2Cells(entrustRecordResponse.getLogInfos()))
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
        refresh();
    }


}
