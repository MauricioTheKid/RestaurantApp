package com.example.restaurantapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.restaurantapp.models.Pedido;
import java.util.Date;
import java.util.List;

@Dao
public interface PedidoDao {

    // Insertar un pedido
    @Insert
    long insert(Pedido pedido);

    // Actualizar un pedido
    @Update
    int update(Pedido pedido);

    // Eliminar un pedido
    @Delete
    int delete(Pedido pedido);

    // Obtener todos los pedidos
    @Query("SELECT * FROM pedidos ORDER BY id DESC")
    List<Pedido> getAllPedidos();

    // Obtener pedido por ID
    @Query("SELECT * FROM pedidos WHERE id = :id")
    Pedido getPedidoById(int id);

    // Obtener pedidos por estado
    @Query("SELECT * FROM pedidos WHERE estado = :estado ORDER BY id DESC")
    List<Pedido> getPedidosByEstado(String estado);

    // Obtener pedidos por usuario
    @Query("SELECT * FROM pedidos WHERE usuario_id = :usuarioId ORDER BY id DESC")
    List<Pedido> getPedidosByUsuario(int usuarioId);

    // Obtener pedidos por mesa
    @Query("SELECT * FROM pedidos WHERE numero_mesa = :mesaId ORDER BY id DESC")
    List<Pedido> getPedidosByMesa(int mesaId);

    // Obtener pedidos por fecha
    @Query("SELECT * FROM pedidos WHERE fecha BETWEEN :inicio AND :fin ORDER BY fecha DESC")
    List<Pedido> getPedidosByFecha(Date inicio, Date fin);

    // Obtener pedidos del día actual
    @Query("SELECT * FROM pedidos WHERE date(fecha) = date('now') ORDER BY id DESC")
    List<Pedido> getPedidosDelDia();

    // Obtener total de ventas del día (solo pedidos pagados)
    @Query("SELECT SUM(total) FROM pedidos WHERE estado = 'pagado' AND date(fecha) = date('now')")
    Double getTotalVentasDelDia();

    // Obtener cantidad de pedidos del día
    @Query("SELECT COUNT(*) FROM pedidos WHERE date(fecha) = date('now')")
    int getCantidadPedidosDelDia();

    // Obtener total de ventas por estado
    @Query("SELECT SUM(total) FROM pedidos WHERE estado = :estado")
    Double getTotalVentasByEstado(String estado);

    // Actualizar estado del pedido
    @Query("UPDATE pedidos SET estado = :estado WHERE id = :id")
    void actualizarEstado(int id, String estado);

    // Eliminar todos los pedidos
    @Query("DELETE FROM pedidos")
    void deleteAll();
}