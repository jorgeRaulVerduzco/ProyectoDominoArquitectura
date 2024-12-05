/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L; // Versión de la clase para serialización
    private String nombre;
    private Avatar avatar;
    private String estado; //para cuando dicida abandonar partida osea si esta sale activo sino inactivo
    private int puntuacion;
    private List<Ficha> fichasJugador = new ArrayList<>();
    private Tablero tablero;

    /**
     * Constructor por defecto de la clase Jugador. Inicializa una lista vacía
     * de fichas para el jugador.
     * @param nombre
     * @param avatar
     */
    
    
    
    public Jugador(String nombre, Avatar avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
    }

    public Jugador(String nombre) {
        this.nombre = nombre;
    }

    public Jugador() {
        fichasJugador = new ArrayList<>();
    }

    /**
     * Constructor sobrecargado de la clase Jugador. Permite crear un jugador
     * con todos sus atributos definidos.
     *
     * @param nombre El nombre del jugador.
     * @param avatar El avatar del jugador.
     * @param estado El estado del jugador (activo o inactivo).
     * @param puntuacion La puntuación inicial del jugador.
     * @param fichasJugador La lista de fichas asignadas al jugador.
     */
    public Jugador(String nombre, Avatar avatar, String estado, int puntuacion, List<Ficha> fichasJugador) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.estado = estado;
        this.puntuacion = puntuacion;
        this.fichasJugador = fichasJugador;
    }

    /**
     * Método getter para obtener el nombre del jugador.
     *
     * @return El nombre del jugador.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método setter para modificar el nombre del jugador.
     *
     * @param nombre El nuevo nombre del jugador.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setTablero(Tablero tablero) {
    this.tablero = tablero;
}

public Tablero getTablero() {
    return this.tablero;
}

    /**
     * Método getter para obtener el avatar del jugador.
     *
     * @return El avatar asociado al jugador.
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * Método setter para modificar el avatar del jugador.
     *
     * @param avatar El nuevo avatar del jugador.
     */
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * Método getter para obtener el estado del jugador. El estado indica si el
     * jugador está activo o inactivo (ha abandonado la partida).
     *
     * @return El estado del jugador.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Método setter para modificar el estado del jugador.
     *
     * @param estado El nuevo estado del jugador (activo o inactivo).
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Método getter para obtener la puntuación del jugador.
     *
     * @return La puntuación actual del jugador.
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Método setter para modificar la puntuación del jugador.
     *
     * @param puntuacion La nueva puntuación del jugador.
     */
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    /**
     * Método getter para obtener la lista de fichas asignadas al jugador.
     *
     * @return La lista de fichas que tiene el jugador.
     */
    public List<Ficha> getFichasJugador() {
        return fichasJugador;
    }

    /**
     * Método setter para modificar la lista de fichas del jugador.
     *
     * @param fichasJugador La nueva lista de fichas para el jugador.
     */
    public void setFichasJugador(List<Ficha> fichasJugador) {
        this.fichasJugador = fichasJugador;
    }

    /**
     * Método sobreescrito para generar una representación en formato String del
     * jugador, que incluye su nombre, avatar, estado, puntuación y fichas.
     *
     * @return Una cadena con el formato "Jugador{nombre=nombre, avatar=avatar,
     * estado=estado, puntuacion=puntuacion, fichasJugador=lista_de_fichas}".
     */
    @Override
    public String toString() {
        return "Jugador{" + "nombre=" + nombre + ", avatar=" + avatar + ", estado=" + estado + ", puntuacion=" + puntuacion + ", fichasJugador=" + fichasJugador + '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Jugador jugador = (Jugador) obj;
        return nombre != null && nombre.equalsIgnoreCase(jugador.nombre); // Comparar solo por nombre
    }

    @Override
    public int hashCode() {
        return nombre != null ? nombre.toLowerCase().hashCode() : 0; // Generar hashCode basado en el nombre
    }
}