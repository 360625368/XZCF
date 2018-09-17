package com.xzcf.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.data.data.response.HavingLogResponse;
import com.xzcf.ui.StockMarketActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FundStockAdapter extends RecyclerView.Adapter<FundStockAdapter.ViewHolder> {
    private List<HavingLogResponse.LogInfosBean> mHavingLogs;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fund_stock, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HavingLogResponse.LogInfosBean bean = mHavingLogs.get(holder.getAdapterPosition());
        holder.tvStockName.setText(bean.getStockName());
        holder.tvCountAll.setText(bean.getCountAll());
        holder.tvNowPrice.setText(bean.getNowPrice());
        holder.tvProfitLossAmount.setText(bean.getProfitLossAmount());
        holder.tvNowAmount.setText(bean.getNowAmount());
        holder.tvCountCan.setText(bean.getCountCan());
        holder.tvCostPriceOne.setText(bean.getCostPriceOne());
        holder.tvProfitLossScale.setText(bean.getProfitLossScale());

        if (bean.getProfitLossAmount().contains("-" )) {
            holder.tvProfitLossAmount.setTextColor(mContext.getResources().getColor(R.color.stock_index_down));
        } else {
            holder.tvProfitLossAmount.setTextColor(mContext.getResources().getColor(R.color.stock_index_up));
        }

        if (bean.getProfitLossScale().contains("-" )) {
            holder.tvProfitLossScale.setTextColor(mContext.getResources().getColor(R.color.stock_index_down));
        } else {
            holder.tvProfitLossScale.setTextColor(mContext.getResources().getColor(R.color.stock_index_up));
        }

        holder.llFundStock.setOnClickListener(l -> mContext.startActivity(StockMarketActivity.newIntent(mContext,bean.getStockCode(),bean.getStockName())));
    }

    @Override
    public int getItemCount() {
        return mHavingLogs == null ? 0 : mHavingLogs.size();
    }

    public void setHavingLogs(List<HavingLogResponse.LogInfosBean> havingLogs) {
        mHavingLogs = havingLogs;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_fund_stock)
        LinearLayout llFundStock;
        @BindView(R.id.tvStockName)
        TextView tvStockName;
        @BindView(R.id.tvCountAll)
        TextView tvCountAll;
        @BindView(R.id.tvNowPrice)
        TextView tvNowPrice;
        @BindView(R.id.tvProfitLossAmount)
        TextView tvProfitLossAmount;
        @BindView(R.id.tvNowAmount)
        TextView tvNowAmount;
        @BindView(R.id.tvCountCan)
        TextView tvCountCan;
        @BindView(R.id.tvCostPriceOne)
        TextView tvCostPriceOne;
        @BindView(R.id.tvProfitLossScale)
        TextView tvProfitLossScale;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
