package com.example.restaurantapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.restaurantapp.R;
import com.example.restaurantapp.adapters.ProductoAdminAdapter;
import com.example.restaurantapp.database.AppDatabase;
import com.example.restaurantapp.models.Producto;
import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment implements ProductoAdminAdapter.OnProductoChangedListener {

    private RecyclerView rvProductos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText etBuscarProducto;
    private ImageView ivLimpiarBusqueda;
    private Button btnAgregarProducto;
    private ProductoAdminAdapter adapter;
    private AppDatabase db;
    private List<Producto> listaOriginal;
    private List<Producto> listaFiltrada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        rvProductos = view.findViewById(R.id.rvProductos);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        etBuscarProducto = view.findViewById(R.id.etBuscarProducto);
        ivLimpiarBusqueda = view.findViewById(R.id.ivLimpiarBusqueda);
        btnAgregarProducto = view.findViewById(R.id.btnAgregarProducto);

        // FORZAR COLOR DE TEXTO NEGRO EN EL EditText DE BÚSQUEDA
        etBuscarProducto.setTextColor(0xFF000000);
        etBuscarProducto.setHintTextColor(0xFF888888);

        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(requireContext());

        cargarProductos();

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

        swipeRefreshLayout.setOnRefreshListener(() -> {
            cargarProductos();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "✅ Menú actualizado", Toast.LENGTH_SHORT).show();
            }, 1000);
        });

        btnAgregarProducto.setOnClickListener(v -> mostrarDialogoAgregar());

        return view;
    }

    private void cargarProductos() {
        listaOriginal = db.productoDao().getAllProductos();
        if (listaOriginal == null || listaOriginal.isEmpty()) {
            insertarProductosPorDefecto();
            listaOriginal = db.productoDao().getAllProductos();
        }
        listaFiltrada = new ArrayList<>(listaOriginal);
        adapter = new ProductoAdminAdapter(listaFiltrada, db, this, getContext());
        rvProductos.setAdapter(adapter);
    }

    private void insertarProductosPorDefecto() {
        String[][] productos = {
                {"🍔 Hamburguesa", "Hamburguesa con queso, lechuga y tomate", "8.99"},
                {"🍕 Pizza Pepperoni", "Pizza pepperoni con queso mozzarella", "12.99"},
                {"🍟 Papas fritas", "Papas crujientes", "3.50"},
                {"🥤 Refresco Cola", "Refresco de cola bien frío", "2.50"},
                {"🌮 Tacos al Pastor", "Tacos al pastor con piña y cilantro", "6.99"},
                {"🍰 Pastel Chocolate", "Pastel de chocolate con crema", "4.99"},
                {"🥗 Ensalada César", "Ensalada César con pollo y aderezo", "7.99"},
                {"🍜 Sopa de Pollo", "Sopa de pollo con verduras", "5.99"}
        };

        for (String[] p : productos) {
            Producto producto = new Producto();
            producto.setNombre(p[0]);
            producto.setDescripcion(p[1]);
            producto.setPrecio(Double.parseDouble(p[2]));
            producto.setCategoria("General");
            producto.setDisponible(true);
            db.productoDao().insert(producto);
        }
    }

    private void filtrarProductos(String texto) {
        listaFiltrada.clear();
        if (texto == null || texto.trim().isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            String busqueda = texto.toLowerCase().trim();
            for (Producto producto : listaOriginal) {
                if (producto.getNombre().toLowerCase().contains(busqueda)) {
                    listaFiltrada.add(producto);
                }
            }
        }
        adapter.actualizarLista(listaFiltrada);
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_producto, null);

        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        EditText etPrecio = view.findViewById(R.id.etPrecio);

        builder.setTitle("➕ Agregar Producto")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    String precioStr = etPrecio.getText().toString().trim();

                    if (nombre.isEmpty()) {
                        Toast.makeText(getContext(), "Ingrese nombre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (precioStr.isEmpty()) {
                        Toast.makeText(getContext(), "Ingrese precio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double precio = Double.parseDouble(precioStr);
                    Producto producto = new Producto();
                    producto.setNombre(nombre);
                    producto.setDescripcion(descripcion);
                    producto.setPrecio(precio);
                    producto.setCategoria("General");
                    producto.setDisponible(true);

                    db.productoDao().insert(producto);
                    Toast.makeText(getContext(), "✅ Producto agregado", Toast.LENGTH_SHORT).show();
                    cargarProductos();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onProductoChanged() {
        cargarProductos();
    }
}