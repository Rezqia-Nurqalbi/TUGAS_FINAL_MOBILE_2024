package com.example.spotlightnews.Activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.spotlightnews.Article;
import com.example.spotlightnews.Fragment.HistoryFragment;
import com.example.spotlightnews.Fragment.NewsFragment;
import com.example.spotlightnews.Fragment.SearchFragment;
import com.example.spotlightnews.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NewsFragment newsFragment;
    private HistoryFragment historyFragment;
    private SearchFragment searchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsFragment = new NewsFragment();
        historyFragment = new HistoryFragment();
        searchFragment = new SearchFragment(new ArrayList<>());

        loadFragment(newsFragment);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int id = item.getItemId();
                    if (id == R.id.nav_news) {
                        selectedFragment = newsFragment;
                    } else if (id == R.id.nav_search) {
                        // Mendapatkan daftar artikel dari newsFragment
                        ArrayList<Article> articles = newsFragment.getArticles();
                        // Membuat SearchFragment dengan artikel yang diperoleh
                        selectedFragment = new SearchFragment(articles);
                    } else if (id == R.id.nav_history) {
                        selectedFragment = historyFragment;
                    }

                    loadFragment(selectedFragment);
                    return true;
                }
            };


    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }
}
