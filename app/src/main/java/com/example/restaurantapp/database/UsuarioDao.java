package com.example.restaurantapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.restaurantapp.models.Usuario;
import java.util.List;

@Dao
public interface UsuarioDao {

    // Insertar un usuario
    @Insert
    long insert(Usuario usuario);

    // Insertar varios usuarios
    @Insert
    List<Long> insertAll(Usuario... usuarios);

    // Actualizar un usuario
    @Update
    int update(Usuario usuario);

    // Eliminar un usuario
    @Delete
    int delete(Usuario usuario);

    // Obtener todos los usuarios
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    List<Usuario> getAllUsuarios();

    // Obtener usuario por ID
    @Query("SELECT * FROM usuarios WHERE id = :id")
    Usuario getUsuarioById(int id);

    // Obtener usuario por nombre
    @Query("SELECT * FROM usuarios WHERE nombre = :nombre")
    Usuario getUsuarioByNombre(String nombre);

    // Obtener usuario por email
    @Query("SELECT * FROM usuarios WHERE email = :email")
    Usuario getUsuarioByEmail(String email);

    // Validar login
    @Query("SELECT * FROM usuarios WHERE nombre = :nombre AND password = :password")
    Usuario login(String nombre, String password);

    // Obtener usuarios por rol
    @Query("SELECT * FROM usuarios WHERE rol = :rol")
    List<Usuario> getUsuariosByRol(String rol);

    // Verificar si existe usuario
    @Query("SELECT COUNT(*) > 0 FROM usuarios WHERE nombre = :nombre")
    boolean existeUsuario(String nombre);

    // Eliminar todos los usuarios
    @Query("DELETE FROM usuarios")
    void deleteAll();
}