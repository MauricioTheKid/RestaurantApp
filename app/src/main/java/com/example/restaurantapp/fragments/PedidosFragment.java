package com.example.restaurantapp.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.restaurantapp.R;
import com.example.restaurantapp.activities.EditarPedidoActivity;
import com.example.restaurantapp.database.AppDatabase;
import com.example.restaurantapp.models.Pedido;
import java.util.List;

public class PedidosFragment extends Fragment {

    private LinearLayout layoutPedidos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        layoutPedidos = view.findViewById(R.id.layoutPedidos);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        db = AppDatabase.getInstance(requireContext());
        preferences = requireContext().getSharedPreferences("RestaurantPrefs", 0);

        cargarPedidos();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarPedidos();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "✅ Pedidos actualizados", Toast.LENGTH_SHORT).show();
            }, 1000);
        });

        return view;
    }

    private void cargarPedidos() {
        layoutPedidos.removeAllViews();

        List<Pedido> pedidos = db.pedidoDao().getAllPedidos();

        if (pedidos == null || pedidos.isEmpty()) {
            TextView tvVacio = new TextView(getContext());
            tvVacio.setText("📭 No hay pedidos registrados");
            tvVacio.setTextSize(16);
            tvVacio.setTextColor(0xFF999999);
            tvVacio.setPadding(20, 50, 20, 20);
            layoutPedidos.addView(tvVacio);
            return;
        }

        for (Pedido pedido : pedidos) {
            LinearLayout card = crearCardPedido(pedido);
            layoutPedidos.addView(card);
        }
    }

    private LinearLayout crearCardPedido(Pedido pedido) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 15, 20, 15);
        card.setBackgroundColor(0xFFF5F5F5);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 15);
        card.setLayoutParams(params);

        // ID del pedido
        TextView tvId = new TextView(getContext());
        tvId.setText("🆔 Pedido #" + pedido.getId());
        tvId.setTextSize(16);
        tvId.setTextColor(0xFF333333);
        tvId.setTypeface(null, android.graphics.Typeface.BOLD);

        // Mesa
        TextView tvMesa = new TextView(getContext());
        tvMesa.setText("🍽️ Mesa: " + pedido.getNumeroMesa());
        tvMesa.setTextSize(14);
        tvMesa.setTextColor(0xFF666666);

        // Total
        TextView tvTotal = new TextView(getContext());
        tvTotal.setText("💰 Total: $" + String.format("%.2f", pedido.getTotal()));
        tvTotal.setTextSize(15);
        tvTotal.setTextColor(0xFF4CAF50);
        tvTotal.setTypeface(null, android.graphics.Typeface.BOLD);

        // Estado
        TextView tvEstado = new TextView(getContext());
        String estado = pedido.getEstado();
        tvEstado.setText("📌 Estado: " + getEstadoTexto(estado));
        tvEstado.setTextSize(14);
        tvEstado.setTextColor(getEstadoColor(estado));

        // Layout de botones
        LinearLayout layoutBotones = new LinearLayout(getContext());
        layoutBotones.setOrientation(LinearLayout.HORIZONTAL);
        layoutBotones.setPadding(0, 15, 0, 0);

        // Botón EDITAR (solo si está pendiente)
        if (estado.equals("pendiente")) {
            Button btnEditar = new Button(getContext());
            btnEditar.setText("✏️ Editar");
            btnEditar.setBackgroundColor(0xFF2196F3);
            btnEditar.setTextColor(0xFFFFFFFF);
            btnEditar.setPadding(16, 8, 16, 8);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );
            btnParams.setMargins(0, 0, 8, 0);
            btnEditar.setLayoutParams(btnParams);

            btnEditar.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditarPedidoActivity.class);
                intent.putExtra("pedido_id", pedido.getId());
                startActivity(intent);
            });
            layoutBotones.addView(btnEditar);
        }

        // Botón CANCELAR (solo si no está cancelado ni pagado)
        if (!estado.equals("cancelado") && !estado.equals("pagado")) {
            Button btnCancelar = new Button(getContext());
            btnCancelar.setText("❌ Cancelar");
            btnCancelar.setBackgroundColor(0xFFF44336);
            btnCancelar.setTextColor(0xFFFFFFFF);
            btnCancelar.setPadding(16, 8, 16, 8);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );
            btnParams.setMargins(8, 0, 8, 0);
            btnCancelar.setLayoutParams(btnParams);

            btnCancelar.setOnClickListener(v -> confirmarCancelar(pedido));
            layoutBotones.addView(btnCancelar);
        }

        // Botón ENTREGAR (solo si está pendiente)
        if (estado.equals("pendiente")) {
            Button btnEntregar = new Button(getContext());
            btnEntregar.setText("✅ Entregar");
            btnEntregar.setBackgroundColor(0xFF4CAF50);
            btnEntregar.setTextColor(0xFFFFFFFF);
            btnEntregar.setPadding(16, 8, 16, 8);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );
            btnParams.setMargins(8, 0, 0, 0);
            btnEntregar.setLayoutParams(btnParams);

            btnEntregar.setOnClickListener(v -> cambiarEstado(pedido, "entregado"));
            layoutBotones.addView(btnEntregar);
        }

        // Botón PAGAR (solo si está entregado) - aparece automáticamente
        if (estado.equals("entregado")) {
            Button btnPagar = new Button(getContext());
            btnPagar.setText("💰 Pagar");
            btnPagar.setBackgroundColor(0xFFFF9800);
            btnPagar.setTextColor(0xFFFFFFFF);
            btnPagar.setPadding(16, 8, 16, 8);

            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );
            btnParams.setMargins(8, 0, 0, 0);
            btnPagar.setLayoutParams(btnParams);

            btnPagar.setOnClickListener(v -> confirmarPagar(pedido));
            layoutBotones.addView(btnPagar);
        }

        card.addView(tvId);
        card.addView(tvMesa);
        card.addView(tvTotal);
        card.addView(tvEstado);
        if (layoutBotones.getChildCount() > 0) {
            card.addView(layoutBotones);
        }

        return card;
    }

    private String getEstadoTexto(String estado) {
        switch (estado) {
            case "pendiente": return "🟡 Pendiente";
            case "entregado": return "🔵 Entregado";
            case "pagado": return "🟢 Pagado";
            case "cancelado": return "🔴 Cancelado";
            default: return estado;
        }
    }

    private int getEstadoColor(String estado) {
        switch (estado) {
            case "pendiente": return 0xFFFF9800;
            case "entregado": return 0xFF2196F3;
            case "pagado": return 0xFF4CAF50;
            case "cancelado": return 0xFFF44336;
            default: return 0xFF666666;
        }
    }

    private void cambiarEstado(Pedido pedido, String nuevoEstado) {
        pedido.setEstado(nuevoEstado);
        db.pedidoDao().update(pedido);
        Toast.makeText(getContext(), "✅ Pedido #" + pedido.getId() + " " + getEstadoTexto(nuevoEstado), Toast.LENGTH_SHORT).show();
        cargarPedidos();
    }

    private void confirmarCancelar(Pedido pedido) {
        new AlertDialog.Builder(getContext())
                .setTitle("Cancelar Pedido")
                .setMessage("¿Estás seguro de cancelar el Pedido #" + pedido.getId() + "?")
                .setPositiveButton("Sí, cancelar", (dialog, which) -> {
                    cambiarEstado(pedido, "cancelado");
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void confirmarPagar(Pedido pedido) {
        new AlertDialog.Builder(getContext())
                .setTitle("Pagar Pedido")
                .setMessage("¿Estás seguro de registrar el pago del Pedido #" + pedido.getId() + "?\nTotal: $" + String.format("%.2f", pedido.getTotal()))
                .setPositiveButton("Sí, pagar", (dialog, which) -> {
                    cambiarEstado(pedido, "pagado");
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}