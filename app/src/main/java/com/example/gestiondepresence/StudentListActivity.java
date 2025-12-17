package com.example.gestiondepresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.StudentAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Student;

import java.util.List;

public class StudentListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView studentRecycler;
    private Button btnAdd, btnNewSeance, btnHistory, btnManageClasses, btnViewSeances;
    private TextView txtClassTitle;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    private int classId = -1;
    private String className = "";
    private boolean isViewingClass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get class info from intent if coming from ManageClassesActivity
        Intent intent = getIntent();
        if (intent.hasExtra("classId")) {
            classId = intent.getIntExtra("classId", -1);
            className = intent.getStringExtra("className");
            isViewingClass = true;
        }

        // Initialize views
        studentRecycler = findViewById(R.id.studentRecycler);
        btnAdd = findViewById(R.id.btnAdd);
        btnNewSeance = findViewById(R.id.btnNewSeance);
        btnHistory = findViewById(R.id.btnHistory);
        btnManageClasses = findViewById(R.id.btnManageClasses);
        btnViewSeances = findViewById(R.id.btnViewSeances);
        txtClassTitle = findViewById(R.id.txtClassTitle);

        // Configure view based on whether we're viewing a specific class or main menu
        if (isViewingClass) {
            // Mode: Viewing specific class students
            txtClassTitle.setText("Classe: " + className);
            txtClassTitle.setVisibility(View.VISIBLE);
            studentRecycler.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.VISIBLE);
            btnNewSeance.setVisibility(View.VISIBLE);  // Show new seance button
            btnHistory.setVisibility(View.VISIBLE);
            btnViewSeances.setVisibility(View.VISIBLE);
            btnManageClasses.setVisibility(View.GONE);

            // Setup RecyclerView
            studentRecycler.setLayoutManager(new LinearLayoutManager(this));
            loadStudents();
        } else {
            // Mode: Main menu (no students shown)
            txtClassTitle.setText("Menu Principal - Gestion de Présence");
            txtClassTitle.setVisibility(View.VISIBLE);
            studentRecycler.setVisibility(View.GONE);
            btnAdd.setVisibility(View.GONE);
            btnNewSeance.setVisibility(View.GONE);
            btnHistory.setVisibility(View.GONE);
            btnViewSeances.setVisibility(View.GONE);
            btnManageClasses.setVisibility(View.VISIBLE);
        }

        // Manage classes button
        btnManageClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, ManageClassesActivity.class);
                startActivity(intent);
            }
        });

        // Add student button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });

        // NEW: New Seance button - Creates a new seance then takes attendance
        btnNewSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, CreateSeanceActivity.class);
                intent.putExtra("classId", classId);
                intent.putExtra("className", className);
                startActivity(intent);
            }
        });

        // View seances button
        if (btnViewSeances != null) {
            btnViewSeances.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudentListActivity.this, SeanceListActivity.class);
                    intent.putExtra("classId", classId);
                    intent.putExtra("className", className);
                    startActivity(intent);
                }
            });
        }

        // History button
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, HistoryActivity.class);
                if (classId != -1) {
                    intent.putExtra("classId", classId);
                    intent.putExtra("className", className);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isViewingClass) {
            loadStudents();
        }
    }

    private void loadStudents() {
        if (classId != -1) {
            studentList = db.getStudentsByClass(classId);
        } else {
            studentList = db.getAllStudents();
        }
        studentAdapter = new StudentAdapter(this, studentList, db);
        studentRecycler.setAdapter(studentAdapter);
    }
}