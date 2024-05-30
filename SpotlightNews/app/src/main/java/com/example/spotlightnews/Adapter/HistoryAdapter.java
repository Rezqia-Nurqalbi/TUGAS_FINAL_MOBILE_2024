package com.example.spotlightnews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotlightnews.Activity.DetailActivity;
import com.example.spotlightnews.Article;
import com.example.spotlightnews.R;
import com.example.spotlightnews.SQLITE.DatabaseHelper;
import com.squareup.picasso.Picasso;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private Context context;
    private List<Article> articles;

    public HistoryAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvDesc.setText(article.getDescription());

        // Perbarui waktu terakhir dibuka
        String lastOpenedText = "Terakhir Dibuka: " + article.getLastOpened();
        holder.tvLastOpened.setText(lastOpenedText);

        if (article.getUrlToImage() != null && !article.getUrlToImage().isEmpty()) {
            Picasso.get().load(article.getUrlToImage()).into(holder.ivImage);
        } else {
            holder.ivImage.setImageResource(R.drawable.notfound);
        }

        holder.itemView.setOnClickListener(v -> {
            // Perbarui waktu terakhir dibuka saat pengguna membuka detail
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            boolean isUpdated = dbHelper.updateLastOpened(article.getTitle());
            if (isUpdated) {
                article.setLastOpened(dbHelper.getCurrentTime());
                notifyDataSetChanged(); // Perbarui tampilan untuk menampilkan perubahan waktu terakhir dibuka
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("article", article);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Gagal memperbarui waktu terakhir dibuka", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        TextView tvDesc;
        TextView tvLastOpened;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_newsImage);
            tvTitle = itemView.findViewById(R.id.tv_newsTitle);
            tvDesc = itemView.findViewById(R.id.tv_newsDesc);
            tvLastOpened = itemView.findViewById(R.id.tv_lastOpened);
        }
    }
}
