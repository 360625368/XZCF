package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.data.data.response.StockMarketFiveSpeedBean;
import com.xzcf.utils.MathUtils;

import java.util.List;


public class SellFiveAdapter extends BaseQuickAdapter<StockMarketFiveSpeedBean,BaseViewHolder> {
    private Double mTodayPrice;

    public Double getTodayPrice() {
        return mTodayPrice;
    }

    public SellFiveAdapter setTodayPrice(Double todayPrice) {
        mTodayPrice = todayPrice;
        return this;
    }

    public SellFiveAdapter(@Nullable List<StockMarketFiveSpeedBean> data, Double todayPrice) {
        super(R.layout.item_sell_five,data);
        this.mTodayPrice = todayPrice;
    }

    @Override
    protected void convert(BaseViewHolder helper, StockMarketFiveSpeedBean item) {
        try{
            helper.addOnClickListener(R.id.ll_five_speed);
            if (Double.valueOf(item.getPrice())>mTodayPrice) {
                // 红色
                helper.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext,R.color.stock_index_up));
            }else if(Double.valueOf(item.getPrice()).equals(mTodayPrice)){
                // 灰色
                helper.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext,R.color.stock_tab_txt));
            }else{
                // 绿色
                helper.setTextColor(R.id.tv_price, ContextCompat.getColor(mContext,R.color.stock_index_down));
            }
            // 卖
                String price = MathUtils.doubleToString(Double.valueOf(item.getPrice()));
                helper.setText(R.id.tv_title,item.getTitle());
                if (TextUtils.equals(price,"0.00")|| TextUtils.equals(price,"0")) {
                    helper.setText(R.id.tv_price, "");
                }else{
                    helper.setText(R.id.tv_price,price);
                }
                if (TextUtils.equals(item.getCount(),"0")) {
                    helper.setText(R.id.tv_num,"");
                }else{
                    helper.setText(R.id.tv_num,item.getCount());
                }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
