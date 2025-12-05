package com.example.gestiondepresence;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Student;

public class AddStudentActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText name, surname, group;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        name = findViewById(R.id.name);
        surname = findViewById(R.id.surname);
        group = findViewById(R.id.group);
        btnSave = findViewById(R.id.btnSave);

        // Save button click
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String studentName = name.getText().toString().trim();
                String studentSurname = surname.getText().toString().trim();
                String studentGroup = group.getText().toString().trim();

                if (studentName.isEmpty() || studentSurname.isEmpty() || studentGroup.isEmpty()) {
                    Toast.makeText(AddStudentActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                Student student = new Student(studentName, studentSurname, studentGroup);
                if (db.addStudent(student)) {
                    Toast.makeText(AddStudentActivity.this, "Étudiant ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
