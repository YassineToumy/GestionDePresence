package com.example.gestiondepresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondepresence.database.DatabaseHelper;

public class MainMenuActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Button btnManageClasses, btnAddStudent, btnViewAllHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        btnManageClasses = findViewById(R.id.btnManageClasses);
        btnAddStudent = findViewById(R.id.btnAddStudent);
        btnViewAllHistory = findViewById(R.id.btnViewAllHistory);

        // Manage classes button
        btnManageClasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ManageClassesActivity.class);
                startActivity(intent);
            }
        });

        // Add student button (goes directly to add student form)
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, AddStudentActivity.class);
                startActivity(intent);
            }
        });

        // View all history button
        btnViewAllHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}