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
        Sala sala = (Sala) evento.obtenerDato("sala");
        
        System.out.println("ALVVVVV: "+sala);
        switch (evento.getTipo()) {
            case "CREAR_SALA":
                registrarSala(sala);
                System.out.println("KYS  : Socket del jugador actual" + cliente);
                blackboard.respuestaFuenteC(cliente, evento);
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
            System.out.println(evento.getDatos());
            Sala sala = (Sala) evento.obtenerDato("sala");

            // Validate sala
            if (sala == null) {
                System.err.println("Error: Sala es nula");
                return;
            }

            if (sala.getCantJugadores() <= 0 || sala.getNumeroFichas() <= 0) {
                System.err.println("Error: Configuración de sala inválida");
                return;
            }

            if (sala.getJugador() == null || sala.getJugador().isEmpty()) {
                System.err.println("Error: La sala debe tener al menos un jugador");
                return;
            }

            registrarSala(sala);
            System.out.println("Sala creada correctamente: " + sala.getId());
            blackboard.respuestaFuenteC(cliente, evento);
        } catch (Exception e) {
            System.err.println("Error al crear sala: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registrarSala(Sala sala) {
        blackboard.agregarSala(sala);
        server.registrarSalas(blackboard.getSalas()); 
        System.out.println("--------------- PORFAVOR QUE NO SEAN 0--------------------------------");
        System.out.println(blackboard.getSalas());
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
    try {
        // Obtener datos del evento
        String salaId = (String) evento.obtenerDato("id"); // Cambié la clave a "id"
        Jugador jugador = (Jugador) evento.obtenerDato("jugador");

        // Validar datos del evento
        if (salaId == null || salaId.isEmpty()) {
            throw new IllegalArgumentException("El ID de la sala no puede ser nulo o vacío.");
        }
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        // Obtener la sala desde el Blackboard
        Sala sala = blackboard.getSala(salaId);
        if (sala == null) {
            throw new IllegalStateException("La sala con ID " + salaId + " no existe.");
        }

        // Validar si la sala tiene espacio disponible
        if (sala.getJugador().size() >= sala.getCantJugadores()) {
            throw new IllegalStateException("La sala con ID " + salaId + " está llena.");
        }

        // Agregar al jugador a la sala
        sala.getJugador().add(jugador);
        blackboard.actualizarEstadoSala(salaId, sala);

        // Verificar si la sala está completa para iniciar la partida
        if (sala.getJugador().size() == sala.getCantJugadores()) {
            iniciarPartida(sala);
        }

        // Enviar evento de respuesta
        Evento respuesta = new Evento("UNIR_SALA");
        respuesta.agregarDato("jugador", jugador);
        respuesta.agregarDato("sala", sala);
        System.out.println("RESpUESTA DE LA FUENTE DE CONOCIMIENTO DE UNIRSE SALA: "+respuesta.getDatos());
        
        blackboard.respuestaFuenteC(cliente,respuesta);

    } catch (Exception e) {
        System.err.println("Error en unirseASala: " + e.getMessage());
        e.printStackTrace();
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
