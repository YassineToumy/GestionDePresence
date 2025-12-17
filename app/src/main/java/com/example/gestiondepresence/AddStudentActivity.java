package com.example.gestiondepresence;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.ClassRoom;
import com.example.gestiondepresence.models.Student;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText name, surname;
    private AutoCompleteTextView group;
    private Button btnSave;
    private List<String> classNames;

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

        // Charger les noms de classes pour l'autocomplétion
        loadClassNames();

        // Configurer l'autocomplétion pour le groupe
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, classNames);
        group.setAdapter(adapter);
        group.setThreshold(1); // Commence à suggérer après 1 caractère

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

                // Vérifier si la classe existe
                if (!db.classExists(studentGroup)) {
                    Toast.makeText(AddStudentActivity.this,
                            "Erreur: La classe '" + studentGroup + "' n'existe pas. Veuillez d'abord créer cette classe.",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Student student = new Student(studentName, studentSurname, studentGroup);
                if (db.addStudent(student)) {
                    Toast.makeText(AddStudentActivity.this, "Étudiant ajouté avec succès à la classe " + studentGroup, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddStudentActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadClassNames() {
        classNames = new ArrayList<>();
        List<ClassRoom> classes = db.getAllClasses();
        for (ClassRoom classRoom : classes) {
            classNames.add(classRoom.getClassName());
        }
    }
}