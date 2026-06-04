package com.example.restaurantapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.R;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {

    // Datos de los productos
    private final String[] nombres = {
            "🍔 Hamburguesa",
            "🍕 Pizza Pepperoni",
            "🌮 Tacos al Pastor",
            "🥗 Ensalada César",
            "🍜 Sopa de Pollo",
            "🥤 Refresco Cola",
            "🍰 Pastel de Chocolate",
            "🍟 Papas Fritas"
    };

    private final String[] descripciones = {
            "Hamburguesa con queso, lechuga y tomate",
            "Pizza pepperoni con queso mozzarella",
            "Tacos al pastor con piña y cilantro",
            "Ensalada César con pollo y aderezo",
            "Sopa de pollo con verduras",
            "Refresco de cola bien frío",
            "Pastel de chocolate con crema",
            "Papas fritas crujientes"
    };

    private final String[] precios = {
            "$8.99", "$12.99", "$6.99", "$7.99", "$5.99", "$2.50", "$4.99", "$3.50"
    };

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.tvNombre.setText(nombres[position]);
        holder.tvDescripcion.setText(descripciones[position]);
        holder.tvPrecio.setText(precios[position]);

        holder.btnAgregar.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "✅ Agregado: " + nombres[position], Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return nombres.length;
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;
        TextView tvDescripcion;
        TextView tvPrecio;
        Button btnAgregar;
        ImageView ivProducto;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnAgregar = itemView.findViewById(R.id.btnAgregar);
            ivProducto = itemView.findViewById(R.id.ivProducto);
        }
    }
}