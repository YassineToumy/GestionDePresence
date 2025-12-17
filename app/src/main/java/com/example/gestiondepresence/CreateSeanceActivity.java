package com.example.gestiondepresence;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestiondepresence.database.DatabaseHelper;
import com.example.gestiondepresence.models.Seance;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateSeanceActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etSubjectName, etSeanceNumber;
    private TextView tvClassName, tvDate, tvTime;
    private Button btnCreateSeance, btnCancel;

    private int classId;
    private String className;
    private String currentDate;
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_seance);

        // Initialize database
        db = new DatabaseHelper(this);

        // Get class info from intent
        Intent intent = getIntent();
        classId = intent.getIntExtra("classId", -1);
        className = intent.getStringExtra("className");

        if (classId == -1 || className == null) {
            Toast.makeText(this, "Erreur: Classe non spécifiée", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Date now = new Date();
        currentDate = dateFormat.format(now);
        currentTime = timeFormat.format(now);

        // Initialize views
        tvClassName = findViewById(R.id.tvClassName);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        etSubjectName = findViewById(R.id.etSubjectName);
        etSeanceNumber = findViewById(R.id.etSeanceNumber);
        btnCreateSeance = findViewById(R.id.btnCreateSeance);
        btnCancel = findViewById(R.id.btnCancel);

        // Set class info
        tvClassName.setText("Classe: " + className);
        tvDate.setText("Date: " + currentDate);
        tvTime.setText("Heure: " + currentTime);

        // Suggest next seance number
        int nextSeanceNumber = db.getNextSeanceNumber(classId);
        etSeanceNumber.setText(String.valueOf(nextSeanceNumber));

        // Create seance button
        btnCreateSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSeance();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void createSeance() {
        String subjectName = etSubjectName.getText().toString().trim();
        String seanceNumberStr = etSeanceNumber.getText().toString().trim();

        // Validation
        if (subjectName.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer le nom de la matière", Toast.LENGTH_SHORT).show();
            return;
        }

        if (seanceNumberStr.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer le numéro de séance", Toast.LENGTH_SHORT).show();
            return;
        }

        int seanceNumber;
        try {
            seanceNumber = Integer.parseInt(seanceNumberStr);
            if (seanceNumber <= 0) {
                Toast.makeText(this, "Le numéro de séance doit être positif", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Numéro de séance invalide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create seance object
        Seance seance = new Seance(classId, className, subjectName, seanceNumber, currentDate, currentTime);

        // Save to database
        long seanceId = db.addSeance(seance);

        if (seanceId != -1) {
            Toast.makeText(this, "Séance créée avec succès", Toast.LENGTH_SHORT).show();

            // Go to attendance activity with seance info
            Intent intent = new Intent(CreateSeanceActivity.this, AttendanceActivity.class);
            intent.putExtra("seanceId", (int) seanceId);
            intent.putExtra("classId", classId);
            intent.putExtra("className", className);
            intent.putExtra("subjectName", subjectName);
            intent.putExtra("seanceNumber", seanceNumber);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Erreur lors de la création de la séance", Toast.LENGTH_SHORT).show();
        }
    }
}