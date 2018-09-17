package com.xzcf.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.ui.StockMarketActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotStocksAdapter extends RecyclerView.Adapter<HotStocksAdapter.ViewHolder> {

    private Context mContext;
    private List<String[]> stockRankList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hot_stock, parent, false);
        return new ViewHolder(view,mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] strArrs = stockRankList.get(holder.getAdapterPosition());
        holder.tvName.setText(strArrs[2]);
        holder.tvCodeNumber.setText(strArrs[1]);
        holder.tvPrice.setText(strArrs[3]);
        holder.tvRate.setText(strArrs[5]);
        try {
            Double rate = Double.valueOf(strArrs[5].replace("%", ""));
            if (rate >= 0) {
                holder.tvRate.setTextColor(mContext.getResources().getColor(R.color.stock_index_up));
            } else {
                holder.tvRate.setTextColor(mContext.getResources().getColor(R.color.stock_index_down));
            }
        }catch (Exception ex){
        }
        holder.setHotItmeClick(strArrs[2],strArrs[1]);
    }

    public void setStockRankList(List<String[]> stockRankList) {
        this.stockRankList = stockRankList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        System.out.println("刷新:");
        return stockRankList == null ? 0 : stockRankList.size();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvCodeNumber)
        TextView tvCodeNumber;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvRate)
        TextView tvRate;
        @BindView(R.id.layout_item)
        ConstraintLayout itemLayout;
        Context mContext;

        public ViewHolder(View itemView,Context context) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.mContext = context;
        }

        public void setHotItmeClick(String stockName,String stockCode){
            itemLayout.setOnClickListener(l->{
                mContext.startActivity(StockMarketActivity.newIntent(mContext,stockCode,stockName));
            });
        }

    }
}
