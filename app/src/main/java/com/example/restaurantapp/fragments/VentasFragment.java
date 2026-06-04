package com.example.restaurantapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.restaurantapp.R;
import com.example.restaurantapp.database.AppDatabase;
import com.example.restaurantapp.models.Pedido;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasFragment extends Fragment {

    private LinearLayout layoutVentas;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ventas, container, false);
        layoutVentas = view.findViewById(R.id.layoutVentas);
        db = AppDatabase.getInstance(requireContext());

        cargarVentas();

        return view;
    }

    private void cargarVentas() {
        layoutVentas.removeAllViews();

        // Obtener solo pedidos pagados
        List<Pedido> pedidosPagados = db.pedidoDao().getPedidosByEstado("pagado");

        // Calcular totales
        double totalHoy = 0;
        double totalSemana = 0;
        double totalMes = 0;
        int cantidadPedidos = pedidosPagados.size();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String hoy = sdf.format(new Date());

        for (Pedido pedido : pedidosPagados) {
            totalHoy += pedido.getTotal();
            totalSemana += pedido.getTotal();
            totalMes += pedido.getTotal();
        }

        // Tarjeta de resumen
        LinearLayout resumenCard = crearCardResumen(totalHoy, totalSemana, totalMes, cantidadPedidos);
        layoutVentas.addView(resumenCard);

        // Título de ventas recientes
        TextView tituloRecientes = new TextView(getContext());
        tituloRecientes.setText("📋 Ventas Recientes");
        tituloRecientes.setTextSize(18);
        tituloRecientes.setTextColor(0xFF333333);
        tituloRecientes.setTypeface(null, android.graphics.Typeface.BOLD);
        tituloRecientes.setPadding(0, 20, 0, 10);
        layoutVentas.addView(tituloRecientes);

        if (pedidosPagados.isEmpty()) {
            TextView tvVacio = new TextView(getContext());
            tvVacio.setText("💰 No hay ventas registradas");
            tvVacio.setTextSize(14);
            tvVacio.setTextColor(0xFF999999);
            tvVacio.setPadding(20, 20, 20, 20);
            layoutVentas.addView(tvVacio);
        } else {
            for (Pedido pedido : pedidosPagados) {
                LinearLayout card = crearCardVenta(pedido);
                layoutVentas.addView(card);
            }
        }
    }

    private LinearLayout crearCardResumen(double totalHoy, double totalSemana, double totalMes, int cantidad) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 20, 20, 20);
        card.setBackgroundColor(0xFFFFF3E0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 20);
        card.setLayoutParams(params);

        TextView tvTitulo = new TextView(getContext());
        tvTitulo.setText("📈 RESUMEN DE VENTAS");
        tvTitulo.setTextSize(16);
        tvTitulo.setTextColor(0xFFE65100);
        tvTitulo.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTitulo.setPadding(0, 0, 0, 15);

        TextView tvVentasHoy = new TextView(getContext());
        tvVentasHoy.setText("💰 Ventas hoy: $" + String.format("%.2f", totalHoy));
        tvVentasHoy.setTextSize(15);
        tvVentasHoy.setTextColor(0xFF333333);
        tvVentasHoy.setPadding(0, 5, 0, 5);

        TextView tvTotalPedidos = new TextView(getContext());
        tvTotalPedidos.setText("📋 Total pedidos pagados: " + cantidad);
        tvTotalPedidos.setTextSize(15);
        tvTotalPedidos.setTextColor(0xFF333333);
        tvTotalPedidos.setPadding(0, 5, 0, 0);

        card.addView(tvTitulo);
        card.addView(tvVentasHoy);
        card.addView(tvTotalPedidos);

        return card;
    }

    private LinearLayout crearCardVenta(Pedido pedido) {
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(15, 12, 15, 12);
        card.setBackgroundColor(0xFFFFFFFF);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 8);
        card.setLayoutParams(params);

        TextView tvId = new TextView(getContext());
        tvId.setText("🆔 Pedido #" + pedido.getId() + " - Mesa " + pedido.getNumeroMesa());
        tvId.setTextSize(14);
        tvId.setTextColor(0xFF333333);
        tvId.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvTotal = new TextView(getContext());
        tvTotal.setText("💰 $" + String.format("%.2f", pedido.getTotal()));
        tvTotal.setTextSize(14);
        tvTotal.setTextColor(0xFF4CAF50);

        card.addView(tvId);
        card.addView(tvTotal);

        return card;
    }
}