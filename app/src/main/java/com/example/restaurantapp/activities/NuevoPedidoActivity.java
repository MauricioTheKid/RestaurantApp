package com.example.restaurantapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.R;
import com.example.restaurantapp.adapters.ProductoSelectorAdapter;
import com.example.restaurantapp.database.AppDatabase;
import com.example.restaurantapp.models.DetallePedido;
import com.example.restaurantapp.models.ItemCarrito;
import com.example.restaurantapp.models.Pedido;
import com.example.restaurantapp.models.Producto;
import java.util.ArrayList;
import java.util.List;

public class NuevoPedidoActivity extends AppCompatActivity {

    private EditText etNumeroMesa, etNotas;
    private EditText etBuscarProducto;
    private ImageView ivLimpiarBusqueda;
    private RecyclerView rvProductos;
    private LinearLayout layoutCarrito;
    private TextView tvSubtotal, tvImpuesto, tvTotal, tvCarritoVacio;
    private Button btnConfirmarPedido;
    private LinearLayout layoutTotales;

    private AppDatabase db;
    private List<Producto> listaProductos;
    private List<Producto> listaProductosOriginal;
    private List<ItemCarrito> carrito;
    private ProductoSelectorAdapter productoAdapter;
    private SharedPreferences preferences;
    private int usuarioId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Forzar modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_pedido);

        etNumeroMesa = findViewById(R.id.etNumeroMesa);
        etNotas = findViewById(R.id.etNotas);
        etBuscarProducto = findViewById(R.id.etBuscarProducto);
        ivLimpiarBusqueda = findViewById(R.id.ivLimpiarBusqueda);
        rvProductos = findViewById(R.id.rvProductos);
        layoutCarrito = findViewById(R.id.layoutCarrito);
        tvCarritoVacio = findViewById(R.id.tvCarritoVacio);
        layoutTotales = findViewById(R.id.layoutTotales);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvImpuesto = findViewById(R.id.tvImpuesto);
        tvTotal = findViewById(R.id.tvTotal);
        btnConfirmarPedido = findViewById(R.id.btnConfirmarPedido);

        // FORZAR COLOR DE TEXTO NEGRO EN TODOS LOS EditText
        etNumeroMesa.setTextColor(0xFF000000);
        etNumeroMesa.setHintTextColor(0xFF888888);
        etNotas.setTextColor(0xFF000000);
        etNotas.setHintTextColor(0xFF888888);
        etBuscarProducto.setTextColor(0xFF000000);
        etBuscarProducto.setHintTextColor(0xFF888888);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Nuevo Pedido");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = AppDatabase.getInstance(this);
        carrito = new ArrayList<>();
        preferences = getSharedPreferences("RestaurantPrefs", MODE_PRIVATE);
        usuarioId = preferences.getInt("userId", 1);

        cargarProductos();

        rvProductos.setLayoutManager(new LinearLayoutManager(this));
        productoAdapter = new ProductoSelectorAdapter(listaProductos, this::agregarAlCarrito);
        rvProductos.setAdapter(productoAdapter);

        // Configurar búsqueda personalizada
        etBuscarProducto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarProductos(s.toString());
                if (s.toString().trim().isEmpty()) {
                    ivLimpiarBusqueda.setVisibility(View.GONE);
                } else {
                    ivLimpiarBusqueda.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivLimpiarBusqueda.setOnClickListener(v -> {
            etBuscarProducto.setText("");
            filtrarProductos("");
        });

        btnConfirmarPedido.setOnClickListener(v -> confirmarPedido());
    }

    private void cargarProductos() {
        listaProductosOriginal = db.productoDao().getAllProductos();
        if (listaProductosOriginal == null || listaProductosOriginal.isEmpty()) {
            listaProductosOriginal = new ArrayList<>();

            Producto p1 = new Producto();
            p1.setId(1);
            p1.setNombre("🍔 Hamburguesa");
            p1.setDescripcion("Hamburguesa con queso, lechuga y tomate");
            p1.setPrecio(8.99);
            listaProductosOriginal.add(p1);

            Producto p2 = new Producto();
            p2.setId(2);
            p2.setNombre("🍕 Pizza Pepperoni");
            p2.setDescripcion("Pizza pepperoni con queso mozzarella");
            p2.setPrecio(12.99);
            listaProductosOriginal.add(p2);

            Producto p3 = new Producto();
            p3.setId(3);
            p3.setNombre("🍟 Papas fritas");
            p3.setDescripcion("Papas crujientes");
            p3.setPrecio(3.50);
            listaProductosOriginal.add(p3);

            Producto p4 = new Producto();
            p4.setId(4);
            p4.setNombre("🥤 Refresco Cola");
            p4.setDescripcion("Refresco de cola bien frío");
            p4.setPrecio(2.50);
            listaProductosOriginal.add(p4);

            Producto p5 = new Producto();
            p5.setId(5);
            p5.setNombre("🌮 Tacos al Pastor");
            p5.setDescripcion("Tacos al pastor con piña y cilantro");
            p5.setPrecio(6.99);
            listaProductosOriginal.add(p5);

            Producto p6 = new Producto();
            p6.setId(6);
            p6.setNombre("🍰 Pastel Chocolate");
            p6.setDescripcion("Pastel de chocolate con crema");
            p6.setPrecio(4.99);
            listaProductosOriginal.add(p6);
        }
        listaProductos = new ArrayList<>(listaProductosOriginal);
    }

    private void filtrarProductos(String texto) {
        listaProductos.clear();
        if (texto == null || texto.trim().isEmpty()) {
            listaProductos.addAll(listaProductosOriginal);
        } else {
            String busqueda = texto.toLowerCase().trim();
            for (Producto p : listaProductosOriginal) {
                if (p.getNombre().toLowerCase().contains(busqueda)) {
                    listaProductos.add(p);
                }
            }
        }
        productoAdapter.actualizarLista(listaProductos);
    }

    private void agregarAlCarrito(Producto producto) {
        for (ItemCarrito item : carrito) {
            if (item.getProductoId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                actualizarCarrito();
                Toast.makeText(this, "✅ Agregado: " + producto.getNombre() + " (Cantidad: " + item.getCantidad() + ")", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        carrito.add(new ItemCarrito(producto.getId(), producto.getNombre(), 1, producto.getPrecio()));
        actualizarCarrito();
        Toast.makeText(this, "✅ Agregado: " + producto.getNombre(), Toast.LENGTH_SHORT).show();
    }

    private void actualizarCarrito() {
        layoutCarrito.removeAllViews();

        if (carrito.isEmpty()) {
            tvCarritoVacio.setVisibility(View.VISIBLE);
            layoutCarrito.setVisibility(View.GONE);
            layoutTotales.setVisibility(View.GONE);
            return;
        }

        tvCarritoVacio.setVisibility(View.GONE);
        layoutCarrito.setVisibility(View.VISIBLE);
        layoutTotales.setVisibility(View.VISIBLE);

        double subtotal = 0;

        for (ItemCarrito item : carrito) {
            subtotal += item.getSubtotal();

            View itemView = getLayoutInflater().inflate(R.layout.item_carrito, null);
            TextView tvNombre = itemView.findViewById(R.id.tvNombreCarrito);
            TextView tvCantidad = itemView.findViewById(R.id.tvCantidadCarrito);
            TextView tvPrecio = itemView.findViewById(R.id.tvPrecioCarrito);
            Button btnMas = itemView.findViewById(R.id.btnMas);
            Button btnMenos = itemView.findViewById(R.id.btnMenos);
            Button btnEliminar = itemView.findViewById(R.id.btnEliminar);

            tvNombre.setText(item.getNombre());
            tvCantidad.setText(String.valueOf(item.getCantidad()));
            tvPrecio.setText("$" + String.format("%.2f", item.getSubtotal()));

            btnMas.setOnClickListener(v -> {
                item.setCantidad(item.getCantidad() + 1);
                actualizarCarrito();
            });

            btnMenos.setOnClickListener(v -> {
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1);
                    actualizarCarrito();
                }
            });

            btnEliminar.setOnClickListener(v -> {
                carrito.remove(item);
                actualizarCarrito();
            });

            layoutCarrito.addView(itemView);
        }

        double impuesto = subtotal * 0.13;
        double total = subtotal + impuesto;

        tvSubtotal.setText("$" + String.format("%.2f", subtotal));
        tvImpuesto.setText("$" + String.format("%.2f", impuesto));
        tvTotal.setText("$" + String.format("%.2f", total));
    }

    private void confirmarPedido() {
        String numeroMesaStr = etNumeroMesa.getText().toString().trim();

        if (numeroMesaStr.isEmpty()) {
            etNumeroMesa.setError("Ingrese el número de mesa");
            etNumeroMesa.requestFocus();
            return;
        }

        if (carrito.isEmpty()) {
            Toast.makeText(this, "❌ Agregue productos al carrito", Toast.LENGTH_SHORT).show();
            return;
        }

        int numeroMesa = Integer.parseInt(numeroMesaStr);
        double subtotal = 0;

        for (ItemCarrito item : carrito) {
            subtotal += item.getSubtotal();
        }

        double impuesto = subtotal * 0.13;
        double total = subtotal + impuesto;

        String notas = etNotas.getText().toString().trim();

        Pedido pedido = new Pedido(numeroMesa, subtotal, impuesto, total, "pendiente", usuarioId, notas);
        long pedidoId = db.pedidoDao().insert(pedido);

        for (ItemCarrito item : carrito) {
            DetallePedido detalle = new DetallePedido(
                    (int) pedidoId,
                    item.getProductoId(),
                    item.getNombre(),
                    item.getCantidad(),
                    item.getPrecioUnitario(),
                    ""
            );
            db.detallePedidoDao().insert(detalle);
        }

        Toast.makeText(this, "✅ Pedido #" + pedidoId + " creado exitosamente", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}