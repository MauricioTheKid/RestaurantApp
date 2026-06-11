package com.example.restaurantapp.activities;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.restaurantapp.R;
import com.example.restaurantapp.database.AppDatabase;

public class ConfiguracionActivity extends AppCompatActivity {

    private Button btnBorrarPedidos, btnReiniciarVentas, btnBorrarTodo;
    private SharedPreferences preferences;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Configuración");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);
        preferences = getSharedPreferences("RestaurantPrefs", MODE_PRIVATE);

        btnBorrarPedidos = findViewById(R.id.btnBorrarPedidos);
        btnReiniciarVentas = findViewById(R.id.btnReiniciarVentas);
        btnBorrarTodo = findViewById(R.id.btnBorrarTodo);

        btnBorrarPedidos.setOnClickListener(v -> confirmarBorrarPedidos());
        btnReiniciarVentas.setOnClickListener(v -> confirmarReiniciarVentas());
        btnBorrarTodo.setOnClickListener(v -> confirmarBorrarTodo());
    }

    private void confirmarBorrarPedidos() {
        new AlertDialog.Builder(this)
                .setTitle("🗑️ Borrar Pedidos")
                .setMessage("¿Estás seguro de borrar TODOS los pedidos?\n\nEsta acción NO se puede deshacer.")
                .setPositiveButton("Sí, borrar", (dialog, which) -> {
                    db.pedidoDao().deleteAll();
                    db.detallePedidoDao().deleteAll();
                    Toast.makeText(this, "✅ Todos los pedidos han sido eliminados", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarReiniciarVentas() {
        new AlertDialog.Builder(this)
                .setTitle("💰 Reiniciar Ventas")
                .setMessage("¿Estás seguro de reiniciar las ventas?\n\nSe borrarán todos los pedidos pagados.")
                .setPositiveButton("Sí, reiniciar", (dialog, which) -> {
                    db.pedidoDao().deleteAll();
                    db.detallePedidoDao().deleteAll();
                    Toast.makeText(this, "✅ Ventas reiniciadas correctamente", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarBorrarTodo() {
        new AlertDialog.Builder(this)
                .setTitle("⚠️ Borrar TODOS los datos")
                .setMessage("¿Estás seguro de borrar TODOS los datos?\n\nSe borrarán:\n• Todos los pedidos\n• Todas las ventas\n• Todos los productos\n• Todos los usuarios\n\n⚠️ Esta acción NO se puede deshacer.")
                .setPositiveButton("Sí, borrar todo", (dialog, which) -> {
                    db.pedidoDao().deleteAll();
                    db.detallePedidoDao().deleteAll();
                    db.productoDao().deleteAll();
                    db.usuarioDao().deleteAll();

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    Toast.makeText(this, "✅ Todos los datos han sido eliminados", Toast.LENGTH_LONG).show();
                    finish();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}