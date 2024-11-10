/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package KnowdledgeSource;

import BlackBoard.BlackBoard;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Sala;
import Dominio.Tablero;
import EventoJuego.Evento;
import Server.Server;
import java.net.Socket;
import java.util.UUID;

/**
 *
 * @author INEGI
 */
public class SalaKnowledgeSource implements KnowdledgeSource {

    private BlackBoard blackboard;
    private Server server;

    /**
     * Constructor que inicializa la clase con una instancia de BlackBoard y un
     * Server.
     *
     * @param blackboard Instancia de BlackBoard que gestiona el estado de las
     * salas y partidas.
     * @param server Instancia del servidor utilizado para interactuar con los
     * clientes.
     */
    public SalaKnowledgeSource(BlackBoard blackboard, Server server) {
        this.blackboard = blackboard;
        this.server = server;
    }

    /**
     * Verifica si este conocimiento (knowledge source) puede procesar el evento
     * proporcionado. Solo procesa eventos de tipo "CREAR_SALA", "UNIR_SALA" o
     * "ABANDONAR_SALA".
     *
     * @param evento Evento a ser evaluado.
     * @return true si el evento puede ser procesado, false en caso contrario.
     */
    @Override
    public boolean puedeProcesar(Evento evento) {
        return evento.getTipo().equals("CREAR_SALA")
                || evento.getTipo().equals("UNIR_SALA")
                || evento.getTipo().equals("ABANDONAR_SALA");
    }

    /**
     * Procesa el evento proporcionado dependiendo de su tipo. Los tipos de
     * eventos que puede manejar son "CREAR_SALA", "UNIR_SALA" y
     * "ABANDONAR_SALA".
     *
     * @param cliente Socket del cliente que envía el evento.
     * @param evento Evento a ser procesado.
     */
    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
        switch (evento.getTipo()) {
            case "CREAR_SALA":
                crearNuevaSala(cliente, evento);
                break;
            case "UNIR_SALA":
                unirseASala(cliente, evento);
                break;
            case "ABANDONAR_SALA":
                abandonarSala(cliente, evento);
                break;
        }
    }

    /**
     * Crea una nueva sala de juego basada en la información proporcionada en el
     * evento. Envía una respuesta al cliente con los detalles de la sala
     * creada.
     *
     * @param cliente Socket del cliente que envía el evento.
     * @param evento Evento que contiene los datos necesarios para crear la
     * sala.
     */
    private void crearNuevaSala(Socket cliente, Evento evento) {
        int numJugadores = (int) evento.obtenerDato("numJugadores");
        int numFichas = (int) evento.obtenerDato("numFichas");
        Jugador creador = (Jugador) evento.obtenerDato("jugador");

        Sala nuevaSala = new Sala();
        nuevaSala.setId(UUID.randomUUID().toString());
        nuevaSala.setCantJugadores(numJugadores);
        nuevaSala.setNumeroFichas(numFichas);
        nuevaSala.setEstado("ESPERANDO");
        nuevaSala.getJugador().add(creador);

        blackboard.actualizarEstadoSala(nuevaSala.getId(), nuevaSala);

        Evento respuesta = new Evento("SALA_CREADA");
        respuesta.agregarDato("sala", nuevaSala);
       server.enviarMensajeACliente(cliente, respuesta);
    }

    /**
     * Permite que un jugador se una a una sala existente. Si la sala está llena
     * después de la unión, se inicia la partida. Envía una respuesta al cliente
     * con los detalles de la sala.
     *
     * @param cliente Socket del cliente que envía el evento.
     * @param evento Evento que contiene los datos necesarios para unirse a la
     * sala.
     */
    private void unirseASala(Socket cliente, Evento evento) {
        String salaId = (String) evento.obtenerDato("salaId");
        Jugador jugador = (Jugador) evento.obtenerDato("jugador");
        Sala sala = blackboard.getSala(salaId);

        if (sala != null && sala.getJugador().size() < sala.getCantJugadores()) {
            sala.getJugador().add(jugador);
            blackboard.actualizarEstadoSala(salaId, sala);

            if (sala.getJugador().size() == sala.getCantJugadores()) {
                iniciarPartida(sala);
            }

            Evento respuesta = new Evento("JUGADOR_UNIDO");
            respuesta.agregarDato("jugador", jugador);
            respuesta.agregarDato("sala", sala);
            server.enviarEvento(respuesta);
        }
    }

    /**
     * Permite que un jugador abandone una sala. Si la sala queda vacía, se
     * elimina. Envía una respuesta al cliente con los detalles de la sala.
     *
     * @param cliente Socket del cliente que envía el evento.
     * @param evento Evento que contiene los datos necesarios para abandonar la
     * sala.
     */
    private void abandonarSala(Socket cliente, Evento evento) {
        String salaId = (String) evento.obtenerDato("salaId");
        Jugador jugador = (Jugador) evento.obtenerDato("jugador");
        Sala sala = blackboard.getSala(salaId);

        if (sala != null) {
            sala.getJugador().remove(jugador);
            if (sala.getJugador().isEmpty()) {
                blackboard.removerSala(salaId);
            } else {
                blackboard.actualizarEstadoSala(salaId, sala);
            }

            Evento respuesta = new Evento("JUGADOR_ABANDONO");
            respuesta.agregarDato("jugador", jugador);
            respuesta.agregarDato("sala", sala);
            server.enviarEvento(respuesta);
        }
    }

    /**
     * Inicia una partida cuando una sala se ha llenado. Configura el estado de
     * la partida y actualiza el estado de la sala. Notifica a los clientes que
     * la partida ha comenzado.
     *
     * @param sala Sala donde se iniciará la partida.
     */
    private void iniciarPartida(Sala sala) {
        Partida partida = new Partida();
        String partidaId = UUID.randomUUID().toString();
        partida.setCantJugadores(sala.getCantJugadores());
        partida.setCantFichas(sala.getNumeroFichas());
        partida.setEstado("EN_CURSO");
        partida.setJugadores(sala.getJugador());
        partida.setTablero(new Tablero());

        blackboard.actualizarEstadoPartida(partidaId, partida);

        sala.setPartida(partida);
        sala.setEstado("EN_JUEGO");
        blackboard.actualizarEstadoSala(sala.getId(), sala);

        Evento eventoInicio = new Evento("INICIAR_PARTIDA");
        eventoInicio.agregarDato("partida", partida);
        eventoInicio.agregarDato("sala", sala);
        server.enviarEvento(eventoInicio);
    }
}
