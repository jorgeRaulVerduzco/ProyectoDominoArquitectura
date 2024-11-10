/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackBoard;

import Controller.Controller;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Sala;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author INEGI
 */
public class BlackBoard {

    private Map<String, Sala> salas;
    private Map<String, Jugador> jugadores;
    private Map<String, Partida> partidas;
    private Controller controller;

    /**
     * Constructor de la clase BlackBoard. Inicializa los mapas de salas,
     * jugadores y partidas.
     */
    public BlackBoard() {
        this.salas = new HashMap<>();
        this.jugadores = new HashMap<>();
        this.partidas = new HashMap<>();
    }

    /**
     * Establece el controlador que manejará las notificaciones de cambios.
     *
     * @param controller El controlador a establecer.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Actualiza el estado de una sala. Guarda la nueva información de la sala
     * en el mapa de salas y notifica al controlador que el estado de la sala ha
     * cambiado.
     *
     * @param salaId El ID de la sala a actualizar.
     * @param sala La nueva instancia de sala con el estado actualizado.
     */
    public void actualizarEstadoSala(String salaId, Sala sala) {
        salas.put(salaId, sala);
        controller.notificarCambio("SALA_ACTUALIZADA", sala);
    }

    /**
     * Actualiza el estado de un jugador. Guarda la nueva información del
     * jugador en el mapa de jugadores y notifica al controlador que el estado
     * del jugador ha cambiado.
     *
     * @param jugadorId El ID del jugador a actualizar.
     * @param jugador La nueva instancia de jugador con el estado actualizado.
     */
    public void actualizarEstadoJugador(String jugadorId, Jugador jugador) {
        jugadores.put(jugadorId, jugador);
        controller.notificarCambio("JUGADOR_ACTUALIZADO", jugador);
    }

    /**
     * Actualiza el estado de una partida. Guarda la nueva información de la
     * partida en el mapa de partidas y notifica al controlador que el estado de
     * la partida ha cambiado.
     *
     * @param partidaId El ID de la partida a actualizar.
     * @param partida La nueva instancia de partida con el estado actualizado.
     */
    public void actualizarEstadoPartida(String partidaId, Partida partida) {
        partidas.put(partidaId, partida);
        controller.notificarCambio("PARTIDA_ACTUALIZADA", partida);
    }

    /**
     * Remueve a un jugador del sistema. Elimina al jugador del mapa de
     * jugadores y notifica al controlador que el jugador se ha desconectado.
     *
     * @param jugadorId El ID del jugador a remover.
     */
    public void removerJugador(String jugadorId) {
        Jugador jugador = jugadores.remove(jugadorId);
        if (jugador != null) {
            controller.notificarCambio("JUGADOR_DESCONECTADO", jugador);
        }
    }

    /**
     * Remueve una sala del sistema. Elimina la sala del mapa de salas y
     * notifica al controlador que la sala ha sido eliminada.
     *
     * @param salaId El ID de la sala a remover.
     */
    public void removerSala(String salaId) {
        Sala sala = salas.remove(salaId);
        if (sala != null) {
            controller.notificarCambio("SALA_ELIMINADA", sala);
        }
    }

    /**
     * Obtiene una sala por su ID.
     *
     * @param salaId El ID de la sala a obtener.
     * @return La sala asociada al ID proporcionado, o null si no existe.
     */
    public Sala getSala(String salaId) {
        return salas.get(salaId);
    }

    /**
     * Obtiene un jugador por su ID.
     *
     * @param jugadorId El ID del jugador a obtener.
     * @return El jugador asociado al ID proporcionado, o null si no existe.
     */
    public Jugador getJugador(String jugadorId) {
        return jugadores.get(jugadorId);
    }

    /**
     * Obtiene una partida por su ID.
     *
     * @param partidaId El ID de la partida a obtener.
     * @return La partida asociada al ID proporcionado, o null si no existe.
     */
    public Partida getPartida(String partidaId) {
        return partidas.get(partidaId);
    }

    /**
     * Obtiene una lista de las salas disponibles que están en estado
     * "ESPERANDO".
     *
     * @return Una lista de salas disponibles.
     */
    public List<Sala> getSalasDisponibles() {
        return salas.values().stream()
                .filter(sala -> "ESPERANDO".equals(sala.getEstado()))
                .collect(Collectors.toList());
    }
}
