/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KnowdledgeSource;

import BlackBoard.BlackBoard;
import Dominio.Ficha;
import Dominio.Partida;
import Dominio.Tablero;
import EventoJuego.Evento;
import Server.Server;
import java.net.Socket;

/**
 *
 * @author INEGI
 */
public class PartidaKnowledgeSource implements KnowdledgeSource {

    private BlackBoard blackboard;
    private Server server;

    public PartidaKnowledgeSource(BlackBoard blackboard, Server server) {
        this.blackboard = blackboard;
        this.server = server;
    }

    @Override
    public boolean puedeProcesar(Evento evento) {
        System.out.println("Fuente de conocimiento procesando evento: " + evento.getTipo());

        // Verificar si el evento es de tipo "CREAR_PARTIDA" o "JUGADA"
        return evento.getTipo().equals("CREAR_PARTIDA") || evento.getTipo().equals("JUGADA");
    }

    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
        System.out.println("Procesando evento desde la fuente");

        // Obtener el tipo de evento
        String tipoEvento = evento.getTipo();

        if ("JUGADA".equals(tipoEvento)) {
            String partidaId = (String) evento.obtenerDato("partidaId");
            String jugadorId = (String) evento.obtenerDato("jugadorId");
            Ficha ficha = (Ficha) evento.obtenerDato("ficha");
            int lado = (int) evento.obtenerDato("lado");

            if (partidaId == null || jugadorId == null || ficha == null) {
                System.err.println("Error: El evento 'JUGADA' no contiene datos válidos.");
                return;
            }

            // Obtener el tablero de la partida
            Tablero tablero = blackboard.obtenerTablero(partidaId, jugadorId);
            if (tablero == null) {
                System.err.println("Error: No se encontró el tablero para el jugador.");
                return;
            }

            // Agregar la ficha al tablero
            System.out.println("Ficha agregada correctamente al tablero.");
            // Actualizar el estado del tablero en el BlackBoard
            blackboard.actualizarTablero(partidaId, tablero);
            // Notificar el cambio a los jugadores o servidores si es necesario
            blackboard.respuestaFuenteC(cliente, evento);  // Método ficticio que responde a los jugadores o servidores

            System.err.println("Error: No se pudo agregar la ficha al tablero.");

        }
    }

    private void registrarPartida(Partida partida) {
        blackboard.agregarPartida(partida);
    }
}
