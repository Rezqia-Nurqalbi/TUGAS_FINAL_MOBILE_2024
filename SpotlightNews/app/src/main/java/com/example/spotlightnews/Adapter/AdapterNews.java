package com.example.spotlightnews.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotlightnews.Article;
import com.example.spotlightnews.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterNews extends RecyclerView.Adapter<AdapterNews.NewsViewHolder> {

    private List<Article> articleList;
    private Context context;
    private OnItemClickListener mListener;

    public AdapterNews(Context context, List<Article> articleList, OnItemClickListener listener) {
        this.context = context;
        this.articleList = articleList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvPublishedAt.setText("Published At: " + article.getPublishedAt());

        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Picasso.get().load(article.getUrlToImage()).into(holder.ivImageNews);
        } else {
            holder.ivImageNews.setImageResource(R.drawable.notfound);
        }

        holder.itemView.setOnClickListener(v -> mListener.onItemClick(article));
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImageNews;
        TextView tvTitle;
        TextView tvPublishedAt;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImageNews = itemView.findViewById(R.id.iv_imageNews);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvPublishedAt = itemView.findViewById(R.id.tv_publishedAt);
        }
    }
}
