package com.example.restaurantapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "detalle_pedido")
public class DetallePedido {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pedido_id")
    private int pedidoId;

    @ColumnInfo(name = "producto_id")
    private int productoId;

    @ColumnInfo(name = "producto_nombre")
    private String productoNombre;

    @ColumnInfo(name = "cantidad")
    private int cantidad;

    @ColumnInfo(name = "precio_unitario")
    private double precioUnitario;

    @ColumnInfo(name = "subtotal")
    private double subtotal;

    @ColumnInfo(name = "notas")
    private String notas;

    // Constructor vacío
    public DetallePedido() {
    }

    // Constructor con parámetros
    public DetallePedido(int pedidoId, int productoId, String productoNombre, int cantidad, double precioUnitario, String notas) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.productoNombre = productoNombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
        this.notas = notas;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPedidoId() { return pedidoId; }
    public void setPedidoId(int pedidoId) { this.pedidoId = pedidoId; }

    public int getProductoId() { return productoId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}