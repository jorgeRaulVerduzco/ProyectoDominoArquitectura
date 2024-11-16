/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FachadaProvicional;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOs.AvatarDTO;
import DTOs.FichaDTO;
import DTOs.JugadorDTO;
import DTOs.PozoDTO;
import DTOs.TableroDTO;
import Dominio.Avatar;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Dominio.Tablero;
import Negocio.ServicioJugador;

/**
 *
 * @author INEGI
 */
public class FachadaServicioJugador {

    private ServicioJugador servicioJugador;
    private ConversorDTOaEntidad conversorDTOaEntidad;

    /**
     * Constructor de la clase FachadaServicioJugador. Inicializa el servicio de
     * jugador y el conversor de DTO a entidad.
     */
    public FachadaServicioJugador() {
        this.servicioJugador = new ServicioJugador();
        this.conversorDTOaEntidad = new ConversorDTOaEntidad();
    }

    /**
     * Permite a un jugador abandonar una partida.
     *
     * @param jugador
     * @param pozo
     */
    public void abandonarPartida(Jugador jugador, Pozo pozo) {
       
       servicioJugador.abandonarPartida(jugador, pozo);
    }

    /**
     * Verifica si un jugador puede colocar una ficha en el tablero.
     *
     * @param jugador
     * @param ficha
     * @param tablero
     * @return true si el jugador puede colocar la ficha en el tablero, de lo
     * contrario false.
     */
    public boolean puedeColocarFicha(Jugador jugador, Ficha ficha, Tablero tablero) {
       
        return servicioJugador.puedeColocarFicha(jugador, ficha, tablero);
    }

    /**
     * Configura el avatar de un jugador.
     *
     * @param jugador
     * @param avatar
     */
    public void configurarAvatar(Jugador jugador, Avatar avatar) {

        servicioJugador.configurarAvatar(jugador, avatar);
    }

    /**
     * Verifica el estado de un jugador en el sistema.
     *
     * @param jugador
     * @return true si el jugador está en un estado válido o activo, de lo
     * contrario false.
     */
    public boolean verificarEstadoJugador(Jugador jugador) {
        return servicioJugador.verificarEstadoJugador(jugador);
    }
}
