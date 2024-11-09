/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class JugadorDTO {
     private String nombre;
    private AvatarDTO avatar;
    private String estado; //para cuando dicida abandonar partida osea si esta sale activo sino inactivo
    private int puntuacion;
    private List<FichaDTO> fichasJugador = new ArrayList<>();

    public JugadorDTO() {
        fichasJugador = new ArrayList<>();
    }

    public JugadorDTO(String nombre, AvatarDTO avatar, String estado, int puntuacion, List<FichaDTO> fichasJugador) {
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

    public AvatarDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarDTO avatar) {
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

    public List<FichaDTO> getFichasJugador() {
        return fichasJugador;
    }

    public void setFichasJugador(List<FichaDTO> fichasJugador) {
        this.fichasJugador = fichasJugador;
    }

    @Override
    public String toString() {
        return "Jugador{" + "nombre=" + nombre + ", avatar=" + avatar + ", estado=" + estado + ", puntuacion=" + puntuacion + ", fichasJugador=" + fichasJugador + '}';
    }
}
