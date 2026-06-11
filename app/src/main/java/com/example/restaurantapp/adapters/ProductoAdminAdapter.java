package com.example.restaurantapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.restaurantapp.R;
import com.example.restaurantapp.database.AppDatabase;
import com.example.restaurantapp.models.Producto;
import java.util.List;

public class ProductoAdminAdapter extends RecyclerView.Adapter<ProductoAdminAdapter.ProductoViewHolder> {

    private List<Producto> listaProductos;
    private AppDatabase db;
    private OnProductoChangedListener listener;
    private Context context;

    public interface OnProductoChangedListener {
        void onProductoChanged();
    }

    public ProductoAdminAdapter(List<Producto> listaProductos, AppDatabase db, OnProductoChangedListener listener, Context context) {
        this.listaProductos = listaProductos;
        this.db = db;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto_admin, parent, false);
        return new ProductoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Producto producto = listaProductos.get(position);
        holder.tvNombre.setText(producto.getNombre());
        holder.tvDescripcion.setText(producto.getDescripcion());
        holder.tvPrecio.setText("$" + String.format("%.2f", producto.getPrecio()));

        holder.btnEditar.setOnClickListener(v -> mostrarDialogoEditar(producto));
        holder.btnEliminar.setOnClickListener(v -> confirmarEliminar(producto));
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public void actualizarLista(List<Producto> nuevaLista) {
        this.listaProductos = nuevaLista;
        notifyDataSetChanged();
    }

    private void mostrarDialogoEditar(Producto producto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_producto, null);

        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        EditText etPrecio = view.findViewById(R.id.etPrecio);

        etNombre.setText(producto.getNombre());
        etDescripcion.setText(producto.getDescripcion());
        etPrecio.setText(String.valueOf(producto.getPrecio()));

        builder.setTitle("✏️ Editar Producto")
                .setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    String descripcion = etDescripcion.getText().toString().trim();
                    String precioStr = etPrecio.getText().toString().trim();

                    if (nombre.isEmpty()) {
                        Toast.makeText(context, "Ingrese nombre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (precioStr.isEmpty()) {
                        Toast.makeText(context, "Ingrese precio", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double precio = Double.parseDouble(precioStr);
                    producto.setNombre(nombre);
                    producto.setDescripcion(descripcion);
                    producto.setPrecio(precio);

                    db.productoDao().update(producto);
                    Toast.makeText(context, "✅ Producto actualizado", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onProductoChanged();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void confirmarEliminar(Producto producto) {
        new AlertDialog.Builder(context)
                .setTitle("🗑️ Eliminar Producto")
                .setMessage("¿Eliminar " + producto.getNombre() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    db.productoDao().delete(producto);
                    Toast.makeText(context, "✅ Producto eliminado", Toast.LENGTH_SHORT).show();
                    if (listener != null) listener.onProductoChanged();
                })
                .setNegativeButton("No", null)
                .show();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion, tvPrecio;
        Button btnEditar, btnEliminar;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}