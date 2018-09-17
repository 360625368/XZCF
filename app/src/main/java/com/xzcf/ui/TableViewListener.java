package com.xzcf.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.evrencoskun.tableview.listener.ITableViewListener;

public class TableViewListener implements ITableViewListener {

    private OnCellClickListener mOnCellClickListener;
    private OnCellClickListener mOnCellLongClickListener;
    private OnColumnHeaderClickListener mOnColumnHeaderClickListener;
    private OnColumnHeaderClickListener mOnColumnHeaderLongClickListener;
    private OnRowHeaderClickListener mOnRowHeaderClickListener;
    private OnRowHeaderClickListener mOnRowHeaderLongClickListener;

    public TableViewListener setOnCellClickListener(OnCellClickListener onCellClickListener) {
        mOnCellClickListener = onCellClickListener;
        return this;
    }

    public TableViewListener setOnCellLongClickListener(OnCellClickListener onCellLongClickListener) {
        mOnCellLongClickListener = onCellLongClickListener;
        return this;
    }

    public TableViewListener setOnColumnHeaderClickListener(OnColumnHeaderClickListener onColumnHeaderClickListener) {
        mOnColumnHeaderClickListener = onColumnHeaderClickListener;
        return this;
    }

    public TableViewListener setOnColumnHeaderLongClickListener(OnColumnHeaderClickListener onColumnHeaderLongClickListener) {
        mOnColumnHeaderLongClickListener = onColumnHeaderLongClickListener;
        return this;
    }

    public TableViewListener setOnRowHeaderClickListener(OnRowHeaderClickListener onRowHeaderClickListener) {
        mOnRowHeaderClickListener = onRowHeaderClickListener;
        return this;
    }

    public TableViewListener setOnRowHeaderLongClickListener(OnRowHeaderClickListener onRowHeaderLongClickListener) {
        mOnRowHeaderLongClickListener = onRowHeaderLongClickListener;
        return this;
    }

    @Override
    public void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        if (mOnCellClickListener != null) {
            mOnCellClickListener.onCellClick(cellView.itemView, row, column);
        }
    }

    @Override
    public void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
        if (mOnCellLongClickListener != null) {
            mOnCellLongClickListener.onCellClick(cellView.itemView, row, column);
        }
    }

    @Override
    public void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
        if (mOnColumnHeaderClickListener != null) {
            mOnColumnHeaderClickListener.onColumnHeaderClick(columnHeaderView.itemView, column);
        }
    }

    @Override
    public void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
        if (mOnColumnHeaderLongClickListener != null) {
            mOnColumnHeaderLongClickListener.onColumnHeaderClick(columnHeaderView.itemView, column);
        }
    }

    @Override
    public void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        if (mOnRowHeaderClickListener != null) {
            mOnRowHeaderClickListener.onRowHeaderClick(rowHeaderView.itemView, row);
        }
    }

    @Override
    public void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
        if (mOnRowHeaderLongClickListener != null) {
            mOnRowHeaderLongClickListener.onRowHeaderClick(rowHeaderView.itemView, row);
        }
    }

    public interface OnCellClickListener {
        void onCellClick(View v, int row, int column);
    }

    public interface OnColumnHeaderClickListener {
        void onColumnHeaderClick(View v, int column);
    }

    public interface OnRowHeaderClickListener {
        void onRowHeaderClick(View v, int row);
    }
}
