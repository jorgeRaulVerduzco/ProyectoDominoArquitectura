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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author INEGI
 */
public class ServerComunicacion {
    
    ServicioControlJuego servicioC;
    private Server server;
    
    public ServerComunicacion(Server server) {
        this.server = server;
        servicioC = new ServicioControlJuego();
    }
//este es importante

    private void enviarSalasDisponibles() {
        try {
            // Obtener las salas directamente del Server
            List<Sala> salasServidor = server.obtenerSalasActivas();

            // Crear un evento con las salas disponibles
            Evento respuesta = new Evento("RESPUESTA_SALAS");
            respuesta.agregarDato("salas", salasServidor);

            // Enviar el evento a todos los clientes
            server.enviarMensajeATodosLosClientes(respuesta);
            System.out.println("Salas enviadas correctamente: " + salasServidor.size());
            
        } catch (Exception e) {
            System.err.println("Error al enviar salas disponibles: " + e.getMessage());
        }
    }
    
    public void registrarUsuario(Socket cliente, Evento eventoRegistro) {
        System.out.println("SERVER C :LLegue registrarUsuario");
        try {
            System.out.println("SERVER C : pase el try");
            // Extraer el jugador del evento
            Jugador jugador = (Jugador) eventoRegistro.obtenerDato("jugador");
            System.out.println("SERVER COMUNICACION: Socket del jugador actual" + cliente);
            // Depuración: Verificar que el jugador está correctamente extraído
            System.out.println("Jugador extraído: " + jugador);

            // Verificar si el nombre de usuario ya existe
            // Crear el jugador en el sistema
            servicioC.crearJugador(jugador);
            System.out.println("[REGISTRO] Jugador creado en el sistema: " + jugador);

            // Registrar el jugador en el servidor
            server.registrarJugador(cliente, jugador);
            System.out.println("[REGISTRO] Jugador registrado en el servidor: " + jugador);
            
        } catch (Exception e) {
            // Manejar cualquier error durante el registro
            System.err.println("Error al registrar usuario: " + e.getMessage());
            e.printStackTrace();  // Para obtener más información sobre el error

            // Enviar un evento de error al cliente
            enviarErrorRegistro(cliente, "Error interno al registrar usuario");
        }
    }
    
    private void enviarErrorRegistro(Socket cliente, String mensaje) {
        try {
            Evento errorEvento = new Evento("REGISTRO_USUARIO");
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
                    enviarSalasDisponibles();
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
    void crearNuevaSala(Socket cliente, Evento evento) {
        try {
            System.out.println("[DEBUG] Procesando evento: CREAR_SALA");

            // Validate event and its data
            if (evento == null || evento.getDatos() == null) {
                System.err.println("[ERROR] Evento o datos nulos");
                return;
            }

            // Extract data safely
            Integer numJugadores = extractIntegerSafely(evento, "numJugadores");
            Integer numFichas = extractIntegerSafely(evento, "numFichas");
            Jugador jugador = extractJugadorSafely(evento);

            // Validate extracted data
            if (numJugadores == null || numFichas == null || jugador == null) {
                System.err.println("[ERROR] Datos inválidos para crear sala");
                return;
            }

            // Create and configure new room
            Sala nuevaSala = new Sala();
            nuevaSala.setId(UUID.randomUUID().toString());
            nuevaSala.setCantJugadores(numJugadores);
            nuevaSala.setNumeroFichas(numFichas);
            nuevaSala.setEstado("ESPERANDO");

            // Add the player to the room's list of players
            List<Jugador> jugadores = new ArrayList<>();
            jugadores.add(jugador);
            nuevaSala.setJugador(jugadores);

            // Add room to game control service
            ServicioControlJuego servicioControlJuego = ServicioControlJuego.getInstance();
            servicioControlJuego.agregarSala(nuevaSala);

            // Notify clients
            Evento respuestaSala = new Evento("CREAR_SALA");
            respuestaSala.agregarDato("sala", nuevaSala);
            // SALE QUE NO TIENE ID
            System.out.println("RESPUESTA SALA"+ respuestaSala);
            server.agregarSala(nuevaSala, cliente);
            System.out.println("[DEBUG] Sala creada correctamente con ID: " + nuevaSala.getId());
            
        } catch (Exception e) {
            System.err.println("[ERROR] Error creando sala: " + e.getMessage());
            e.printStackTrace();
        }
    }

// Helper method to safely extract Integer from event
    private Integer extractIntegerSafely(Evento evento, String key) {
        Object value = evento.obtenerDato(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        System.err.println("[ERROR] Valor inválido para " + key + ": " + value);
        return null;
    }

// Helper method to safely extract Jugador from event
    private Jugador extractJugadorSafely(Evento evento) {
        Object value = evento.obtenerDato("jugador");
        if (value instanceof Jugador) {
            return (Jugador) value;
        }

        // If it's a list, try to get the first player
        if (value instanceof List) {
            List<?> lista = (List<?>) value;
            if (!lista.isEmpty() && lista.get(0) instanceof Jugador) {
                return (Jugador) lista.get(0);
            }
        }
        
        System.err.println("[ERROR] Jugador inválido: " + value);
        return null;
    }
    
    private void verificarEstadoSalas() {
        ServicioControlJuego servicioControlJuego = ServicioControlJuego.getInstance();
        
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
//    private void enviarSalasDisponibles() {
//         try {
//            // Obtener las salas directamente del Server
//            List<Sala> salasServidor = server.obtenerSalasActivas();
//
//            // Crear un evento con las salas disponibles
//            Evento respuesta = new Evento("RESPUESTA_SALAS");
//            respuesta.agregarDato("salas", salasServidor);
//
//            // Enviar el evento a todos los clientes
//            server.enviarMensajeATodosLosClientes(respuesta);
//            System.out.println("Salas enviadas correctamente: " + salasServidor.size());
//        } catch (Exception e) {
//            System.err.println("Error al enviar salas disponibles: " + e.getMessage());
//        }
//    }
    public List<Sala> enviarSalasDisponibles2(Socket cliente) {
        System.out.println("ejecutando ENVIOSALAS 222");
        try {
            ServicioControlJuego servicioControlJuego = ServicioControlJuego.getInstance();

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
                server.enviarEvento(respuesta, cliente);
            }

            // Log the rooms for debugging
            for (Sala sala : salasServidor) {
                System.out.println("Sala ID: " + sala.getId()
                        + ", Jugadores: " + sala.getJugador().size()
                        + "/" + sala.getCantJugadores()
                        + ", Fichas: " + sala.getNumeroFichas());
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
        ServicioControlJuego servicioControlJuego = ServicioControlJuego.getInstance();
        
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
            ServicioControlJuego servicioControlJuego = ServicioControlJuego.getInstance();
            
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
