package com.example.spotlightnews.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotlightnews.API.ApiConfig;
import com.example.spotlightnews.API.ApiService;
import com.example.spotlightnews.Activity.DetailActivity;
import com.example.spotlightnews.Adapter.AdapterNews;
import com.example.spotlightnews.Article;
import com.example.spotlightnews.News.NewsResponse;
import com.example.spotlightnews.R;
import com.example.spotlightnews.SQLITE.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment implements AdapterNews.OnItemClickListener {

    private RecyclerView recyclerView;
    private AdapterNews adapterNews;
    private ArrayList<Article> articles = new ArrayList<>();
    private DatabaseHelper dbHelper;
    private ProgressBar progressBar;
    private Spinner spinnerCountry;
    private Spinner spinnerCategory;
    private TextView tvError;
    private Button btnRetry;
    private String selectedCountry = "in";
    private String selectedCategory = "general";
    private final List<String> countryCodes = Arrays.asList("in", "us", "gb", "ca", "id");
    private final Map<String, String> countryMap = new HashMap<String, String>() {{
        put("in", "India");
        put("us", "United States");
        put("gb", "United Kingdom");
        put("ca", "Canada");
        put("id", "Indonesia");
    }};
    private final List<String> categories = Arrays.asList("general", "business", "entertainment", "health", "science", "sports", "technology");
    private boolean isFetching = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        recyclerView = view.findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterNews = new AdapterNews(getContext(), articles, this);
        recyclerView.setAdapter(adapterNews);
        progressBar = view.findViewById(R.id.pb);
        tvError = view.findViewById(R.id.tv_error);
        btnRetry = view.findViewById(R.id.btn_retry);
        spinnerCountry = view.findViewById(R.id.spinner_country);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        setupSpinners();
        dbHelper = new DatabaseHelper(getContext());
        btnRetry.setOnClickListener(v -> {
            articles.clear();
            tvError.setVisibility(View.GONE);
            btnRetry.setVisibility(View.GONE);
            fetchNewsData();
        });
        fetchNewsData();
        return view;
    }

    private void setupSpinners() {
        List<String> countryNames = new ArrayList<>();
        for (String code : countryCodes) {
            countryNames.add(countryMap.get(code));
        }
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countryNames);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(countryAdapter);

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCountry = getKeyFromValue(countryMap, parent.getItemAtPosition(position).toString());
                articles.clear();
                fetchNewsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories.get(position);
                articles.clear();
                fetchNewsData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchNewsData() {
        if (isFetching) return;
        isFetching = true;
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        btnRetry.setVisibility(View.GONE);

        ApiService apiService = ApiConfig.getApiService();
        Call<NewsResponse> call = apiService.getData(selectedCountry, selectedCategory, "da2e2f186fb84f25b6687ea23817dad0");
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                isFetching = false;
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    articles.clear();
                    articles.addAll(response.body().getArticles());
                    adapterNews.notifyDataSetChanged();
                    if (articles.isEmpty()) {
                        showError();
                    }
                } else {
                    showError();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                isFetching = false;
                progressBar.setVisibility(View.GONE);
                showError();
            }
        });
    }

    private void showError() {
        tvError.setVisibility(View.VISIBLE);
        btnRetry.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        articles.clear();
        if (!isNetworkAvailable()) {
            showError();
        } else if (articles.isEmpty()) {
            fetchNewsData();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public void setArticles(ArrayList<Article> articles) {
        this.articles = articles;
    }

    private String getKeyFromValue(Map<String, String> map, String value) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
