package com.xzcf.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzcf.R;

import java.util.ArrayList;
import java.util.List;

public class HomeIndexViewPagerAdapter extends PagerAdapter {
    private String indexStr = "上证指数,0,0,0,0,0;深证成指,0,0,0,0,0;中小板指,0,0,0,0,0;创业板指,0,0,0,0,0;";
    private List<StockIndex> stockIndices;
    private Context context;

    public HomeIndexViewPagerAdapter() {
        stockIndices = parseIndices(indexStr);
    }

    @Override
    public int getCount() {
        return stockIndices == null ? 0 : stockIndices.size() / 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (context == null) {
            context = container.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_index, container, false);

        StockIndex stockIndex_L = stockIndices.get(position * 2);
        TextView tvIndexName_L = view.findViewById(R.id.tvIndexName_L);
        TextView tvCurrentPoints_L = view.findViewById(R.id.tvCurrentPoints_L);
        TextView tvPrice_L = view.findViewById(R.id.tvPrice_L);
        TextView tvRate_L = view.findViewById(R.id.tvRate_L);
        tvIndexName_L.setText(stockIndex_L.indexName);
        tvCurrentPoints_L.setText(context.getString(R.string.home_index_current_points_format, stockIndex_L.currentPoints));
        tvPrice_L.setText(context.getString(R.string.home_index_current_price_format, stockIndex_L.currentPrice));
        tvRate_L.setText(context.getString(R.string.home_index_rising_falling_rate_format, stockIndex_L.risingAndFallingRate));
        if (stockIndex_L.currentPrice < 0) {
            tvCurrentPoints_L.setTextColor(context.getResources().getColor(R.color.stock_index_down));
        } else {
            tvCurrentPoints_L.setTextColor(context.getResources().getColor(R.color.stock_index_up));
        }

        StockIndex stockIndex_R = stockIndices.get(position * 2 + 1);
        TextView tvIndexName_R = view.findViewById(R.id.tvIndexName_R);
        TextView tvCurrentPoints_R = view.findViewById(R.id.tvCurrentPoints_R);
        TextView tvPrice_R = view.findViewById(R.id.tvPrice_R);
        TextView tvRate_R = view.findViewById(R.id.tvRate_R);
        tvIndexName_R.setText(stockIndex_R.indexName);
        tvCurrentPoints_R.setText(context.getString(R.string.home_index_current_points_format, stockIndex_R.currentPoints));
        tvPrice_R.setText(context.getString(R.string.home_index_current_price_format, stockIndex_R.currentPrice));
        tvRate_R.setText(context.getString(R.string.home_index_rising_falling_rate_format, stockIndex_R.risingAndFallingRate));
        if (stockIndex_R.currentPrice < 0) {
            tvCurrentPoints_R.setTextColor(context.getResources().getColor(R.color.stock_index_down));
        } else {
            tvCurrentPoints_R.setTextColor(context.getResources().getColor(R.color.stock_index_up));
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
        this.stockIndices = parseIndices(this.indexStr);
        notifyDataSetChanged();
    }

    private List<StockIndex> parseIndices(String str) {
        String[] split = str.split(";");
        List<StockIndex> stockIndexList = new ArrayList<>();
        for (String s : split) {
            stockIndexList.add(StockIndex.parseString(s));
        }
        return stockIndexList;
    }

    static class StockIndex {
        // 指数名称
        String indexName;
        // 当前点数
        double currentPoints;
        // 当前价格
        double currentPrice;
        // 涨跌率
        double risingAndFallingRate;
        // 成交量
        long volume;
        // 成交额
        long turnover;

        public static StockIndex parseString(String str) {
            String[] split = str.split(",");
            if (split.length != 6) {
                return null;
            }
            StockIndex stockIndex = new StockIndex();
            stockIndex.indexName = split[0].replace("\""," ").trim();
            stockIndex.currentPoints = Double.valueOf(split[1]);
            stockIndex.currentPrice = Double.valueOf(split[2]);
            stockIndex.risingAndFallingRate = Double.valueOf(split[3]);
            stockIndex.volume = Long.valueOf(split[4]);
            stockIndex.turnover = Long.valueOf(split[5]);
            return stockIndex;
        }
    }
}
