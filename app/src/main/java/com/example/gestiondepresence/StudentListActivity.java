package com.example.gestiondepresence;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
    private Button btnAdd, btnAttendance;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        studentRecycler = findViewById(R.id.studentRecycl);
        btnAdd = findViewById(R.id.btnAdd);
        btnAttendance = findViewById(R.id.btnAttendance);

        // Setup RecyclerView
        studentRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadStudents();

        // Add student button
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });

        // Attendance button
        btnAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentListActivity.this, AttendanceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        studentList = db.getAllStudents();
        studentAdapter = new StudentAdapter(this, studentList, db);
        studentRecycler.setAdapter(studentAdapter);
    }
}
