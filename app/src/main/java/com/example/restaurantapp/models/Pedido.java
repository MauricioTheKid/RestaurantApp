package com.example.restaurantapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.List;

@Entity(tableName = "pedidos")
public class Pedido {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "numero_mesa")
    private int numeroMesa;

    @ColumnInfo(name = "fecha")
    private Date fecha;

    @ColumnInfo(name = "subtotal")
    private double subtotal;

    @ColumnInfo(name = "impuesto")
    private double impuesto;

    @ColumnInfo(name = "total")
    private double total;

    @ColumnInfo(name = "estado")
    private String estado; // pendiente, preparacion, listo, entregado, pagado, cancelado

    @ColumnInfo(name = "usuario_id")
    private int usuarioId;

    @ColumnInfo(name = "notas")
    private String notas;

    @Ignore
    private List<DetallePedido> detalles;

    // Constructor vacío (requerido por Room)
    public Pedido() {
    }

    // Constructor con parámetros
    public Pedido(int numeroMesa, double subtotal, double impuesto, double total, String estado, int usuarioId, String notas) {
        this.numeroMesa = numeroMesa;
        this.fecha = new Date();
        this.subtotal = subtotal;
        this.impuesto = impuesto;
        this.total = total;
        this.estado = estado;
        this.usuarioId = usuarioId;
        this.notas = notas;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(int numeroMesa) { this.numeroMesa = numeroMesa; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getImpuesto() { return impuesto; }
    public void setImpuesto(double impuesto) { this.impuesto = impuesto; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }

    public List<DetallePedido> getDetalles() { return detalles; }
    public void setDetalles(List<DetallePedido> detalles) { this.detalles = detalles; }

    public void calcularTotal() {
        this.impuesto = this.subtotal * 0.13; // 13% impuesto
        this.total = this.subtotal + this.impuesto;
    }
}