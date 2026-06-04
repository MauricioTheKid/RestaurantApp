package com.example.restaurantapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.restaurantapp.models.PedidoDetalle;
import java.util.List;

@Dao
public interface PedidoDetalleDao {

    // Insertar un detalle de pedido
    @Insert
    long insert(PedidoDetalle detalle);

    // Insertar varios detalles
    @Insert
    List<Long> insertAll(PedidoDetalle... detalles);

    // Actualizar un detalle
    @Update
    int update(PedidoDetalle detalle);

    // Eliminar un detalle
    @Delete
    int delete(PedidoDetalle detalle);

    // Obtener todos los detalles
    @Query("SELECT * FROM pedido_detalles")
    List<PedidoDetalle> getAllDetalles();

    // Obtener detalles por pedido
    @Query("SELECT * FROM pedido_detalles WHERE pedido_id = :pedidoId")
    List<PedidoDetalle> getDetallesByPedido(int pedidoId);

    // Obtener detalle por ID
    @Query("SELECT * FROM pedido_detalles WHERE id = :id")
    PedidoDetalle getDetalleById(int id);

    // Eliminar detalles de un pedido
    @Query("DELETE FROM pedido_detalles WHERE pedido_id = :pedidoId")
    void deleteDetallesByPedido(int pedidoId);

    // Eliminar todos los detalles
    @Query("DELETE FROM pedido_detalles")
    void deleteAll();
}