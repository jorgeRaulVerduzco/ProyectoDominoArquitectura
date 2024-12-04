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
import Server.ConversorJSON;
import Server.Server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
            || evento.getTipo().equals("SOLICITAR_SALAS")
            || evento.getTipo().equals("JUGADORES_ESPERA");
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

        System.out.println("ALVVVVV: " + sala);
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
                case "JUGADORES_ESPERA":
//            obtenerJugadoresPorSala(cliente, evento);
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
    //este actualiza
   private void unirseASala(Socket cliente, Evento evento) {
    try {
        Sala sala = (Sala) evento.obtenerDato("sala");
        Jugador jugador = (Jugador) evento.obtenerDato("jugador");

        if (sala == null || sala.getId() == null || sala.getId().isEmpty()) {
            throw new IllegalArgumentException("El ID de la sala no puede ser nulo o vacío.");
        }
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }

        // Cargar la sala existente y su lista de jugadores
        Sala salaExistente = cargarSalaExistente(sala.getId());
        if (salaExistente != null) {
            sala.setJugador(new ArrayList<>(salaExistente.getJugador())); // Copiar jugadores existentes
        }

        // Agregar nuevo jugador si no está ya presente
        boolean jugadorExiste = sala.getJugador().stream()
            .anyMatch(j -> j.getNombre().equals(jugador.getNombre()));

        if (!jugadorExiste) {
            sala.getJugador().add(jugador);
        }

        // Actualizar cantidad de jugadores
        sala.setCantJugadores(sala.getJugador().size());

        // Guardar cambios
        server.actualizarSala(sala);

        System.out.println("Jugadores en la sala después de agregar: " + sala.getJugador());

        // Comenzar la partida si está llena
        if (sala.getJugador().size() == sala.getCantJugadores()) {
            iniciarPartida(sala);
        }

    } catch (Exception e) {
        System.err.println("Error en unirseASala: " + e.getMessage());
        e.printStackTrace();
    }
}
   
   public Sala obtenerSalaPorId(String id) {
    Path salasPath = Paths.get("salas_multijugador.json");
    try {
        List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(Files.readString(salasPath, StandardCharsets.UTF_8));
        return salasCargadas.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    } catch (IOException e) {
        System.err.println("Error al leer las salas: " + e.getMessage());
        return null;
    }
}
private Sala cargarSalaExistente(String id) {
    try {
        Path salasPath = Paths.get("salas_multijugador.json");
        List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(Files.readString(salasPath, StandardCharsets.UTF_8));

        return salasCargadas.stream()
                .filter(sala -> sala.getId().equals(id))
                .findFirst()
                .orElse(null);
    } catch (IOException e) {
        System.err.println("Error al cargar las salas: " + e.getMessage());
        return null;
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
            server.actualizarSala(sala);
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
//    
//   private void obtenerJugadoresPorSala(Socket cliente, Evento evento) {
//     Map<String, List<String>> jugadoresPorSala = server.obtenerJugadoresPorSala();
//    
//    Evento respuesta = new Evento("JUGADORES_ESPERA");
//    respuesta.agregarDato("jugadoresPorSala", jugadoresPorSala);
//    
//    server.enviarMensajeACliente(cliente, respuesta);
//}
}
