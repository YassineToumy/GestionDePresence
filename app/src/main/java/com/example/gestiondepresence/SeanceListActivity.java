package com.example.gestiondepresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.SeanceAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Seance;

import java.util.List;

public class SeanceListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView seanceRecycler;
    private Button btnNewSeance;
    private TextView tvTitle, tvEmpty;
    private SeanceAdapter seanceAdapter;
    private List<Seance> seanceList;

    private int classId = -1;
    private String className = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seance_list);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get class info from intent
        Intent intent = getIntent();
        classId = intent.getIntExtra("classId", -1);
        className = intent.getStringExtra("className");

        // Initialize views
        seanceRecycler = findViewById(R.id.seanceRecycler);
        btnNewSeance = findViewById(R.id.btnNewSeance);
        tvTitle = findViewById(R.id.tvTitle);
        tvEmpty = findViewById(R.id.tvEmpty);

        // Set title
        if (className != null && !className.isEmpty()) {
            tvTitle.setText("Séances - " + className);
        } else {
            tvTitle.setText("Toutes les Séances");
        }

        // Setup RecyclerView
        seanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadSeances();

        // New seance button
        btnNewSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SeanceListActivity.this, CreateSeanceActivity.class);
                intent.putExtra("classId", classId);
                intent.putExtra("className", className);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSeances();
    }

    private void loadSeances() {
        if (classId != -1) {
            seanceList = db.getSeancesByClass(classId);
        } else {
            seanceList = db.getAllSeances();
        }

        if (seanceList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            seanceRecycler.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            seanceRecycler.setVisibility(View.VISIBLE);
            seanceAdapter = new SeanceAdapter(this, seanceList, db);
            seanceRecycler.setAdapter(seanceAdapter);
        }
    }
}