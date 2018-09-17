package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.data.data.response.StockMarketDetailRes;

import java.util.List;



public class MarketDetailAdapter extends BaseQuickAdapter<StockMarketDetailRes,BaseViewHolder> {


    private Double mYesterdayPrice;

    public MarketDetailAdapter(@Nullable List<StockMarketDetailRes> data,Double yesterdayPrice) {
        super(R.layout.item_market_detail,data);
        this.mYesterdayPrice = yesterdayPrice;
    }

    @Override
    protected void convert(BaseViewHolder helper, StockMarketDetailRes item) {
        try{
            if (Double.valueOf(item.getDetailPrice())>mYesterdayPrice) {
                // 红色
                helper.setTextColor(R.id.tv_item_detail_price, ContextCompat.getColor(mContext,R.color.stock_index_up));
            }else {
                // 绿色
                helper.setTextColor(R.id.tv_item_detail_price,ContextCompat.getColor(mContext,R.color.stock_index_down));
            }
            if (item.isDetailBuy()) {
                helper.setTextColor(R.id.tv_item_detail_type, ContextCompat.getColor(mContext,R.color.stock_index_up));
                helper.setText(R.id.tv_item_detail_type,"B");
            }else{
                helper.setTextColor(R.id.tv_item_detail_type, ContextCompat.getColor(mContext,R.color.stock_index_down));
                helper.setText(R.id.tv_item_detail_type,"S");
            }

            helper.setText(R.id.tv_item_detail_time,item.getDetailTime());
            helper.setText(R.id.tv_item_detail_price,item.getDetailPrice());
            helper.setText(R.id.tv_item_detail_count,item.getDetailCount());
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
