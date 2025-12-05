package com.example.gestiondepresence;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.AttendanceAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Attendance;
import com.example.gestiondepresence.models.Student;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView attendanceRecycler;
    private Button btnValidate;
    private AttendanceAdapter attendanceAdapter;
    private List<Student> studentList;
    private String currentDate;
    private String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(new Date());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        sessionName = "Séance du " + currentDate + " à " + timeFormat.format(new Date());

        // Initialize views
        attendanceRecycler = findViewById(R.id.attendanceRecycler);
        btnValidate = findViewById(R.id.btnValidate);

        // Setup RecyclerView
        attendanceRecycler.setLayoutManager(new LinearLayoutManager(this));
        studentList = db.getAllStudents();
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
        for (Attendance attendance : attendanceList) {
            attendance.setDate(currentDate);
            attendance.setSessionName(sessionName);
            if (!db.addAttendance(attendance)) {
                allSaved = false;
            }
        }

        if (allSaved) {
            Toast.makeText(this, "Présence enregistrée avec succès", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement", Toast.LENGTH_SHORT).show();
        }
    }
}
