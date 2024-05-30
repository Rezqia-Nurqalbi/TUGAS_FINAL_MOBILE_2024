package com.example.spotlightnews.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotlightnews.Adapter.HistoryAdapter;
import com.example.spotlightnews.Article;
import com.example.spotlightnews.R;
import com.example.spotlightnews.SQLITE.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<Article> articles = new ArrayList<>();
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(getContext(), articles);
        recyclerView.setAdapter(adapter);
        dbHelper = new DatabaseHelper(getContext());
        loadHistoryData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHistoryData();
    }

    private void loadHistoryData() {
        articles.clear();
        articles.addAll(dbHelper.getAllData());
        adapter.notifyDataSetChanged();

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.scrollToPosition(0);
            }
        });

        if (articles.isEmpty()) {
            Toast.makeText(getContext(), "Tidak ada data riwayat yang tersedia", Toast.LENGTH_SHORT).show();
        }
    }
}
