package com.example.restaurantapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.restaurantapp.R;
import com.example.restaurantapp.adapters.ProductoAdapter;

public class MenuFragment extends Fragment {

    private RecyclerView rvProductos;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProductoAdapter productoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        rvProductos = view.findViewById(R.id.rvProductos);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        rvProductos.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarProductos();

        // Configurar swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Recargar productos
            cargarProductos();

            // Ocultar el indicador de carga después de 1 segundo
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getContext(), "✅ Menú actualizado", Toast.LENGTH_SHORT).show();
            }, 1000);
        });

        return view;
    }

    private void cargarProductos() {
        productoAdapter = new ProductoAdapter();
        rvProductos.setAdapter(productoAdapter);
    }
}