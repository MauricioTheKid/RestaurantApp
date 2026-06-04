package com.example.restaurantapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnLogin, btnRegister;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar vistas
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Inicializar SharedPreferences
        preferences = getSharedPreferences("RestaurantPrefs", MODE_PRIVATE);

        // Verificar si ya hay sesión activa
        if (preferences.getBoolean("isLoggedIn", false)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }

        // Botón Iniciar Sesión
        btnLogin.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            String savedUser = preferences.getString("usuario", "");
            String savedPass = preferences.getString("password", "");

            if (usuario.equals(savedUser) && password.equals(savedPass)) {
                // Guardar sesión
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("loggedUser", usuario);
                editor.apply();

                Toast.makeText(this, "✅ Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "❌ Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        // Botón Registrar
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}