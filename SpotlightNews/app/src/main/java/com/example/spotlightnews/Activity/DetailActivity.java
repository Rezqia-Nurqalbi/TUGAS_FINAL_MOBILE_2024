package com.example.spotlightnews.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotlightnews.Article;
import com.example.spotlightnews.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DetailActivity extends AppCompatActivity {

    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    private ImageView imageView;
    private TextView titleTextView;
    private TextView descTextView;
    private TextView sourceTextView;
    private TextView authorTextView;
    private TextView publishedAtTextView;
    private TextView contentTextView;
    private Button openUrlButton;
    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageView = findViewById(R.id.iv_articleItem);
        titleTextView = findViewById(R.id.tv_titleArticleItem);
        descTextView = findViewById(R.id.tv_descArticleItem);
        sourceTextView = findViewById(R.id.tv_sourceArticleItem);
        authorTextView = findViewById(R.id.tv_authorArticleItem);
        publishedAtTextView = findViewById(R.id.tv_publishedAtArticleItem);
        contentTextView = findViewById(R.id.tv_contentArticleItem);
        openUrlButton = findViewById(R.id.openUrlButton);

        article = getIntent().getParcelableExtra("article");

        if (article != null) {
            executor.execute(() -> {
                String imageUrl = article.getUrlToImage();
                try {
                    final Bitmap bitmap;
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        bitmap = Picasso.get().load(imageUrl).get();
                    } else {
                        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.notfound);
                    }

                    handler.post(() -> {
                        imageView.setImageBitmap(bitmap);
                        titleTextView.setText(article.getTitle());
                        descTextView.setText(article.getDescription() != null ? article.getDescription() : "Null");
                        sourceTextView.setText("Source: " + (article.getSourceName() != null ? article.getSourceName() : "Null"));
                        authorTextView.setText("Author: " + (article.getAuthor() != null ? article.getAuthor() : "Null"));
                        publishedAtTextView.setText("Published At: " + (article.getPublishedAt() != null ? article.getPublishedAt() : "Null"));
                        contentTextView.setText(article.getContent() != null ? article.getContent() : "Null");

                        openUrlButton.setOnClickListener(v -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(article.getUrl()));
                            startActivity(browserIntent);
                        });
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
