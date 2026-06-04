package com.example.restaurantapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "RestaurantPrefs";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_ROLE = "userRole";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // Guardar sesión de usuario
    public void saveSession(int userId, String username, String email, String role) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    // Verificar si hay sesión activa
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Obtener ID del usuario logueado
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    // Obtener nombre del usuario logueado
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    // Obtener email del usuario logueado
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, "");
    }

    // Obtener rol del usuario logueado
    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "");
    }

    // Cerrar sesión
    public void logout() {
        editor.clear();
        editor.apply();
    }

    // Eliminar todos los datos
    public void clearAll() {
        editor.clear();
        editor.apply();
    }
}