package com.example.restaurantapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.restaurantapp.models.Producto;
import java.util.List;

@Dao
public interface ProductoDao {

    // Insertar un producto
    @Insert
    long insert(Producto producto);

    // Insertar varios productos
    @Insert
    List<Long> insertAll(Producto... productos);

    // Actualizar un producto
    @Update
    int update(Producto producto);

    // Eliminar un producto
    @Delete
    int delete(Producto producto);

    // Obtener todos los productos
    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    List<Producto> getAllProductos();

    // Obtener producto por ID
    @Query("SELECT * FROM productos WHERE id = :id")
    Producto getProductoById(int id);

    // Obtener productos por categoría
    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    List<Producto> getProductosByCategoria(String categoria);

    // Obtener productos disponibles
    @Query("SELECT * FROM productos WHERE disponible = 1")
    List<Producto> getProductosDisponibles();

    // Buscar productos por nombre
    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :busqueda || '%'")
    List<Producto> buscarProductos(String busqueda);

    // Obtener productos por rango de precio
    @Query("SELECT * FROM productos WHERE precio BETWEEN :min AND :max")
    List<Producto> getProductosByPrecio(double min, double max);

    // Eliminar todos los productos
    @Query("DELETE FROM productos")
    void deleteAll();
}