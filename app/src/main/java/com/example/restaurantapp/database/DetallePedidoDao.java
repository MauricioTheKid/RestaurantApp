package com.example.restaurantapp.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.restaurantapp.models.DetallePedido;
import java.util.List;

@Dao
public interface DetallePedidoDao {

    @Insert
    long insert(DetallePedido detalle);

    @Insert
    List<Long> insertAll(List<DetallePedido> detalles);

    @Query("SELECT * FROM detalle_pedido WHERE pedido_id = :pedidoId")
    List<DetallePedido> getDetallesByPedido(int pedidoId);

    @Query("DELETE FROM detalle_pedido WHERE pedido_id = :pedidoId")
    void deleteDetallesByPedido(int pedidoId);
}