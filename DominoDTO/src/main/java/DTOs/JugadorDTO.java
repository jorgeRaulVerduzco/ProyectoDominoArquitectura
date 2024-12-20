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

    /**
     * Constructor por defecto de la clase Jugador. Inicializa una lista vacía
     * de fichas para el jugador.
     */
    public JugadorDTO() {
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
    public JugadorDTO(String nombre, AvatarDTO avatar, String estado, int puntuacion, List<FichaDTO> fichasJugador) {
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

    /**
     * Método getter para obtener el avatar del jugador.
     *
     * @return El avatar asociado al jugador.
     */
    public AvatarDTO getAvatar() {
        return avatar;
    }

    /**
     * Método setter para modificar el avatar del jugador.
     *
     * @param avatar El nuevo avatar del jugador.
     */
    public void setAvatar(AvatarDTO avatar) {
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
    public List<FichaDTO> getFichasJugador() {
        return fichasJugador;
    }

    /**
     * Método setter para modificar la lista de fichas del jugador.
     *
     * @param fichasJugador La nueva lista de fichas para el jugador.
     */
    public void setFichasJugador(List<FichaDTO> fichasJugador) {
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
}
