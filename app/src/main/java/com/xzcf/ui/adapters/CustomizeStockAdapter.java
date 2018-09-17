package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.data.data.response.CustomizeStockResponse;
import com.xzcf.ui.StockMarketActivity;
import com.xzcf.utils.MathUtils;

import java.util.List;


public class CustomizeStockAdapter extends BaseQuickAdapter<CustomizeStockResponse, BaseViewHolder> {


    public CustomizeStockAdapter(@Nullable List<CustomizeStockResponse> data) {
        super(R.layout.item_customize_stock, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CustomizeStockResponse item) {
        try{
            helper.setText(R.id.tv_item_stock_name,item.getStockName());
            helper.setText(R.id.tv_item_stock_code,item.getStockCode());
            RelativeLayout turnoverBg = helper.getView(R.id.rl_item_stock_turnover);
            Double todayPrice =  Math.abs(Double.valueOf(item.getTodayPrice()));
            Double yesterdayPrice =  Math.abs(Double.valueOf(item.getYesterdayPrice()));
            double zhangDie = MathUtils.getZhangDie(todayPrice, yesterdayPrice);
            double zhangFu = MathUtils.getZhangFu(todayPrice, yesterdayPrice);
            String zhangDieStr = MathUtils.doubleToString(zhangDie);
            String zhangFuStr = MathUtils.doubleToString(zhangFu);
            if(zhangDie<0){
                // 绿色
                helper.setTextColor(R.id.tv_item_stock_price, ContextCompat.getColor(mContext,R.color.stock_index_down));
                helper.setTextColor(R.id.tv_item_stock_point, ContextCompat.getColor(mContext,R.color.stock_index_down));
                turnoverBg.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_customize_turnover_bg_down));
            }else{
                // 红色
                helper.setTextColor(R.id.tv_item_stock_price, ContextCompat.getColor(mContext,R.color.stock_index_up));
                helper.setTextColor(R.id.tv_item_stock_point, ContextCompat.getColor(mContext,R.color.stock_index_up));
                turnoverBg.setBackground(ContextCompat.getDrawable(mContext,R.drawable.ic_customize_turnover_bg_up));
            }
            helper.setText(R.id.tv_item_stock_price,MathUtils.doubleToString(todayPrice));
            helper.setText(R.id.tv_item_stock_point,zhangDieStr );
            helper.setText(R.id.tv_item_stock_turnover,zhangFuStr+"%");

            helper.setOnClickListener(R.id.ll_item_stock,l->{
                mContext.startActivity(StockMarketActivity.newIntent(mContext,item.getStockCode(),item.getStockName()));
            });
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


}
