/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerLocal;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Negocio.ServicioControlJuego;
import Server.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class ServerComunicacion {

    private Server server;
    private ObjectInputStream in;
    private ServicioControlJuego servicioControlJuego;

    /**
     * Constructor que inicializa la comunicación del servidor con los clientes
     * y la lógica del juego.
     *
     * @param server El servidor que manejará las conexiones y enviará los
     * mensajes a los clientes.
     */
    public ServerComunicacion(Server server) {
        this.server = server;
        this.servicioControlJuego = new ServicioControlJuego();
    }
    
    

    /**
     * Procesa los eventos recibidos de los clientes y ejecuta las acciones
     * correspondientes según el tipo de evento.
     *
     * @param cliente El socket del cliente que envió el evento.
     * @param evento El evento enviado por el cliente, que contiene información
     * sobre la acción a procesar.
     */
    public void procesarEvento(Socket cliente, Evento evento) {
        System.out.println("Procesando evento: " + evento.getTipo());
        try {
            switch (evento.getTipo()) {
                 case "CREAR_SALA":
                System.out.println("Servidor: Procesando creación de sala...");
                System.out.println("  - Datos recibidos: ");
                System.out.println("    - Número de jugadores: " + evento.obtenerDato("numJugadores"));
                System.out.println("    - Número de fichas: " + evento.obtenerDato("numFichas"));

                crearNuevaSala(cliente, evento); // Crear la sala
                break;

                case "UNIR_SALA":
                    unirseASala(cliente, evento);
                    break;
                case "ABANDONAR_SALA":
                    // abandonarSala(cliente, evento);
                    break;
                case "JUGADA":
                    // procesarJugada(cliente, evento);
                    break;
                case "SOLICITAR_SALAS":
                    enviarSalasDisponibles(cliente);
                    break;
                default:
                    System.out.println("Evento no reconocido: " + evento.getTipo());
            }
        } catch (Exception e) {
            System.err.println("Error procesando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    

    /**
     * Crea una nueva sala de juego en respuesta a un evento de creación de sala
     * enviado por un cliente.
     *
     * @param cliente El socket del cliente que solicitó crear una sala.
     * @param evento El evento que contiene los datos necesarios para crear la
     * sala (número de jugadores, fichas, etc.).
     */
  private void crearNuevaSala(Socket cliente, Evento evento) {
    try {
        // Extraer datos del evento
        int numJugadores = (int) evento.obtenerDato("numJugadores");
        int numFichas = (int) evento.obtenerDato("numFichas");

        // Crear una nueva sala
        Sala nuevaSala = new Sala();
        nuevaSala.setCantJugadores(numJugadores);
        nuevaSala.setNumeroFichas(numFichas);
        nuevaSala.setEstado("ESPERANDO");

        // Agregar la sala al sistema
        servicioControlJuego.agregarSala(nuevaSala);

        // Mensaje de depuración para confirmar que la sala fue añadida
        System.out.println("Servidor: Sala creada con éxito:");
        System.out.println("  - ID: " + nuevaSala.getId());
        System.out.println("  - Número de jugadores: " + nuevaSala.getCantJugadores());
        System.out.println("  - Número de fichas: " + nuevaSala.getNumeroFichas());
        System.out.println("  - Estado: " + nuevaSala.getEstado());
        System.out.println("Servidor: Total de salas después de agregar: " + servicioControlJuego.getSalasDisponibles().size());

        // Notificar a los clientes conectados
        enviarSalasDisponibles(null);
    } catch (Exception e) {
        System.err.println("Error al crear nueva sala: " + e.getMessage());
        e.printStackTrace();
    }
}





    /**
     * mucho texto pero ese envia la lista de salas disponibles a un cliente
     * específico o a todos los clientes conectados. Este método consulta las
     * salas disponibles a través del servicio de control de juego y crea un
     * evento con esta información para enviarlo al cliente(s).
     *
     * @param cliente el socket del cliente al que se debe enviar la respuesta.
     * Si es `null`, el evento se enviará a todos los clientes conectados.
     */
  private void enviarSalasDisponibles(Socket cliente) {
    try {
        List<Sala> salasDisponibles = servicioControlJuego.getSalasDisponibles();
        System.out.println("Servidor: Enviando " + salasDisponibles.size() + " salas disponibles");

        Evento respuesta = new Evento("RESPUESTA_SALAS");
        respuesta.agregarDato("salas", new ArrayList<>(salasDisponibles));

        if (cliente != null) {
            server.enviarMensajeACliente(cliente, respuesta);
        } else {
            server.enviarEventoATodos(respuesta);
        }
    } catch (Exception e) {
        System.err.println("Error al enviar salas disponibles: " + e.getMessage());
        e.printStackTrace();
    }
}




    
    
    

    /**
     * Permite que un cliente se una a una sala de juego existente, notificando
     * a los demás jugadores en la sala de que un nuevo jugador se ha unido.
     *
     * @param cliente El socket del cliente que desea unirse a la sala.
     * @param evento El evento que contiene los datos de la sala y del jugador
     * que se va a unir.
     */
    private void unirseASala(Socket cliente, Evento evento) {
        Sala sala = (Sala) evento.obtenerDato("sala");
        Jugador jugador = (Jugador) evento.obtenerDato("jugador");

        if (servicioControlJuego.agregarJugador(sala, jugador)) {
            Evento respuesta = new Evento("JUGADOR_UNIDO");
            respuesta.agregarDato("jugador", jugador);
            respuesta.agregarDato("sala", sala);

            for (Jugador j : sala.getJugador()) {
                Socket socketJugador = server.getSocketJugador(j);
                if (socketJugador != null) {
                    server.enviarMensajeACliente(socketJugador, respuesta);
                }
            }

            // Si la sala está llena, iniciar la partida
            if (sala.getJugador().size() == sala.getCantJugadores()) {
                iniciarPartida(sala);
            }
        }
    }

    /**
     * Inicia una nueva partida cuando se cumplen las condiciones necesarias
     * (por ejemplo, cuando la sala tiene el número requerido de jugadores).
     *
     * @param sala La sala en la que se iniciará la partida.
     */
    private void iniciarPartida(Sala sala) {
        Evento eventoInicio = new Evento("INICIAR_PARTIDA");
        eventoInicio.agregarDato("sala", sala);
        eventoInicio.agregarDato("partida", sala.getPartida());

        // Notificar a todos los jugadores
        for (Jugador jugador : sala.getJugador()) {
            Socket socketJugador = server.getSocketJugador(jugador);
            if (socketJugador != null) {
                server.enviarMensajeACliente(socketJugador, eventoInicio);
            }
        }
    }

    /**
     * Maneja los errores de comunicación, mostrando un mensaje de error en la
     * consola y delegando el manejo del error al servidor.
     */
    public void manejarErrorComunicacion() {
        System.out.println("Error en la comunicación");
        server.manejarErrorComunicacion();
    }
    
    
    

    
    private void responderSolicitudSalas(Socket clienteSocket) {
        try {
             List<Sala> salasDisponibles = servicioControlJuego.getSalasDisponibles();
            System.out.println("Servidor: Preparando respuesta de salas disponibles");
            
            // Crear evento de respuesta
            Evento respuesta = new Evento("RESPUESTA_SALAS");
            respuesta.agregarDato("salas", salasDisponibles);
            
            System.out.println("Servidor: Enviando " + 
                (salasDisponibles != null ? salasDisponibles.size() : "0") + 
                " salas al cliente");
            
            // Enviar la respuesta
             Evento enviarEventoACliente = new Evento ("SOLICITAR_SALAS");
            enviarEventoACliente.equals(respuesta);
            
        } catch (Exception e) {
            System.err.println("Servidor: Error al responder solicitud de salas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    
    

    /**
     * Notifica a todos los clientes conectados que se ha creado una nueva sala.
     *
     * @param sala La sala que ha sido creada.
     */
    private void notificarNuevaSala(Sala sala) {
        Evento evento = new Evento("NUEVA_SALA");
        evento.agregarDato("sala", sala);
        server.enviarEvento(evento);
    }

}
