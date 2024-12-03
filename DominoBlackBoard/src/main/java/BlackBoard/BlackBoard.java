/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackBoard;

import Controller.Controller;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Sala;
import EventoJuego.Evento;
import KnowdledgeSource.JugadorKnowledgeSource;
import KnowdledgeSource.KnowdledgeSource;
import KnowdledgeSource.SalaKnowledgeSource;
import Server.Server;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author INEGI
 */
public class BlackBoard {

    private Map<String, Sala> salas;
   private Map<String, Jugador> jugadores;
    private Map<String, Partida> partidas;
    private Controller controller;
    private List<KnowdledgeSource> observers;
    private Server server;  // Asegúrate de tener una referencia al Server
public Map<String, Jugador> getJugadores() {
    return new HashMap<>(jugadores);  // Devolver una copia para evitar modificaciones externas
}

public Map<String, Sala> getSalas() {
    return new HashMap<>(salas);  // Devolver una copia para evitar modificaciones externas
}
    public BlackBoard(Server server) {

        this.server = server;  // Inicializa el server
        this.salas = new HashMap<>();
        this.jugadores = new HashMap<>();
        this.partidas = new HashMap<>();
        this.observers = new ArrayList<>();
        registrarFuentesDeConocimiento();  // Crear y registrar fuentes de conocimiento

    }

    // Método setter para asignar el controller después de la creación del objeto
    public void setController(Controller controller) {
        this.controller = controller;
    }

    // Método para registrar las fuentes de conocimiento
    private void registrarFuentesDeConocimiento() {
        // Crear las fuentes de conocimiento necesarias, ahora pasamos el Server también
        KnowdledgeSource fuenteSala = new SalaKnowledgeSource(this, server);  // Pasar tanto BlackBoard como Server
        KnowdledgeSource fuenteJugador = new JugadorKnowledgeSource(this, server);

        // Agregar las fuentes al Blackboard
        observers.add(fuenteSala);
        observers.add(fuenteJugador);

        // Aquí puedes registrar más fuentes de conocimiento si es necesario
        System.out.println("Fuentes de conocimiento registradas.");
    }

    // Método genérico para actualizar el estado de una entidad
    public <T> void actualizarEstadoEntidad(Map<String, T> mapa, String id, T entidad, String tipoEntidad) {
        mapa.put(id, entidad);
        controller.notificarCambio(tipoEntidad + "_ACTUALIZADO");
    }

    // Métodos específicos usando el genérico anterior
    public void actualizarEstadoSala(String salaId, Sala sala) {
        System.out.println("llegue ALV");
        actualizarEstadoEntidad(salas, salaId, sala, "sala");
    }

    public void actualizarEstadoJugador(String jugadorId, Jugador jugador) {
        actualizarEstadoEntidad(jugadores, jugadorId, jugador, "JUGADOR");
    }

    public void actualizarEstadoPartida(String partidaId, Partida partida) {
        actualizarEstadoEntidad(partidas, partidaId, partida, "PARTIDA");
    }

    // Método para agregar un jugador
    public void agregarJugador(Jugador jugador) {
        if (controller != null) {
            // Verificamos si el jugador tiene un ID válido
            if (jugador != null && jugador.getNombre() != null) {
                // Agregamos el jugador al mapa usando su ID como clave
                jugadores.put(jugador.getNombre(), jugador);  // Usa el ID del jugador como clave para el mapa

                // Notificamos el cambio al controlador después de agregar el jugador
                controller.notificarCambio("REGISTRO_USUARIO");
            } else {
                System.err.println("Error: El jugador o su ID es nulo.");
            }
        } else {
            System.err.println("Error: El controlador es null.");
        }
    }

  public void agregarSala(Sala sala) {
     if (sala == null || sala.getId() == null) {
        System.err.println("Error: Sala inválida o sin ID");
        return;
    }
    
    if (controller != null) {
        // Log detallado
        System.out.println("Agregando sala: " + sala.getId());
        System.out.println("Detalles de la sala: " + 
            "Jugadores=" + (sala.getJugador() != null ? sala.getJugador().size() : "0") + 
            ", Estado=" + sala.getEstado());
        
        salas.put(sala.getId(), sala);
        
        System.out.println("Total de salas después de agregar: " + salas.size());
        controller.notificarCambio("CREAR_SALA");
    } else {
        System.err.println("Error: El controlador es nulo.");
    }
}
    public void removerJugador(String jugadorId) {
        Jugador jugador = jugadores.remove(jugadorId);
        if (jugador != null) {
            controller.notificarCambio("JUGADOR_DESCONECTADO");
        }
    }

    public void removerSala(String salaId) {
        Sala sala = salas.remove(salaId);
        if (sala != null) {
            controller.notificarCambio("SALA_ELIMINADA");
        }
    }

    public Sala getSala(String salaId) {
        return salas.get(salaId);
    }

    public Jugador getJugador(String jugadorId) {
        return jugadores.get(jugadorId);
    }

    public Partida getPartida(String partidaId) {
        return partidas.get(partidaId);
    }

    public List<Sala> getSalasDisponibles() {
        return salas.values().stream()
                .filter(sala -> "ESPERANDO".equals(sala.getEstado()))
                .collect(Collectors.toList());
    }

    public void enviarEventoBlackBoard(Socket cliente, Evento evento) {
        System.out.println("LLEGUE A BLACKBOARD");
        System.out.println("BLACKBOARD 1  : Socket del jugador actual" + cliente);
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }

        System.out.println("NOTIFICANDO A LAS FUENTES");

        boolean eventoProcesado = false;  // Variable para verificar si alguna fuente procesó el evento

        // Notificar solo a las fuentes que pueden procesar este evento
        for (KnowdledgeSource observer : observers) {
            if (observer.puedeProcesar(evento)) {
                System.out.println("BLACKBOARD 2  : Socket del jugador actual" + cliente);
                observer.procesarEvento(cliente, evento);
                eventoProcesado = true;  // Marcar que al menos una fuente procesó el evento
            }
        }

        if (!eventoProcesado) {
            throw new IllegalStateException("Ninguna fuente de conocimiento pudo procesar el evento: " + evento.getTipo());
        }

        System.out.println("Evento procesado por las fuentes de conocimiento.");
    }

    public void respuestaFuenteC(Socket cliente, Evento eventoRespuesta) {
        System.out.println("Fuente de conocimiento coloco respuesta en blackboard");

        if (eventoRespuesta == null) {
            throw new IllegalArgumentException("El evento de respuesta no puede ser nulo.");
        }
        System.out.println("BLACKBOARD 3  : Socket del jugador actual" + cliente);
        System.out.println("Recibiendo respuesta de una fuente de conocimiento: " + eventoRespuesta.getDatos());
    }
}
