package com.example.restaurantapp.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mesas")
public class Mesa {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "numero")
    private int numero;

    @ColumnInfo(name = "capacidad")
    private int capacidad;

    @ColumnInfo(name = "estado")
    private String estado; // libre, ocupada, reservada

    @ColumnInfo(name = "qr_code")
    private String qrCode;

    // Constructor vacío (requerido por Room)
    public Mesa() {
    }

    // Constructor con parámetros
    public Mesa(int id, int numero, int capacidad, String estado, String qrCode) {
        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
        this.qrCode = qrCode;
    }

    // Constructor sin id
    public Mesa(int numero, int capacidad, String estado, String qrCode) {
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
        this.qrCode = qrCode;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    public String toString() {
        return "Mesa{" +
                "id=" + id +
                ", numero=" + numero +
                ", capacidad=" + capacidad +
                ", estado='" + estado + '\'' +
                '}';
    }
}