package com.example.restaurantapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsuario, etEmail, etPassword, etConfirmPassword;
    private Button btnGuardar, btnRegresar;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar vistas
        etUsuario = findViewById(R.id.etUsuario);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnRegresar = findViewById(R.id.btnRegresar);

        // Inicializar SharedPreferences
        preferences = getSharedPreferences("RestaurantPrefs", MODE_PRIVATE);

        // Botón Guardar
        btnGuardar.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validación: Usuario mínimo 3 caracteres
            if (usuario.length() < 3) {
                etUsuario.setError("Mínimo 3 caracteres");
                etUsuario.requestFocus();
                return;
            }

            // Validación: Email válido
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Correo electrónico no válido");
                etEmail.requestFocus();
                return;
            }

            // Validación: Password mínimo 5 caracteres alfanumérico
            if (password.length() < 5) {
                etPassword.setError("Mínimo 5 caracteres");
                etPassword.requestFocus();
                return;
            }

            if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
                etPassword.setError("Debe contener letras y números");
                etPassword.requestFocus();
                return;
            }

            // Validación: Confirmar password
            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Las contraseñas no coinciden");
                etConfirmPassword.requestFocus();
                return;
            }

            // Guardar en SharedPreferences
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("usuario", usuario);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();

            Toast.makeText(this, "✅ Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            finish(); // Regresar al Login
        });

        // Botón Regresar
        btnRegresar.setOnClickListener(v -> {
            finish();
        });
    }
}