package com.example.gestiondepresence;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.HistoryAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Attendance;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView historyRecycler;
    private HistoryAdapter historyAdapter;
    private List<Attendance> attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        historyRecycler = findViewById(R.id.historyRecycler);

        // Setup RecyclerView
        historyRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadHistory();
    }

    private void loadHistory() {
        attendanceList = db.getAllAttendance();
        historyAdapter = new HistoryAdapter(this, attendanceList, db);
        historyRecycler.setAdapter(historyAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory();
    }
}
