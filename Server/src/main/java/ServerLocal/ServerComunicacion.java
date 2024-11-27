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
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class ServerComunicacion {

    private Server server;
    private ServicioControlJuego servicioControlJuego;

    public ServerComunicacion(Server server) {
        this.server = server;
        this.servicioControlJuego = new ServicioControlJuego();
    }
//este es importante

    public void registrarUsuario(Socket cliente, Evento eventoRegistro) {
        try {
            // Extraer el jugador del evento
            Jugador jugador = (Jugador) eventoRegistro.obtenerDato("jugador");

            // Verificar si el nombre de usuario ya existe
            if (server.contieneJugador(jugador.getNombre())) {
                // Si el nombre ya existe, enviar un evento de error
                Evento errorEvento = new Evento("REGISTRO_USUARIO_ERROR");
                errorEvento.agregarDato("mensaje", "El nombre de usuario ya está en uso");
                server.enviarMensajeACliente(cliente, errorEvento);
                return;
            }

            // Registrar el jugador en el servidor
            server.registrarJugador(cliente, jugador);

//        // Crear un evento de confirmación de registro
//        Evento confirmacionEvento = new Evento("REGISTRO_USUARIO");
//        confirmacionEvento.agregarDato("jugador", jugador);
//        server.enviarMensajeACliente(cliente, confirmacionEvento);
//
//        // Notificar a todos los clientes sobre el nuevo jugador (opcional)
//        Evento nuevoJugadorEvento = new Evento("REGISTRO_USUARIO");
//        nuevoJugadorEvento.agregarDato("jugador", jugador);
//        server.enviarEventoATodos(nuevoJugadorEvento);
        } catch (Exception e) {
            // Manejar cualquier error durante el registro
            System.err.println("Error al registrar usuario: " + e.getMessage());
            enviarErrorRegistro(cliente, "Error interno al registrar usuario");
        }
    }

    private void enviarErrorRegistro(Socket cliente, String mensaje) {
        try {
            Evento errorEvento = new Evento("REGISTRO_USUARIO_ERROR");
            errorEvento.agregarDato("mensaje", mensaje);
            server.enviarMensajeACliente(cliente, errorEvento);
        } catch (Exception e) {
            System.err.println("[ERROR] No se pudo enviar mensaje de error: " + e.getMessage());
        }
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
                    System.out.println("Servidor: Iniciando proceso de creación de sala...");
                    crearNuevaSala(cliente, evento);
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
               case "RESPUESTA_SALAS":
    enviarSalasDisponibles(cliente);
    break;
                case "REGISTRO_USUARIO":
                    registrarUsuario(cliente, evento);

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
            // Validación de datos
            if (!evento.getDatos().containsKey("numJugadores")
                    || !evento.getDatos().containsKey("numFichas")
                    || !evento.getDatos().containsKey("jugador")) {
                System.err.println("Error: Datos incompletos para crear sala");
                return;
            }

            int numJugadores = (int) evento.obtenerDato("numJugadores");
            int numFichas = (int) evento.obtenerDato("numFichas");
            Jugador creador = (Jugador) evento.obtenerDato("jugador");

            // Validaciones adicionales
            if (numJugadores <= 0 || numFichas <= 0 || creador == null) {
                System.err.println("Error: Datos inválidos para crear sala");
                return;
            }

            // Crear y configurar nueva sala
            Sala nuevaSala = new Sala();
            nuevaSala.setCantJugadores(numJugadores);
            nuevaSala.setNumeroFichas(numFichas);
            nuevaSala.setEstado("ESPERANDO");
            nuevaSala.getJugador().add(creador);

            // Agregar sala al servicio
            servicioControlJuego.agregarSala(nuevaSala);

            System.out.println("Servidor: Sala creada exitosamente:");
            System.out.println("  - ID: " + nuevaSala.getId());
            System.out.println("  - Jugadores: " + nuevaSala.getJugador().size() + "/" + nuevaSala.getCantJugadores());
            System.out.println("  - Fichas: " + nuevaSala.getNumeroFichas());

            // Enviar confirmación al creador
            Evento confirmacion = new Evento("SALA_CREADA");
            confirmacion.agregarDato("sala", nuevaSala);
            server.enviarMensajeACliente(cliente, confirmacion);

            // Notificar a todos los clientes
            Evento notificacion = new Evento("NUEVA_SALA");
            notificacion.agregarDato("sala", nuevaSala);
            server.enviarEventoATodos(notificacion);

            // Verificar estado del sistema
            verificarEstadoSalas();

        } catch (Exception e) {
            System.err.println("Error crítico creando sala: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void verificarEstadoSalas() {
        List<Sala> salasDisponibles = servicioControlJuego.getSalasDisponibles();
        System.out.println("Estado actual del sistema:");
        System.out.println("Total de salas: " + salasDisponibles.size());
        for (Sala sala : salasDisponibles) {
            System.out.println("  Sala " + sala.getId() + ":");
            System.out.println("    - Estado: " + sala.getEstado());
            System.out.println("    - Jugadores: " + sala.getJugador().size() + "/" + sala.getCantJugadores());
            System.out.println("    - Fichas: " + sala.getNumeroFichas());
        }
    }
    


    /**
     * mucho texto pero ese envia la lista de salas disponibles a un cliente
     * específico o a todos los clientes conectados. Este método consulta las
     * salas disponibles a través del servicio de control de juego y crea un
     * evento con esta información para enviarlo al cliente(s).
     *
     * @param cliente el socket del cliente al que se debe enviar la respuesta.
     * Si es null, el evento se enviará a todos los clientes conectados.
     */
    private void enviarSalasDisponibles(Socket cliente) {
    try {
        // Get available rooms from the server
        List<Sala> salasServidor = servicioControlJuego.getSalasDisponibles();
        
        System.out.println("Salas disponibles: " + salasServidor.size());
        
        // Create an event with available rooms
        Evento respuesta = new Evento("RESPUESTA_SALAS");
        respuesta.agregarDato("salas", salasServidor);
        
        // Send the rooms to the specific client
        if (cliente != null) {
            server.enviarMensajeACliente(cliente, respuesta);
        } else {
            // If no specific client, send to all connected clients
            server.enviarEvento(respuesta);
        }
        
        // Log the rooms for debugging
        for (Sala sala : salasServidor) {
            System.out.println("Sala ID: " + sala.getId() + 
                               ", Jugadores: " + sala.getJugador().size() + 
                               "/" + sala.getCantJugadores() + 
                               ", Fichas: " + sala.getNumeroFichas());
        }
    } catch (Exception e) {
        System.err.println("Error al enviar salas disponibles: " + e.getMessage());
    }
}
  public List<Sala> enviarSalasDisponibles2(Socket cliente) {
    try {
        // Get available rooms from the server
        List<Sala> salasServidor = servicioControlJuego.getSalasDisponibles();
        
        System.out.println("Salas disponibles: " + salasServidor.size());
        
        // Create an event with available rooms
        Evento respuesta = new Evento("RESPUESTA_SALAS");
        respuesta.agregarDato("salas", salasServidor);
        
        // Send the rooms to the specific client
        if (cliente != null) {
            server.enviarMensajeACliente(cliente, respuesta);
        } else {
            // If no specific client, send to all connected clients
            server.enviarEvento(respuesta);
        }
      
        // Log the rooms for debugging
        for (Sala sala : salasServidor) {
            System.out.println("Sala ID: " + sala.getId() + 
                               ", Jugadores: " + sala.getJugador().size() + 
                               "/" + sala.getCantJugadores() + 
                               ", Fichas: " + sala.getNumeroFichas());
        }
            return salasServidor;
    } catch (Exception e) {
        System.err.println("Error al enviar salas disponibles: " + e.getMessage());
    }
      return null;
}

// Método auxiliar para convertir las salas desde el evento
public List<Sala> convertirSalasDesdeEvento(Evento evento) {
    List<Sala> salas = new ArrayList<>();
    Object datosSalas = evento.getDatos().get("salas");
    
    if (datosSalas instanceof List<?>) {
        for (Object obj : (List<?>) datosSalas) {
            if (obj instanceof Sala) {
                salas.add((Sala) obj);
            } else {
                System.err.println("Error: Objeto no es de tipo Sala: " + obj);
            }
        }
    } else {
        System.err.println("Error: 'salas' no es una lista.");
    }
    
    return salas;
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

            System.out.println("Servidor: Enviando "
                    + (salasDisponibles != null ? salasDisponibles.size() : "0")
                    + " salas al cliente");

            // Enviar la respuesta
            Evento enviarEventoACliente = new Evento("SOLICITAR_SALAS");
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

    private static void CrearElJugadorFinal(String nombreJugador, int socketNumero) {
        try (Socket socket = new Socket("localhost", socketNumero); ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream()); ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println("[CLIENTE] Conectado al servidor.");

            // Enviar un evento de registro de usuario
            Evento eventoRegistro = new Evento("REGISTRO_USUARIO");
            eventoRegistro.agregarDato("jugador", new Jugador(nombreJugador));
            out.writeObject(eventoRegistro);
            out.flush();

            System.out.println("[CLIENTE] Evento de registro enviado: " + eventoRegistro);

            // Leer la respuesta del servidor
            Evento respuesta = (Evento) in.readObject();
            System.out.println("[CLIENTE] Respuesta recibida: " + respuesta.getTipo());
        } catch (Exception e) {
            System.err.println("[ERROR] Error en el cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
