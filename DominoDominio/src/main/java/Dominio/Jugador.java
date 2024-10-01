/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class Jugador {

    private String nombre;
    private Avatar avatar;
    private String estado; //para cuando dicida abandonar partida osea si esta sale activo sino inactivo
    private int puntuacion;
    private List<Ficha> fichasJugador;

    public Jugador() {
        fichasJugador = new ArrayList<>();
    }

    public Jugador(String nombre, Avatar avatar, String estado, int puntuacion, List<Ficha> fichasJugador) {
        this.nombre = nombre;
        this.avatar = avatar;
        this.estado = estado;
        this.puntuacion = puntuacion;
        this.fichasJugador = fichasJugador;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<Ficha> getFichasJugador() {
        return fichasJugador;
    }

    public void setFichasJugador(List<Ficha> fichasJugador) {
        this.fichasJugador = fichasJugador;
    }

    @Override
    public String toString() {
        return "Jugador{" + "nombre=" + nombre + ", avatar=" + avatar + ", estado=" + estado + ", puntuacion=" + puntuacion + ", fichasJugador=" + fichasJugador + '}';
    }

}
