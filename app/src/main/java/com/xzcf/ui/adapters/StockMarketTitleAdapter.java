package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.data.data.response.StockMarketTitleResponse;

import java.util.List;


public class StockMarketTitleAdapter extends BaseQuickAdapter<StockMarketTitleResponse,BaseViewHolder> {


    public StockMarketTitleAdapter(@Nullable List<StockMarketTitleResponse> data) {
        super(R.layout.item_stock_market_title,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockMarketTitleResponse item) {
        helper.setText(R.id.tv_item_stock_market_title,item.getTitle());
        helper.setText(R.id.tv_item_stock_market_content,item.getContent());
    }
}
