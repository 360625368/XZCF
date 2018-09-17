package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.ui.StockMarketActivity;

import java.util.List;


public class HomeHotStockAdapter extends BaseQuickAdapter<String[],BaseViewHolder> {


    public HomeHotStockAdapter(@Nullable List<String[]> data) {
        super(R.layout.item_hot_stock, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String[] item) {
        helper.setText(R.id.tvName,item[2]);
        helper.setText(R.id.tvCodeNumber,item[1]);
        helper.setText(R.id.tvPrice,item[3]);
        helper.setText(R.id.tvRate,item[5]);

        try {
            Double rate = Double.valueOf(item[5].replace("%", ""));
            if (rate >= 0) {
                helper.setTextColor(R.id.tvRate,mContext.getResources().getColor(R.color.stock_index_up));
            } else {
                helper.setTextColor(R.id.tvRate,mContext.getResources().getColor(R.color.stock_index_down));
            }
        }catch (Exception ex){
        }

        ConstraintLayout constraintLayout = helper.getView(R.id.layout_item);
        constraintLayout.setOnClickListener(view -> mContext.startActivity(StockMarketActivity.newIntent(mContext,item[1],item[2])));

    }
}
