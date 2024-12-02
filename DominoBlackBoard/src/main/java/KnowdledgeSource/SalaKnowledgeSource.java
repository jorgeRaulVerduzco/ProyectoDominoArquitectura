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
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                || evento.getTipo().equals("ABANDONAR_SALA")
                || evento.getTipo().equals("SOLICITAR_SALAS");  // Añadir este caso
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
            case "SOLICITAR_SALAS":
                enviarSalasDisponibles(cliente);
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
       try {
        // Validate event data thoroughly
        if (evento == null || evento.getDatos() == null) {
            System.err.println("Error: Evento o datos nulos");
            return;
        }

        // Safely extract and validate each required parameter
        Integer numJugadoresObj = (Integer) evento.obtenerDato("numJugadores");
        Integer numFichasObj = (Integer) evento.obtenerDato("numFichas");
        Jugador jugadorObj = (Jugador) evento.obtenerDato("jugador");

        if (!(numJugadoresObj instanceof Integer) || 
            !(numFichasObj instanceof Integer) || 
            !(jugadorObj instanceof Jugador)) {
            System.err.println("Error: Tipos de datos inválidos");
            return;
        }

        int numJugadores = numJugadoresObj;
        int numFichas = numFichasObj;
        Jugador creador = (Jugador) jugadorObj;

        // Existing validation
        if (numJugadores <= 0 || numFichas <= 0 || creador == null) {
            System.err.println("Error: Datos inválidos para crear sala");
            return;
        }

        Sala nuevaSala = new Sala();
        nuevaSala.setId(UUID.randomUUID().toString());
        nuevaSala.setCantJugadores(numJugadores);
        nuevaSala.setNumeroFichas(numFichas);
        nuevaSala.setEstado("ESPERANDO");
        nuevaSala.getJugador().add(creador);

        blackboard.actualizarEstadoSala(nuevaSala.getId(), nuevaSala);
        
        Evento respuesta = new Evento("CREAR_SALA");
        respuesta.agregarDato("sala", nuevaSala);
        
        server.enviarMensajeACliente(cliente, respuesta);
           System.out.println("ENVIAR EVENTO");
        server.enviarEvento(evento, cliente);
        
        System.out.println("Sala creada con ID: " + nuevaSala.getId());
    } catch (Exception e) {
        System.err.println("Error crítico al crear sala: " + e.getMessage());
        e.printStackTrace();
    }
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
            server.enviarEventoATodos(respuesta);
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
            blackboard.respuestaFuenteC(cliente, respuesta);
        }
    }

    private void enviarSalasDisponibles(Socket cliente) {
     List<Sala> salasDisponibles = blackboard.getSalasDisponibles();
    System.out.println("Total salas disponibles: " + salasDisponibles.size());

    for (Sala sala : salasDisponibles) {
        System.out.println("Sala ID: " + sala.getId() 
            + ", Estado: " + sala.getEstado()
            + ", Jugadores: " + sala.getJugador().size() + "/" + sala.getCantJugadores());
    }

    Evento respuesta = new Evento("SOLICITAR_SALAS");
    respuesta.agregarDato("salas", new ArrayList<>(salasDisponibles)); 
    server.enviarMensajeACliente(cliente, respuesta);
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
//        server.enviarEvento(eventoInicio);
    }
}
