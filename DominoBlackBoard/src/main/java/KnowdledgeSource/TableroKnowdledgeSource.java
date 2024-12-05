/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KnowdledgeSource;

import BlackBoard.BlackBoard;
import Dominio.Ficha;
import Dominio.Tablero;
import EventoJuego.Evento;
import Server.Server;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class TableroKnowdledgeSource implements KnowdledgeSource {
    private BlackBoard blackboard;
    private Server server;

    public TableroKnowdledgeSource(BlackBoard blackboard, Server server) {
        this.blackboard = blackboard;
        this.server = server;
    }

    @Override
    public boolean puedeProcesar(Evento evento) {
        System.out.println("Fuente de conocimiento procesando evento: " + evento.getTipo());

        // Verificar si este evento es de tipo CREAR_PARTIDA
        return evento.getTipo().equals("DATOS_TABLERO");
    }

    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
System.out.println("Procesando evento desde TableroKnowledgeSource...");
        
        // Obtener los datos del evento
        Tablero tablero = (Tablero) evento.obtenerDato("tablero");
        List<Ficha> fichasTablero = (List<Ficha>) evento.obtenerDato("fichasTablero");
        List<Ficha> fichasPozo = (List<Ficha>) evento.obtenerDato("fichasPozo");

        // Validar datos
        if (tablero == null) {
            System.err.println("Error: El evento no contiene un tablero válido.");
            return;
        }
        if (fichasTablero == null) {
            System.err.println("Error: El evento no contiene fichas del tablero válidas.");
            return;
        }
        if (fichasPozo == null) {
            System.err.println("Error: El evento no contiene fichas del pozo válidas.");
            return;
        }

        // Procesar los datos (por ejemplo, actualizando el estado del BlackBoard)
        blackboard.actualizarTablero(tablero);
        blackboard.actualizarFichasTablero(fichasTablero);
        blackboard.actualizarFichasPozo(fichasPozo);

        // Preparar una respuesta si es necesario
        Evento respuesta = new Evento("RESPUESTA_DATOS_TABLERO");
        respuesta.agregarDato("estado", "Datos del tablero procesados correctamente");
        respuesta.agregarDato("tablero", tablero);
        respuesta.agregarDato("fichasTablero", fichasTablero);
        respuesta.agregarDato("fichasPozo", fichasPozo);

        // Enviar respuesta al cliente
        System.out.println("Enviando respuesta al cliente...");
        blackboard.respuestaFuenteC(cliente, respuesta);
        System.out.println("Respuesta enviada correctamente.");    }

}
