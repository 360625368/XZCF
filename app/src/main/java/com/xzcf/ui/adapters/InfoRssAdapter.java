package com.xzcf.ui.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xzcf.R;
import com.xzcf.data.data.response.NewsResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoRssAdapter extends RecyclerView.Adapter<InfoRssAdapter.ViewHolder> {

    List<NewsResponse.Rss> rsses;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsResponse.Rss rss = rsses.get(holder.getAdapterPosition());
        holder.tvTitle.setText(rss.getTitle());
        holder.tvDatetime.setText(rss.getPubDate());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(rss);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rsses == null ? 0 : rsses.size();
    }

    public void setRsses(List<NewsResponse.Rss> rsses) {
        this.rsses = rsses;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvDatetime)
        TextView tvDatetime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(NewsResponse.Rss rss);
    }
}
