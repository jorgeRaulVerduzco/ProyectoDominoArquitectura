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

public class JugadorKnowledgeSource implements KnowdledgeSource {

    private BlackBoard blackboard;
    private Server server;

    public JugadorKnowledgeSource(BlackBoard blackboard, Server server) {
        this.blackboard = blackboard;
        this.server = server;
    }

    @Override
public boolean puedeProcesar(Evento evento) {
    // Agregar un log para ver qué tipo de eventos están siendo procesados
    System.out.println("Fuente de conocimiento procesando evento: " + evento.getTipo());

    // Verificar si este evento es de tipo REGISTRO_USUARIO
    return evento.getTipo().equals("REGISTRO_USUARIO");
}


    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
        System.out.println("Porcesando envento desde la fuente");
        if (evento.obtenerDato("jugador") == null) {
            System.err.println("Error: El evento no contiene datos del jugador.");
            return;
        }

        Jugador jugador = (Jugador) evento.obtenerDato("jugador");

        if ("REGISTRO_USUARIO".equals(evento.getTipo())) {
            registrarJugador(jugador);
             System.out.println("KYS  : Socket del jugador actual"+cliente);
            blackboard.respuestaFuenteC(cliente, evento);
        }
    }

    private void registrarJugador(Jugador jugador) {
        blackboard.agregarJugador(jugador);
    }
}
