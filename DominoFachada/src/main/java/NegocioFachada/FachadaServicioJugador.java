/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NegocioFachada;

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
     * @param jugadorDTO el DTO del jugador que abandona la partida.
     * @param pozoDTO el DTO del pozo de fichas relacionado con la partida.
     */
    public void abandonarPartida(JugadorDTO jugadorDTO, PozoDTO pozoDTO) {
        Jugador jugador = conversorDTOaEntidad.convertirJugador(jugadorDTO);
        Pozo pozo = conversorDTOaEntidad.convertirPozo(pozoDTO);
        servicioJugador.abandonarPartida(jugador, pozo);
    }

    /**
     * Verifica si un jugador puede colocar una ficha en el tablero.
     *
     * @param jugadorDTO el DTO del jugador que desea colocar la ficha.
     * @param fichaDTO el DTO de la ficha que se intenta colocar.
     * @param tableroDTO el DTO del tablero donde se quiere colocar la ficha.
     * @return true si el jugador puede colocar la ficha en el tablero, de lo
     * contrario false.
     */
    public boolean puedeColocarFicha(JugadorDTO jugadorDTO, FichaDTO fichaDTO, TableroDTO tableroDTO) {
        Jugador jugador = conversorDTOaEntidad.convertirJugador(jugadorDTO);
        Ficha ficha = conversorDTOaEntidad.convertirFicha(fichaDTO);
        Tablero tablero = conversorDTOaEntidad.convertirTablero(tableroDTO);
        return servicioJugador.puedeColocarFicha(jugador, ficha, tablero);
    }

    /**
     * Configura el avatar de un jugador.
     *
     * @param jugadorDTO el DTO del jugador para el cual se configura el avatar.
     * @param avatarDTO el DTO del avatar que se asignará al jugador.
     */
    public void configurarAvatar(JugadorDTO jugadorDTO, AvatarDTO avatarDTO) {
        Jugador jugador = conversorDTOaEntidad.convertirJugador(jugadorDTO);
        Avatar avatar = conversorDTOaEntidad.convertirAvatar(avatarDTO);
        servicioJugador.configurarAvatar(jugador, avatar);
    }

    /**
     * Verifica el estado de un jugador en el sistema.
     *
     * @param jugadorDTO el DTO del jugador cuyo estado se desea verificar.
     * @return true si el jugador está en un estado válido o activo, de lo
     * contrario false.
     */
    public boolean verificarEstadoJugador(JugadorDTO jugadorDTO) {
        Jugador jugador = conversorDTOaEntidad.convertirJugador(jugadorDTO);
        return servicioJugador.verificarEstadoJugador(jugador);
    }
}
