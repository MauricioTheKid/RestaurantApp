package com.example.restaurantapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pedido_detalles")
public class PedidoDetalle {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "pedido_id")
    private int pedidoId;

    @ColumnInfo(name = "producto_id")
    private int productoId;

    @ColumnInfo(name = "cantidad")
    private int cantidad;

    @ColumnInfo(name = "precio_unitario")
    private double precioUnitario;

    @ColumnInfo(name = "subtotal")
    private double subtotal;

    @ColumnInfo(name = "notas")
    private String notas;

    // Constructor vacío (requerido por Room)
    public PedidoDetalle() {
    }

    // Constructor con parámetros
    public PedidoDetalle(int id, int pedidoId, int productoId, int cantidad, double precioUnitario, double subtotal, String notas) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = subtotal;
        this.notas = notas;
    }

    // Constructor sin id
    public PedidoDetalle(int pedidoId, int productoId, int cantidad, double precioUnitario, String notas) {
        this.pedidoId = pedidoId;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
        this.notas = notas;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
        this.subtotal = this.cantidad * this.precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    @Override
    public String toString() {
        return "PedidoDetalle{" +
                "productoId=" + productoId +
                ", cantidad=" + cantidad +
                ", subtotal=" + subtotal +
                '}';
    }
}