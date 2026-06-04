package com.example.restaurantapp.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.restaurantapp.R;

public class InicioFragment extends Fragment {

    private TextView tvUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        tvUsuario = view.findViewById(R.id.tvUsuario);

        SharedPreferences preferences = requireContext().getSharedPreferences("RestaurantPrefs", 0);
        String usuario = preferences.getString("loggedUser", "Usuario");
        tvUsuario.setText(usuario);

        return view;
    }
}