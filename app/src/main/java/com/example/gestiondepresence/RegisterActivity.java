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
import com.example.gestiondepresence.models.User;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText registerEmail, registerPassword, registerConfirmPassword;
    private Button btnRegister;
    private TextView txtGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize database
        db = new DatabaseHelper(this);

        // Note: Cette page est UNIQUEMENT pour l'inscription des PROFESSEURS
        // Les étudiants sont ajoutés via AddStudentActivity

        // Initialize views
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txtGoToLogin = findViewById(R.id.txtGoToLogin);

        // Register button click
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmail.getText().toString().trim();
                String password = registerPassword.getText().toString().trim();
                String confirmPassword = registerConfirmPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegisterActivity.this, "Email invalide", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Le mot de passe doit contenir au moins 6 caractères", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (db.checkUserExists(email)) {
                    Toast.makeText(RegisterActivity.this, "Cet email existe déjà", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(email, password);
                if (db.registerUser(user)) {
                    Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Erreur lors de l'inscription", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Go to login
        txtGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}