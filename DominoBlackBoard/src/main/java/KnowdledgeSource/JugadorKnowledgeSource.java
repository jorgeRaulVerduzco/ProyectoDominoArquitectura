/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KnowdledgeSource;

import BlackBoard.BlackBoard;
import Dominio.Jugador;
import EventoJuego.Evento;
import Server.Server;
import java.net.Socket;

/**
 *
 * @author Serva
 */
public class JugadorKnowledgeSource implements KnowdledgeSource {

    private BlackBoard blackboard;
    private Server server;

    public JugadorKnowledgeSource(BlackBoard blackboard, Server server) {
        this.blackboard = blackboard;
        this.server = server;
    }

    @Override
    public boolean puedeProcesar(Evento evento) {
        // Verifica si el evento está relacionado con el registro o actualización de un jugador
        return "REGISTRAR_JUGADOR".equals(evento.getTipo());
    }

    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
        // Verifica que el evento contenga los datos del jugador
        if (evento.obtenerDato("jugador") == null) {
            System.err.println("Error: El evento no contiene datos del jugador.");
            return; // Salir si no hay jugador en el evento
        }

        Jugador jugador = (Jugador) evento.obtenerDato("jugador");

        if ("REGISTRAR_JUGADOR".equals(evento.getTipo())) {
            // Registrar el jugador en el Blackboard
            registrarJugador(jugador);
            // Notificar al servidor o a otros componentes si es necesario
            server.enviarEventoAJugador(jugador, evento);
        }
    }

    private void registrarJugador(Jugador jugador) {
        // Agregar el jugador al Blackboard
        blackboard.agregarJugador(jugador);
    }
}
