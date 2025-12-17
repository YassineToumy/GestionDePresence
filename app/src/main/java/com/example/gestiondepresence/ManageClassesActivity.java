package com.example.gestiondepresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestiondepresence.adapters.ClassAdapter;
import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.ClassRoom;

import java.util.List;

public class ManageClassesActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private RecyclerView classRecycler;
    private Button btnAddClass;
    private ClassAdapter classAdapter;
    private List<ClassRoom> classList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_classes);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        classRecycler = findViewById(R.id.classRecycler);
        btnAddClass = findViewById(R.id.btnAddClass);

        // Setup RecyclerView
        classRecycler.setLayoutManager(new LinearLayoutManager(this));
        loadClasses();

        // Add class button
        btnAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageClassesActivity.this, AddClassActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadClasses();
    }

    private void loadClasses() {
        classList = db.getAllClasses();
        classAdapter = new ClassAdapter(this, classList, db);
        classRecycler.setAdapter(classAdapter);
    }
}