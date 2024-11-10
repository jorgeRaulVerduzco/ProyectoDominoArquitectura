/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import BlackBoard.BlackBoard;
import Dominio.Jugador;
import EventoJuego.Evento;
import KnowdledgeSource.KnowdledgeSource;
import KnowdledgeSource.SalaKnowledgeSource;
import Server.Server;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class Controller {

    private BlackBoard blackboard;
    private List<KnowdledgeSource> knowledgeSources;
    private Server server;

    /**
     * Constructor de la clase Controller. Inicializa la pizarra, las fuentes de
     * conocimiento y el servidor. También registra las fuentes de conocimiento
     * en el sistema.
     *
     * @param server El servidor que enviará los eventos.
     */
    public Controller(Server server) {
        this.blackboard = new BlackBoard();
        this.knowledgeSources = new ArrayList<>();
        this.server = server;
        this.blackboard.setController(this);
        registrarKnowledgeSources();
    }

    /**
     * Registra las fuentes de conocimiento que procesarán los eventos. En este
     * caso, se registra una fuente de conocimiento relacionada con las salas.
     */
    private void registrarKnowledgeSources() {
        knowledgeSources.add(new SalaKnowledgeSource(blackboard, server));

    }

    /**
     * Procesa un evento que llega desde un cliente. Se verifica qué fuente de
     * conocimiento puede procesar el evento, y se delega la tarea de procesarlo
     * a esa fuente de conocimiento.
     *
     * @param cliente El cliente que envió el evento.
     * @param evento El evento que se debe procesar.
     */
    public void procesarEvento(Socket cliente, Evento evento) {
        for (KnowdledgeSource ks : knowledgeSources) {
            if (ks.puedeProcesar(evento)) {
                ks.procesarEvento(cliente, evento);
                break;
            }
        }
    }

    /**
     * Procesa la desconexión de un jugador. Se notifica a todas las salas donde
     * el jugador estaba presente, y se actualiza el estado de esas salas o se
     * elimina si es necesario. Finalmente, se elimina al jugador de la pizarra.
     *
     * @param jugador El jugador que se ha desconectado.
     */
    public void procesarDesconexion(Jugador jugador) {
        // Notificar a todas las salas donde el jugador estaba
        blackboard.getSalasDisponibles().stream()
                .filter(sala -> sala.getJugador().contains(jugador))
                .forEach(sala -> {
                    sala.getJugador().remove(jugador);
                    if (sala.getJugador().isEmpty()) {
                        blackboard.removerSala(sala.getId());
                    } else {
                        blackboard.actualizarEstadoSala(sala.getId(), sala);
                    }
                });

        // Remover el jugador del blackboard
        blackboard.removerJugador(jugador.getNombre());
    }

    /**
     * Notifica un cambio al servidor. Crea un evento con el tipo de cambio y lo
     * envía al servidor para que se notifique a los clientes.
     *
     * @param tipoEvento El tipo de evento que describe el cambio.
     * @param dato El dato relacionado con el evento.
     */
    public void notificarCambio(String tipoEvento, Object dato) {
        Evento notificacion = new Evento(tipoEvento);
        notificacion.agregarDato("dato", dato);
        server.enviarEvento(notificacion);
    }
}
