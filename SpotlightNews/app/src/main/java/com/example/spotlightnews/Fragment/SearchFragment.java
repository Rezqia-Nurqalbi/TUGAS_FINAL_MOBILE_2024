package com.example.spotlightnews.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotlightnews.Activity.DetailActivity;
import com.example.spotlightnews.Adapter.AdapterNews;
import com.example.spotlightnews.Article;
import com.example.spotlightnews.R;
import com.example.spotlightnews.SQLITE.DatabaseHelper;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements AdapterNews.OnItemClickListener {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView noDataText;
    private ProgressBar progressBar;
    private AdapterNews adapterNews;
    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Article> filteredArticles = new ArrayList<>();
    private DatabaseHelper dbHelper;

    public SearchFragment(ArrayList<Article> articles) {
        this.articles = articles;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.rv_searchView);
        noDataText = view.findViewById(R.id.no_data_text);
        progressBar = view.findViewById(R.id.pb);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterNews = new AdapterNews(getContext(), filteredArticles, this);
        recyclerView.setAdapter(adapterNews);

        dbHelper = new DatabaseHelper(getContext());

        progressBar.setVisibility(View.GONE);
        noDataText.setVisibility(View.GONE);

        setupSearchView();

        return view;
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                } else {
                    filterArticles(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show();
                } else {
                    filterArticles(newText);
                }
                return false;
            }
        });
    }

    private void filterArticles(String query) {
        progressBar.setVisibility(View.VISIBLE);
        noDataText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);

        filteredArticles.clear();
        adapterNews.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<Article> newFilteredArticles = new ArrayList<>();

                if (query.isEmpty()) {
                    newFilteredArticles.addAll(articles);
                } else {
                    for (Article article : articles) {
                        if (article.getTitle().toLowerCase().contains(query.toLowerCase())) {
                            newFilteredArticles.add(article);
                        }
                    }
                }

                filteredArticles.clear();
                filteredArticles.addAll(newFilteredArticles);

                progressBar.setVisibility(View.GONE);

                if (filteredArticles.isEmpty()) {
                    noDataText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    noDataText.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                adapterNews.notifyDataSetChanged();
            }
        }, 1000);
    }

    @Override
    public void onItemClick(Article article) {
        boolean isInserted = dbHelper.addNewsHistory(article.getSourceName(), article.getAuthor(),
                article.getTitle(), article.getDescription(), article.getUrl(), article.getUrlToImage(),
                article.getPublishedAt(), article.getContent());
        if (isInserted) {
            Toast.makeText(getContext(), "Added to history", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Failed to add to history", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("article", article);
        startActivity(intent);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
