package com.example.mycounters;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "my_counters_prefs";
    public static final String KEYS_SET = "counter_keys";

    private SharedPreferences prefs;

    List<Counter> counterList = new ArrayList<>();

    CounterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadCounters(new HashSet<String>(prefs.getStringSet(KEYS_SET, new HashSet<>())));

        RecyclerView view = findViewById(R.id.recyclerView);
        view.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CounterAdapter(counterList);
        view.setAdapter(adapter);

        FloatingActionButton addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(v -> showAddDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadCounters(Set<String> keys) {
        for (String k : keys) {
            counterList.add(new Counter(prefs, k));
        }
    }

    private void showAddDialog() {
        EditText input = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("New counter")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        addCounter(name);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addCounter(String name) {
        Counter newCounter = new Counter(prefs, name);
        counterList.add(newCounter);

        Set<String> keys = new HashSet<>(prefs.getStringSet(KEYS_SET, new HashSet<>()));
        keys.add(name);

        prefs.edit().putStringSet(KEYS_SET, keys).apply();
        adapter.notifyItemInserted(counterList.size() - 1);
    }
}