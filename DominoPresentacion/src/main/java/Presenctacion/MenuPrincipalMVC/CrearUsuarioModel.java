/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import Dominio.Jugador;

/**
 *
 * @author Serva
 */
public class CrearUsuarioModel {

    private String nombre;
    private String avatar;

    // Constructor
    public CrearUsuarioModel(String nombre, String avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        if (avatar == null || avatar.trim().isEmpty()) {
            throw new IllegalArgumentException("El avatar no puede estar vacío.");
        }
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Usuario{"
                + "nombre='" + nombre + '\''
                + ", avatar='" + avatar + '\''
                + '}';
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jugador jugador = (Jugador) obj;
        return nombre != null && nombre.equalsIgnoreCase(jugador.getNombre()); // Comparar solo por nombre
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.toLowerCase().hashCode() : 0; // Generar hashCode basado en el nombre
    }
    
}
