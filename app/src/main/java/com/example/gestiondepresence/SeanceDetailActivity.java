package com.example.gestiondepresence;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.HistoryAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.Seance;

import java.util.List;

public class SeanceDetailActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private TextView tvSeanceTitle, tvSubject, tvClassName, tvDateTime, tvStatus;
    private TextView tvPresentCount, tvAbsentCount, tvRetardCount;
    private RecyclerView attendanceRecycler;

    private int seanceId;
    private Seance seance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seance_detail);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get seance ID from intent
        seanceId = getIntent().getIntExtra("seanceId", -1);

        if (seanceId == -1) {
            finish();
            return;
        }

        // Initialize views
        tvSeanceTitle = findViewById(R.id.tvSeanceTitle);
        tvSubject = findViewById(R.id.tvSubject);
        tvClassName = findViewById(R.id.tvClassName);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvStatus = findViewById(R.id.tvStatus);
        tvPresentCount = findViewById(R.id.tvPresentCount);
        tvAbsentCount = findViewById(R.id.tvAbsentCount);
        tvRetardCount = findViewById(R.id.tvRetardCount);
        attendanceRecycler = findViewById(R.id.attendanceRecycler);

        // Load seance details
        loadSeanceDetails();
    }

    private void loadSeanceDetails() {
        seance = db.getSeanceById(seanceId);

        if (seance == null) {
            finish();
            return;
        }

        // Display seance info
        tvSeanceTitle.setText("Séance " + seance.getSeanceNumber());
        tvSubject.setText("Matière: " + seance.getSubjectName());
        tvClassName.setText("Classe: " + seance.getClassName());
        tvDateTime.setText("Date: " + seance.getDate() + " à " + seance.getTime());

        if ("completed".equals(seance.getStatus())) {
            tvStatus.setText("Statut: ✅ Terminée");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else {
            tvStatus.setText("Statut: ⏳ En cours");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }

        // Load attendance for this seance
        List<Attendance> attendanceList = db.getAttendanceBySeance(seanceId);

        // Calculate statistics
        int presentCount = 0;
        int absentCount = 0;
        int retardCount = 0;

        for (Attendance attendance : attendanceList) {
            switch (attendance.getStatus()) {
                case "present":
                    presentCount++;
                    break;
                case "absent":
                    absentCount++;
                    break;
                case "retard":
                    retardCount++;
                    break;
            }
        }

        // Display statistics
        tvPresentCount.setText("Présents: " + presentCount);
        tvAbsentCount.setText("Absents: " + absentCount);
        tvRetardCount.setText("Retards: " + retardCount);

        // Setup RecyclerView
        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        HistoryAdapter adapter = new HistoryAdapter(this, attendanceList, db);
        attendanceRecycler.setAdapter(adapter);
    }
}