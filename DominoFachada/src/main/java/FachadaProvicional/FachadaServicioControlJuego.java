/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FachadaProvicional;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOaEntidad.ConversorEntidadADTO;
import DTOs.FichaDTO;
import DTOs.JugadorDTO;
import DTOs.PartidaDTO;
import DTOs.PozoDTO;
import DTOs.SalaDTO;
import DTOs.TableroDTO;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Pozo;
import Dominio.Sala;
import Dominio.Tablero;
import Negocio.ServicioControlJuego;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class FachadaServicioControlJuego {

    public ServicioControlJuego servicioControlJuego;
    private ConversorDTOaEntidad conversorAEntidad;
    private ConversorEntidadADTO conversorADTO;

    /**
     * Constructor de la clase FachadaServicioControlJuego. Inicializa los
     * objetos de servicio y conversores para convertir entre entidades y DTOs.
     */
    public FachadaServicioControlJuego() {
        this.servicioControlJuego = new ServicioControlJuego();
        this.conversorAEntidad = new ConversorDTOaEntidad();
        this.conversorADTO = new ConversorEntidadADTO();
    }


    /**
     * Agrega un jugador a una sala de juego.
     *
     * @param sala
     * @param jugador
     * @return true si el jugador fue agregado exitosamente; false si no se pudo
     * agregar.
     */
    public boolean agregarJugador(Sala sala, Jugador jugador) {
      
        boolean resultado = servicioControlJuego.agregarJugador(sala, jugador);
        return resultado;
    }

    /**
     * Inicia una partida en una sala de juego.
     *
     * @param sala
     */
    public void iniciarPartida(Sala sala) {
      
        servicioControlJuego.iniciarPartida(sala);
    }

    /**
     * Obtiene la lista de salas de juego disponibles.
     *
     * @return una lista de objetos SalaDTO con la información de las salas
     * disponibles.
     */
    public List<Sala> getSalasDisponibles() {
     return servicioControlJuego.getSalasDisponibles();
    }

    public boolean abandonarSala(Sala sala, Jugador jugador) {
  
        boolean resultado = servicioControlJuego.abandonarSala(sala, jugador);
        return resultado;
    }

    /**
     * Determina el lado de una ficha en el tablero.
     *
     * @param ficha
     * @param tablero
     * @return el lado de la ficha en el tablero (por ejemplo, "IZQUIERDA" o
     * "DERECHA").
     */
    public String determinarLado(Ficha ficha, Tablero tablero) {
      
        return servicioControlJuego.determinarLado(ficha, tablero);
    }

    /**
     * Obtiene el jugador actual en una partida.
     *
     * @param partida
     * @return el DTO del jugador actual en la partida.
     */
    public Jugador obtenerJugadorActual(Partida partida) {
        return servicioControlJuego.obtenerJugadorActual(partida);

    }

    /**
     * Verifica el ganador de una partida.
     *
     * @param partida
     */
    public void verificarGanador(Partida partida) {
     
        servicioControlJuego.verificarGanador(partida);
    }

    /**
     * Establece el orden de turnos de los jugadores en una partida.
     *
     * @param jugadorInicial
     * @param jugadores
     */
    public void establecerOrdenDeTurnos(Jugador jugadorInicial, List<Jugador> jugadores) {

        servicioControlJuego.establecerOrdenDeTurnos(jugadorInicial, jugadores);
    }

    /**
     * Obtiene el orden de turnos de los jugadores en la partida.
     *
     * @return una lista de DTOs de los jugadores en orden de turnos.
     */
    public List<Jugador> getOrdenDeTurnos() {
     return servicioControlJuego.getOrdenDeTurnos();
 
     
    }

    /**
     * Determina el jugador inicial para el juego.
     *
     * @param jugadores
     * @param pozo
     * @return el DTO del jugador inicial.
     */
    public Jugador determinarJugadorInicial(List<Jugador> jugadores, Pozo pozo) {
       
      return servicioControlJuego.determinarJugadorInicial(jugadores, pozo);
      
    }

    /**
     * Termina una partida en curso.
     *
     * @param partida
     */
    public void terminarPartida(Partida partida) {
        servicioControlJuego.terminarPartida(partida);
    }

    /**
     * Inicia una partida.
     *
     * @param partida
     */
    public void iniciarJuego(Partida partida) {
        servicioControlJuego.iniciarJuego(partida);
    }

    /**
     * Realiza un turno en una partida.
     *
     * @param partida
     */
    public void realizarTurno(Partida partida) {
        servicioControlJuego.realizarTurno(partida);
    }

    /**
     * Calcula los puntajes de los jugadores en una partida.
     *
     * @param partida
     */
    public void calcularPuntajes(Partida partida) {
        servicioControlJuego.calcularPuntajes(partida);
    }

 
}
