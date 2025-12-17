package com.example.gestiondepresence;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.ClassRoom;

public class AddClassActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText className, classDescription;
    private Button btnSaveClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        // Initialize database
        db = new DatabaseHelper(this);

        // Initialize views
        className = findViewById(R.id.className);
        classDescription = findViewById(R.id.classDescription);
        btnSaveClass = findViewById(R.id.btnSaveClass);

        // Save button click
        btnSaveClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = className.getText().toString().trim();
                String description = classDescription.getText().toString().trim();

                if (name.isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "Veuillez entrer un nom de classe", Toast.LENGTH_SHORT).show();
                    return;
                }

                ClassRoom classRoom = new ClassRoom(name, description);
                if (db.addClass(classRoom)) {
                    Toast.makeText(AddClassActivity.this, "Classe ajoutée avec succès", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddClassActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}