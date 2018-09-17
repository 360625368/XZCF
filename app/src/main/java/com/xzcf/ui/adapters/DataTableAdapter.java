package com.xzcf.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.evrencoskun.tableview.adapter.AbstractTableAdapter;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;
import com.xzcf.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DataTableAdapter extends AbstractTableAdapter<String, String, String> {

    private List<Integer> mGravity;

    public DataTableAdapter(Context context) {
        super(context);
    }

    public void setGravity(List<Integer> gravity) {
        this.mGravity = gravity;
    }

    @Override
    public int getColumnHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getRowHeaderItemViewType(int position) {
        return 0;
    }

    @Override
    public int getCellItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateCellViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_table_cell, parent, false);
        return new CellViewHolder(view);
    }

    @Override
    public void onBindCellViewHolder(AbstractViewHolder holder, Object cellItemModel, int columnPosition, int rowPosition) {
        String cell = (String) cellItemModel;
        ((CellViewHolder) holder).tvText.setGravity(mGravity.get(columnPosition));
        ((CellViewHolder) holder).tvText.setText(cell);

        ((CellViewHolder) holder).itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        ((CellViewHolder) holder).tvText.requestLayout();
    }

    @Override
    public RecyclerView.ViewHolder onCreateColumnHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_table_column_header, parent, false);
        return new ColumnHeaderViewHolder(view);
    }

    @Override
    public void onBindColumnHeaderViewHolder(AbstractViewHolder holder, Object columnHeaderItemModel, int columnPosition) {
        String columnHeader = (String) columnHeaderItemModel;
        ((ColumnHeaderViewHolder) holder).tvText.setGravity(mGravity.get(columnPosition));
        ((ColumnHeaderViewHolder) holder).tvText.setText(columnHeader);

        ((ColumnHeaderViewHolder) holder).itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
        ((ColumnHeaderViewHolder) holder).tvText.requestLayout();
    }

    @Override
    public RecyclerView.ViewHolder onCreateRowHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_table_row_header, parent, false);
        return new RowHeaderViewHolder(view);
    }

    @Override
    public void onBindRowHeaderViewHolder(AbstractViewHolder holder, Object rowHeaderItemModel, int rowPosition) {
        ((RowHeaderViewHolder) holder).itemView.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public View onCreateCornerView() {
        return null;
    }

    static class CellViewHolder extends AbstractViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        public CellViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class RowHeaderViewHolder extends AbstractViewHolder {

        public RowHeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class ColumnHeaderViewHolder extends AbstractViewHolder {
        @BindView(R.id.tvText)
        TextView tvText;

        public ColumnHeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}