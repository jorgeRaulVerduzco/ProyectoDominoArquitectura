/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KnowdledgeSource;

import BlackBoard.BlackBoard;
import Dominio.Partida;
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

        // Verificar si este evento es de tipo CREAR_PARTIDA
        return evento.getTipo().equals("CREAR_PARTIDA");
    }

    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
        System.out.println("Porcesando envento desde la fuente");
        if (evento.obtenerDato("partida") == null) {
            System.err.println("Error: El evento no contiene datos del jugador.");
            return;
        }

        Partida partida = (Partida) evento.obtenerDato("partida");

        if ("CREAR_PARTIDA".equals(evento.getTipo())) {
            registrarPartida(partida);
            System.out.println("KYS  : Socket del jugador actual" + cliente);
            blackboard.respuestaFuenteC(cliente, evento);
        }
    }

    private void registrarPartida(Partida partida) {
        blackboard.agregarPartida(partida);
    }
}
