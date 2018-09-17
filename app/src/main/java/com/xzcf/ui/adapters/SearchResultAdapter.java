package com.xzcf.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.data.data.response.StockListResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    List<StockListResponse.StockBean> stockBeanList;
    OnItemClickListener mOnItemClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        StockListResponse.StockBean stockBean = stockBeanList.get(holder.getAdapterPosition());
        holder.tvName.setText(stockBean.getStockName());
        holder.tvStockCode.setText(stockBean.getStockCode());
        holder.itemView.setOnClickListener(view -> {
            mOnItemClickListener.onClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return stockBeanList == null ? 0 : stockBeanList.size();
    }

    public void clearStock() {
        if (stockBeanList == null) return;
        stockBeanList.clear();
        notifyDataSetChanged();
    }

    public void addStock(StockListResponse.StockBean stockBean) {
        if (stockBeanList == null) {
            stockBeanList = new ArrayList<>();
        }
        stockBeanList.add(stockBean);
        notifyItemInserted(stockBeanList.size() - 1);
    }

    public StockListResponse.StockBean getItem(int position) {
        if (position >= getItemCount()) return null;
        return stockBeanList.get(position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvStockCode)
        TextView tvStockCode;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


}
