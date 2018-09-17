package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.data.data.response.HavingLogResponse;

import java.util.List;


public class HavingAdapter extends BaseQuickAdapter<HavingLogResponse.LogInfosBean,BaseViewHolder> {


    public HavingAdapter(@Nullable List<HavingLogResponse.LogInfosBean> data) {
        super(R.layout.item_having,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HavingLogResponse.LogInfosBean item) {
        helper.addOnClickListener(R.id.ll_having_item);
        helper.setText(R.id.tvStockName,item.getStockName());
        helper.setText(R.id.tvCountAll,item.getCountAll());
        helper.setText(R.id.tvNowPrice,item.getNowPrice());
        helper.setText(R.id.tvProfitLossAmount,item.getProfitLossAmount());
        helper.setText(R.id.tvNowAmount,item.getNowAmount());
        helper.setText(R.id.tvCountCan,item.getCountCan());
        helper.setText(R.id.tvCostPriceOne,item.getCostPriceOne());
        helper.setText(R.id.tvProfitLossScale,item.getProfitLossScale());

        if (item.getProfitLossAmount().contains("-" )) {
            helper.setTextColor(R.id.tvProfitLossAmount,mContext.getResources().getColor(R.color.stock_index_down));
        } else {
            helper.setTextColor(R.id.tvProfitLossAmount,mContext.getResources().getColor(R.color.stock_index_up));
        }

        if (item.getProfitLossScale().contains("-" )) {
            helper.setTextColor(R.id.tvProfitLossScale,mContext.getResources().getColor(R.color.stock_index_down));
        } else {
            helper.setTextColor(R.id.tvProfitLossScale,mContext.getResources().getColor(R.color.stock_index_up));
        }
    }
}
