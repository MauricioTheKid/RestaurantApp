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

    @Insert
    long insert(Producto producto);

    @Insert
    List<Long> insertAll(Producto... productos);

    @Update
    int update(Producto producto);

    @Delete
    int delete(Producto producto);

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    List<Producto> getAllProductos();

    @Query("SELECT * FROM productos WHERE id = :id")
    Producto getProductoById(int id);

    @Query("SELECT * FROM productos WHERE categoria = :categoria")
    List<Producto> getProductosByCategoria(String categoria);

    @Query("SELECT * FROM productos WHERE disponible = 1")
    List<Producto> getProductosDisponibles();

    @Query("SELECT * FROM productos WHERE nombre LIKE '%' || :busqueda || '%'")
    List<Producto> buscarProductos(String busqueda);

    @Query("SELECT * FROM productos WHERE precio BETWEEN :min AND :max")
    List<Producto> getProductosByPrecio(double min, double max);

    @Query("DELETE FROM productos")
    void deleteAll();
}