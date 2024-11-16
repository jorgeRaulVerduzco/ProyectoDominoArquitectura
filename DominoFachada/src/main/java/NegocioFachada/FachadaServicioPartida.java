/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NegocioFachada;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOs.JugadorDTO;
import DTOs.PartidaDTO;
import Dominio.Jugador;
import Dominio.Partida;
import Negocio.ServicioPartida;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class FachadaServicioPartida {
    
    private ServicioPartida servicioPartida;
    private ConversorDTOaEntidad conversor;

    public FachadaServicioPartida() {
        this.servicioPartida = new ServicioPartida();
        this.conversor = new ConversorDTOaEntidad();
    }

    /**
     * Inicia una partida a partir de un PartidaDTO.
     *
     * @param partidaDTO el objeto PartidaDTO que contiene los datos de la partida.
     */
    public void iniciarPartida(PartidaDTO partidaDTO) {
        Partida partida = conversor.convertirPartida(partidaDTO);
        servicioPartida.iniciarPartida(partida);
    }

    /**
     * Valida la cantidad de jugadores en una partida.
     *
     * @param jugadoresDTO lista de objetos JugadorDTO.
     * @return true si la cantidad de jugadores es válida, false en caso contrario.
     */
    public boolean validarCantidadJugadores(List<JugadorDTO> jugadoresDTO) {
        List<Jugador> jugadores = conversor.convertirListaJugadores(jugadoresDTO);
        return servicioPartida.validarCantidadJugadores(jugadores);
    }

    /**
     * Valida la cantidad de fichas en una partida.
     *
     * @param cantidadFichas la cantidad de fichas por jugador.
     * @return true si la cantidad de fichas es válida, false en caso contrario.
     */
    public boolean validarCantidadFichas(int cantidadFichas) {
        return servicioPartida.validarCantidadFichas(cantidadFichas);
    }

    /**
     * Termina una partida a partir de un PartidaDTO.
     *
     * @param partidaDTO el objeto PartidaDTO que contiene los datos de la partida.
     */
    public void terminarPartida(PartidaDTO partidaDTO) {
        Partida partida = conversor.convertirPartida(partidaDTO);
        servicioPartida.terminarPartida(partida);
    }

    /**
     * Calcula los puntajes de los jugadores en una partida.
     *
     * @param partidaDTO el objeto PartidaDTO que contiene los datos de la partida.
     */
    public void calcularPuntajes(PartidaDTO partidaDTO) {
        Partida partida = conversor.convertirPartida(partidaDTO);
        servicioPartida.calcularPuntajes(partida);
    }

    /**
     * Reparte fichas a los jugadores (para pruebas internas o debugging).
     *
     * @param partidaDTO el objeto PartidaDTO que contiene los datos de la partida.
     */
    public void repartirFichas(PartidaDTO partidaDTO) {
        Partida partida = conversor.convertirPartida(partidaDTO);
        // Uso reflejado para invocar el método privado repartirFichas (sólo para fines demostrativos).
        try {
            java.lang.reflect.Method metodo = ServicioPartida.class.getDeclaredMethod("repartirFichas", Partida.class);
            metodo.setAccessible(true);
            metodo.invoke(servicioPartida, partida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
