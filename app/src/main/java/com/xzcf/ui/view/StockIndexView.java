package com.xzcf.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.data.data.StockDataManager;
import com.xzcf.data.data.response.StockIndexResponse;
import com.xzcf.utils.MathUtils;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class StockIndexView {

    @BindView(R.id.tv_shangz_index_point)
    TextView tvShangzIndexPoint;
    @BindView(R.id.iv_shangz_index_arrow)
    ImageView ivShangzIndexArrow;
    @BindView(R.id.tv_shangz_index_rate)
    TextView tvShangzIndexRate;
    @BindView(R.id.tv_shangz_index_turnover)
    TextView tvShangzIndexTurnover;
    @BindView(R.id.tv_shenz_index_point)
    TextView tvShenzIndexPoint;
    @BindView(R.id.iv_shenz_index_arrow)
    ImageView ivShenzIndexArrow;
    @BindView(R.id.tv_shenz_index_rate)
    TextView tvShenzIndexRate;
    @BindView(R.id.tv_shenz_index_turnover)
    TextView tvShenzIndexTurnover;
    @BindView(R.id.tv_cy_index_point)
    TextView tvCyIndexPoint;
    @BindView(R.id.iv_cy_index_arrow)
    ImageView ivCyIndexArrow;
    @BindView(R.id.tv_cy_index_rate)
    TextView tvCyIndexRate;
    @BindView(R.id.tv_cy_index_turnover)
    TextView tvCyIndexTurnover;
    @BindView(R.id.tv_zx_index_point)
    TextView tvZxIndexPoint;
    @BindView(R.id.iv_zx_index_arrow)
    ImageView ivZxIndexArrow;
    @BindView(R.id.tv_zx_index_rate)
    TextView tvZxIndexRate;
    @BindView(R.id.tv_zx_index_turnover)
    TextView tvZxIndexTurnover;
    @BindView(R.id.tv_shangz_index_name)
    TextView tvShangzIndexName;
    @BindView(R.id.tv_shenz_index_name)
    TextView tvShenzIndexName;
    @BindView(R.id.tv_cy_index_name)
    TextView tvCyIndexName;
    @BindView(R.id.tv_zx_index_name)
    TextView tvZxIndexName;

    private Unbinder unbinder;

    private Context mContext;
    private RecyclerView mParentView;

    private Disposable mIndexDisposable;
    private Disposable mIntervalDisposable;


    public StockIndexView(Context context, RecyclerView recyclerView) {
        mContext = context;
        this.mParentView = recyclerView;
    }

    public View initView() {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.view_stock_index, (ViewGroup) mParentView.getParent(), false);
        unbinder = ButterKnife.bind(this, inflate);
        return inflate;
    }

    public void initData(){
        try{
            Flowable<StockIndexResponse> stockIndexFlowable = StockDataManager.getInstance().getStockIndex();
            mIndexDisposable = stockIndexFlowable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setData, throwable -> {});
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void setData(StockIndexResponse stockIndexResponse) {
        if (stockIndexResponse != null) {
            try {
                List<String> stockIndexs = stockIndexResponse.getStockIndexs();
                // 0
                String[] shangzhengSplit = stockIndexs.get(0).split(",");
                tvShangzIndexName.setText(shangzhengSplit[0]);
                boolean isUP = checkRate(shangzhengSplit[2],tvShangzIndexRate);
                checkPointStyle(shangzhengSplit[1], tvShangzIndexPoint, ivShangzIndexArrow,isUP);
                checkTurnOver(shangzhengSplit[3],tvShangzIndexTurnover,isUP);
                // 1
                String[] shenzhengSplit = stockIndexs.get(1).split(",");
                tvShenzIndexName.setText(shenzhengSplit[0]);
                isUP = checkRate(shenzhengSplit[2],tvShenzIndexRate);
                checkPointStyle(shenzhengSplit[1], tvShenzIndexPoint, ivShenzIndexArrow,isUP);
                checkTurnOver(shenzhengSplit[3],tvShenzIndexTurnover,isUP);
                // 2
                String[] zhongxiaoSplit = stockIndexs.get(2).split(",");
                tvZxIndexName.setText(zhongxiaoSplit[0]);
                isUP = checkRate(zhongxiaoSplit[2],tvZxIndexRate);
                checkPointStyle(zhongxiaoSplit[1], tvZxIndexPoint, ivZxIndexArrow,isUP);
                checkTurnOver(zhongxiaoSplit[3],tvZxIndexTurnover,isUP);
                // 3
                String[] chuangyeSplit = stockIndexs.get(3).split(",");
                tvCyIndexName.setText(chuangyeSplit[0]);
                isUP = checkRate(chuangyeSplit[2],tvCyIndexRate);
                checkPointStyle(chuangyeSplit[1], tvCyIndexPoint, ivCyIndexArrow,isUP);
                checkTurnOver(chuangyeSplit[3],tvCyIndexTurnover,isUP);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private boolean checkPointStyle(String strPoint,TextView tvPoint,ImageView imageView,boolean isUp){
        try{
            Double point = Double.valueOf(strPoint);
            if (isUp) {
                // 红
                tvPoint.setText(MathUtils.doubleToString(Math.abs(point)));
                tvPoint.setTextColor(ContextCompat.getColor(mContext,R.color.stock_index_up));
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_stock_index_up));
                return true;
            }else{
                // 绿
                tvPoint.setText(MathUtils.doubleToString(point));
                tvPoint.setTextColor(ContextCompat.getColor(mContext,R.color.stock_index_down));
                imageView.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.ic_stock_index_down));
                return false;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

    }

    private boolean checkRate(String strRate,TextView tvRate){
        String rateUpFormat = "+%s";
        String rateDownFormat = "%s";
        boolean isUpFlag = false;
        try{
            Double point = Double.valueOf(strRate);
            if (point>0) {
                isUpFlag  = true;
            }else{
                isUpFlag = false;
            }
            strRate = MathUtils.doubleToString(Double.valueOf(strRate));
            if (isUpFlag) {
                String rateFormat = String.format(Locale.getDefault(), rateUpFormat, strRate);
                tvRate.setText(rateFormat);
            }else{
                String rateFormat = String.format(Locale.getDefault(), rateDownFormat, strRate);
                tvRate.setText(rateFormat);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return isUpFlag;
    }

    private void checkTurnOver(String strTurnover,TextView tvTurnover,boolean isUp){
        try{
            String turnoverUpFormat = "+%s%%";
            String turnoverDownFormat = "%s%%";
            strTurnover = MathUtils.doubleToString(Double.valueOf(strTurnover));
            if (isUp) {
                String turnoverFormat = String.format(Locale.getDefault(), turnoverUpFormat, strTurnover);
                tvTurnover.setText(turnoverFormat);
            }else{
                String turnoverFormat = String.format(Locale.getDefault(), turnoverDownFormat, strTurnover);
                tvTurnover.setText(turnoverFormat);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void intervalUpdateData(){
        try{
            mIntervalDisposable = Observable.interval(1, TimeUnit.SECONDS)
                    .subscribe(aLong -> {
                        initData();
                    });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void onDisposable(){
        if (mIntervalDisposable != null && !mIntervalDisposable.isDisposed()) {
            mIntervalDisposable.dispose();
        }
    }


    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }

        if (mIndexDisposable != null && !mIndexDisposable.isDisposed()) {
            mIndexDisposable.dispose();
        }

        if (mIntervalDisposable != null && !mIntervalDisposable.isDisposed()) {
            mIntervalDisposable.dispose();
        }
    }


}
