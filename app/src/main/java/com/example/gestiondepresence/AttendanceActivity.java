package com.example.gestiondepresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.AttendanceAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.Student;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView attendanceRecycler;
    private Button btnValidate;
    private TextView tvSeanceInfo;
    private AttendanceAdapter attendanceAdapter;
    private List<Student> studentList;

    private String currentDate;
    private String sessionName;

    // Seance info
    private int seanceId = -1;
    private int classId = -1;
    private String className = "";
    private String subjectName = "";
    private int seanceNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get seance info from intent
        Intent intent = getIntent();
        seanceId = intent.getIntExtra("seanceId", -1);
        classId = intent.getIntExtra("classId", -1);
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        seanceNumber = intent.getIntExtra("seanceNumber", 0);

        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(new Date());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        // Build session name based on seance info
        if (seanceId != -1 && subjectName != null) {
            sessionName = "Séance " + seanceNumber + " - " + subjectName + " (" + className + ") - " + currentDate;
        } else {
            sessionName = "Séance du " + currentDate + " à " + timeFormat.format(new Date());
        }

        // Initialize views
        attendanceRecycler = findViewById(R.id.attendanceRecycler);
        btnValidate = findViewById(R.id.btnValidate);
        tvSeanceInfo = findViewById(R.id.tvSeanceInfo);

        // Display seance info
        if (tvSeanceInfo != null) {
            if (seanceId != -1) {
                tvSeanceInfo.setText("Séance " + seanceNumber + " - " + subjectName + "\nClasse: " + className);
                tvSeanceInfo.setVisibility(View.VISIBLE);
            } else {
                tvSeanceInfo.setVisibility(View.GONE);
            }
        }

        // Setup RecyclerView
        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));

        // Load students based on class
        if (classId != -1 && className != null) {
            studentList = db.getStudentsByClassName(className);
        } else {
            studentList = db.getAllStudents();
        }

        if (studentList.isEmpty()) {
            Toast.makeText(this, "Aucun étudiant dans cette classe", Toast.LENGTH_LONG).show();
        }

        attendanceAdapter = new AttendanceAdapter(this, studentList);
        attendanceRecycler.setAdapter(attendanceAdapter);

        // Validate button
        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAttendance();
            }
        });
    }

    private void saveAttendance() {
        List<Attendance> attendanceList = attendanceAdapter.getAttendanceData();

        if (attendanceList.isEmpty()) {
            Toast.makeText(this, "Aucune donnée de présence", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean allSaved = true;
        int presentCount = 0;
        int absentCount = 0;
        int retardCount = 0;

        for (Attendance attendance : attendanceList) {
            attendance.setDate(currentDate);
            attendance.setSessionName(sessionName);
            attendance.setSeanceId(seanceId); // Link to seance

            if (!db.addAttendance(attendance)) {
                allSaved = false;
            } else {
                // Count statistics
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
        }

        // Mark seance as completed
        if (seanceId != -1) {
            db.updateSeanceStatus(seanceId, "completed");
        }

        if (allSaved) {
            String message = "Présence enregistrée!\n" +
                    "Présents: " + presentCount + "\n" +
                    "Absents: " + absentCount + "\n" +
                    "Retards: " + retardCount;
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }
}