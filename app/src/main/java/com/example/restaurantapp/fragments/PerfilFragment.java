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

public class PerfilFragment extends Fragment {

    private String nombreUsuario;
    private TextView tvUsuario, tvEmail;

    public static PerfilFragment newInstance(String usuario) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString("usuario", usuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nombreUsuario = getArguments().getString("usuario");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        tvUsuario = view.findViewById(R.id.tvUsuario);
        tvEmail = view.findViewById(R.id.tvEmail);

        SharedPreferences preferences = requireContext().getSharedPreferences("RestaurantPrefs", 0);
        String email = preferences.getString("email", "usuario@ejemplo.com");

        tvUsuario.setText(nombreUsuario != null ? nombreUsuario : "Usuario");
        tvEmail.setText(email);

        return view;
    }
}