package com.xzcf.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xzcf.R;


public class StockIndexSortView {


    private Context mContext;
    private RecyclerView mParentView;

    public StockIndexSortView(Context context,RecyclerView recyclerView) {
        mContext = context;
        this.mParentView = recyclerView;
    }

    public View initView(){
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.view_stock_customize_sort,(ViewGroup) mParentView.getParent(), false);

        return inflate;
    }

}
