package com.xzcf.ui.adapters;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xzcf.R;
import com.xzcf.data.data.response.StockRankItemTitle;
import com.xzcf.ui.StockMarketActivity;

import java.util.List;
import java.util.Locale;


public class StockRankAdapter extends BaseQuickAdapter<StockRankItemTitle, BaseViewHolder> {


    boolean isUPGone = true;
    boolean isDownGone = true;

    public StockRankAdapter(@Nullable List<StockRankItemTitle> data) {
        super(R.layout.item_stock_zd_title, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockRankItemTitle item) {

        try {

            RecyclerView recycleStockUpList = helper.getView(R.id.ll_up);
            RecyclerView recycleStockDownList = helper.getView(R.id.ll_down);


            helper.setOnClickListener(R.id.ll_stock_rank_up, view -> {
                if (isUPGone) {
                    helper.setBackgroundRes(R.id.iv_stock_rank_up_arrow, R.drawable.ic_arrow);
                    recycleStockUpList.setVisibility(View.GONE);
                    isUPGone = false;
                } else {
                    helper.setBackgroundRes(R.id.iv_stock_rank_up_arrow, R.drawable.ic_arrow_expand);
                    recycleStockUpList.setVisibility(View.VISIBLE);
                    isUPGone = true;
                }
            });

            helper.setOnClickListener(R.id.ll_stock_rank_down, view -> {
                if (isDownGone) {
                    helper.setBackgroundRes(R.id.iv_stock_rank_down_arrow, R.drawable.ic_arrow);
                    recycleStockDownList.setVisibility(View.GONE);
                    isDownGone = false;
                } else {
                    helper.setBackgroundRes(R.id.iv_stock_rank_down_arrow, R.drawable.ic_arrow_expand);
                    recycleStockDownList.setVisibility(View.VISIBLE);
                    isDownGone = true;
                }
            });

            List<String> upList = item.getUpList();
            recycleStockUpList.setLayoutManager(new LinearLayoutManager(mContext));
            StockRankListAdapter mStockRankUpAdapter = new StockRankListAdapter(upList);
            recycleStockUpList.setAdapter(mStockRankUpAdapter);

            List<String> downList = item.getDownList();
            recycleStockDownList.setLayoutManager(new LinearLayoutManager(mContext));
            StockRankListAdapter mStockRankDownAdapter = new StockRankListAdapter(downList);
            recycleStockDownList.setAdapter(mStockRankDownAdapter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    class StockRankListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        StockRankItemTitle stockRankItemTitle = new StockRankItemTitle();
        boolean isUP;
        String rateUpFormat = "+%s";

        public StockRankListAdapter(@Nullable List<String> data) {
            super(R.layout.item_stock_zd, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            try{
                String stockName = stockRankItemTitle.getStockName(item);
                String stockCode = stockRankItemTitle.getStockCode(item);
                String stockPrice = stockRankItemTitle.getStockPrice(item);
                String stockRate = stockRankItemTitle.getStockRate(item);
                // 涨/跌
                if (stockRate.contains("-")) {
                    isUP = false;
                    helper.setTextColor(R.id.tv_item_stock_zd_price, ContextCompat.getColor(mContext, R.color.stock_index_down));
                    helper.setTextColor(R.id.tv_item_stock_zd_rate, ContextCompat.getColor(mContext, R.color.stock_index_down));
                } else {
                    isUP = true;
                    helper.setTextColor(R.id.tv_item_stock_zd_price, ContextCompat.getColor(mContext, R.color.stock_index_up));
                    helper.setTextColor(R.id.tv_item_stock_zd_rate, ContextCompat.getColor(mContext, R.color.stock_index_up));
                }
                // 名称
                helper.setText(R.id.tv_item_stock_zd_name, stockName);
                // 代号
                helper.setText(R.id.tv_item_stock_zd_code, stockCode);
                // 现价
                helper.setText(R.id.tv_item_stock_zd_price, stockPrice);
                // 涨跌幅
                if (isUP) {
                    String rateFormat = String.format(Locale.getDefault(), rateUpFormat, stockRate);
                    helper.setText(R.id.tv_item_stock_zd_rate, rateFormat);
                } else {
                    helper.setText(R.id.tv_item_stock_zd_rate, stockRate);
                }

                helper.setOnClickListener(R.id.ll_item_stock_zd, view -> {
                    // 打开详情
                    mContext.startActivity(StockMarketActivity.newIntent(mContext, stockCode, stockName));
                });
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
    }


    // 点击监听
    interface OnStockItemListen {
        void onStockItemClick(String stockCode);
    }


}
