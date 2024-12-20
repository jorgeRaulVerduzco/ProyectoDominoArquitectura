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
public class SalaDTO {

    int cantJugadores;
    int numeroFichas;
    List<JugadorDTO> jugador = new ArrayList<>();
    String estado;
    PartidaDTO partida;

    /**
     * Constructor por defecto de la clase Sala. Inicializa los atributos de la
     * sala sin parámetros adicionales.
     */
    public SalaDTO() {
    }

    /**
     * Método getter para obtener la cantidad de jugadores en la sala.
     *
     * @return La cantidad de jugadores.
     */
    public int getCantJugadores() {
        return cantJugadores;
    }

    /**
     * Método setter para establecer la cantidad de jugadores en la sala.
     *
     * @param cantJugadores La cantidad de jugadores deseada.
     */
    public void setCantJugadores(int cantJugadores) {
        this.cantJugadores = cantJugadores;
    }

    /**
     * Método getter para obtener el número de fichas por jugador en la partida.
     *
     * @return El número de fichas por jugador.
     */
    public int getNumeroFichas() {
        return numeroFichas;
    }

    /**
     * Método setter para establecer el número de fichas por jugador en la
     * partida.
     *
     * @param numeroFichas El número de fichas deseado.
     */
    public void setNumeroFichas(int numeroFichas) {
        this.numeroFichas = numeroFichas;
    }

    /**
     * Método getter para obtener la lista de jugadores en la sala.
     *
     * @return Una lista de jugadores en la sala.
     */
    public List<JugadorDTO> getJugador() {
        return jugador;
    }

    /**
     * Método setter para establecer la lista de jugadores en la sala.
     *
     * @param jugador La lista de jugadores que participarán en la sala.
     */
    public void setJugador(List<JugadorDTO> jugador) {
        this.jugador = jugador;
    }

    /**
     * Método getter para obtener el estado de la sala.
     *
     * @return El estado actual de la sala.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Método setter para modificar el estado de la sala.
     *
     * @param estado El nuevo estado de la sala.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Método getter para obtener la partida asociada a la sala.
     *
     * @return La partida actual asociada a la sala.
     */
    public PartidaDTO getPartida() {
        return partida;
    }

    /**
     * Método setter para establecer la partida asociada a la sala.
     *
     * @param partida La partida que se jugará en la sala.
     */
    public void setPartida(PartidaDTO partida) {
        this.partida = partida;
    }

    /**
     * Método toString para generar una representación en cadena de la sala.
     *
     * @return Una cadena que describe la sala, incluyendo la cantidad de
     * jugadores, el número de fichas, el estado de la sala y la partida
     * asociada.
     */
    @Override
    public String toString() {
        return "Sala{" + "cantJugadores=" + cantJugadores + ", numeroFichas=" + numeroFichas + ", jugador=" + jugador + ", estado=" + estado + ", partida=" + partida + '}';
    }
}
