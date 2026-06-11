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
import com.example.restaurantapp.models.DetallePedido;
import com.example.restaurantapp.models.Pedido;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VentasFragment extends Fragment {

    private LinearLayout layoutVentas;
    private AppDatabase db;
    private SimpleDateFormat sdfFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

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
        int cantidadPedidos = pedidosPagados.size();

        SimpleDateFormat sdfDia = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String hoy = sdfDia.format(new Date());

        for (Pedido pedido : pedidosPagados) {
            totalHoy += pedido.getTotal();
        }

        // Tarjeta de resumen
        LinearLayout resumenCard = crearCardResumen(totalHoy, cantidadPedidos);
        layoutVentas.addView(resumenCard);

        // Título de detalle de ventas
        TextView tituloDetalle = new TextView(getContext());
        tituloDetalle.setText("🧾 DETALLE DE VENTAS");
        tituloDetalle.setTextSize(16);
        tituloDetalle.setTextColor(0xFF333333);
        tituloDetalle.setTypeface(null, android.graphics.Typeface.BOLD);
        tituloDetalle.setPadding(0, 20, 0, 10);
        layoutVentas.addView(tituloDetalle);

        if (pedidosPagados.isEmpty()) {
            TextView tvVacio = new TextView(getContext());
            tvVacio.setText("💰 No hay ventas registradas");
            tvVacio.setTextSize(14);
            tvVacio.setTextColor(0xFF999999);
            tvVacio.setPadding(20, 20, 20, 20);
            layoutVentas.addView(tvVacio);
        } else {
            for (Pedido pedido : pedidosPagados) {
                LinearLayout card = crearCardVentaConDetalle(pedido);
                layoutVentas.addView(card);
            }
        }
    }

    private LinearLayout crearCardResumen(double totalHoy, int cantidad) {
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

    private LinearLayout crearCardVentaConDetalle(Pedido pedido) {
        // Card principal
        LinearLayout card = new LinearLayout(getContext());
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(15, 15, 15, 15);
        card.setBackgroundColor(0xFFFFFFFF);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 12);
        card.setLayoutParams(params);

        // Sombra (elevación simulada)
        card.setElevation(4);
        card.setBackgroundResource(android.R.drawable.editbox_background);

        // Encabezado del pedido
        TextView tvEncabezado = new TextView(getContext());
        tvEncabezado.setText("🆔 Pedido #" + pedido.getId() + " - Mesa " + pedido.getNumeroMesa());
        tvEncabezado.setTextSize(15);
        tvEncabezado.setTextColor(0xFF333333);
        tvEncabezado.setTypeface(null, android.graphics.Typeface.BOLD);
        tvEncabezado.setPadding(0, 0, 0, 8);
        card.addView(tvEncabezado);

        // Fecha y hora
        TextView tvFecha = new TextView(getContext());
        tvFecha.setText("📅 " + sdfFecha.format(pedido.getFecha()));
        tvFecha.setTextSize(12);
        tvFecha.setTextColor(0xFF888888);
        tvFecha.setPadding(0, 0, 0, 12);
        card.addView(tvFecha);

        // Obtener detalles del pedido
        List<DetallePedido> detalles = db.detallePedidoDao().getDetallesByPedido(pedido.getId());

        // Contenedor de productos
        LinearLayout layoutProductos = new LinearLayout(getContext());
        layoutProductos.setOrientation(LinearLayout.VERTICAL);
        layoutProductos.setPadding(10, 10, 10, 10);
        layoutProductos.setBackgroundColor(0xFFF5F5F5);
        layoutProductos.setElevation(2);

        for (DetallePedido detalle : detalles) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(0, 5, 0, 5);

            TextView tvProducto = new TextView(getContext());
            tvProducto.setText("🍽️ " + detalle.getProductoNombre() + " x" + detalle.getCantidad());
            tvProducto.setTextSize(14);
            tvProducto.setTextColor(0xFF333333);

            TextView tvSubtotal = new TextView(getContext());
            tvSubtotal.setText("$" + String.format("%.2f", detalle.getSubtotal()));
            tvSubtotal.setTextSize(14);
            tvSubtotal.setTextColor(0xFF4CAF50);
            tvSubtotal.setTypeface(null, android.graphics.Typeface.BOLD);

            LinearLayout.LayoutParams paramsProducto = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );
            tvProducto.setLayoutParams(paramsProducto);

            row.addView(tvProducto);
            row.addView(tvSubtotal);
            layoutProductos.addView(row);
        }

        card.addView(layoutProductos);

        // Total del pedido
        TextView tvTotal = new TextView(getContext());
        tvTotal.setText("💰 TOTAL: $" + String.format("%.2f", pedido.getTotal()));
        tvTotal.setTextSize(15);
        tvTotal.setTextColor(0xFF4CAF50);
        tvTotal.setTypeface(null, android.graphics.Typeface.BOLD);
        tvTotal.setPadding(0, 12, 0, 0);
        card.addView(tvTotal);

        return card;
    }
}