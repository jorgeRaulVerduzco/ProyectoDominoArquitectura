/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerLocal;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Sala;
import Dominio.Tablero;
import EventoJuego.Evento;
import Negocio.ServicioControlJuego;
import Negocio.ServicioPozo;
import Server.ConversorJSON;
import Server.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
                     procesarJugada(cliente, evento);
                    break;
                case "RESPUESTA_SALAS":
                    enviarSalasDisponibles();
                    break;
                case "REGISTRO_USUARIO":
                    registrarUsuario(cliente, evento);

                    break;
                case "INICIAR_PARTIDA":
                    iniciarPartida(cliente, evento);
                    break;
                case "DATOS_TABLERO":
                    obtenerDatosTablero(cliente, evento);
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
     * Procesa el evento "JUGADA" recibido del cliente.
     * Verifica los datos y los reenvía al servidor principal.
     *
     * @param cliente El socket del cliente que envió el evento.
     * @param evento  El evento que contiene los datos de la jugada.
     */
    public void procesarJugada(Socket cliente, Evento evento) {
    try {
        // Validar que el evento sea del tipo esperado
        if (!evento.getTipo().equals("JUGADA")) {
            throw new IllegalArgumentException("Evento no válido: " + evento.getTipo());
        }

        // Obtener los datos del evento
        Ficha ficha = (Ficha) evento.obtenerDato("ficha");
        String lado = (String) evento.obtenerDato("lado");

        // Verificar si los datos son válidos
        if (ficha == null || lado == null) {
            throw new IllegalArgumentException("[ERROR] Datos incompletos en el evento.");
        }

        System.out.println("[DEBUG] Procesando jugada: Ficha " + ficha + ", Lado " + lado);

        // Construir el evento para reenviarlo al servidor principal
        Evento eventoServidor = new Evento("JUGADA");
        eventoServidor.agregarDato("ficha", ficha);
        eventoServidor.agregarDato("lado", lado);

        // Reenviar el evento al servidor principal
        server.agregarTablero(eventoServidor, cliente);

        

    } catch (Exception e) {
        System.err.println("[ERROR] Error al procesar la jugada: " + e.getMessage());
        e.printStackTrace();
    }
}


//    private void obtenerJugadoresPorSala(Socket cliente, Evento evento) {
//    // Delegar al servidor para obtener los jugadores por sala
//    Map<String, List<String>> jugadoresPorSala = server.obtenerJugadoresPorSala();
//    
//    // Crear un nuevo evento de respuesta
//    Evento respuesta = new Evento("JUGADORES_ESPERA");
//    respuesta.agregarDato("jugadoresPorSala", jugadoresPorSala);
//    
//    // Enviar la respuesta al cliente
//    server.enviarMensajeACliente(cliente, respuesta);
//}
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
            String id = (String) evento.obtenerDato("id");
            System.out.println("CREARNUEVA SALA ID ES:" + id);
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
            nuevaSala.setId(id);
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
            System.out.println("RESPUESTA SALA" + respuestaSala);
            server.agregarSala(nuevaSala, cliente);
            System.out.println("[DEBUG] Sala creada correctamente con ID: " + nuevaSala.getId());

        } catch (Exception e) {
            System.err.println("[ERROR] Error creando sala: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void obtenerDatosTablero(Socket cliente, Evento evento) {
        // Obtener los datos necesarios desde el evento
        Tablero tablero = (Tablero) evento.obtenerDato("tablero");
        List<Ficha> fichasTablero = (List<Ficha>) evento.obtenerDato("fichasTablero");
        List<Ficha> fichasPozo = (List<Ficha>) evento.obtenerDato("fichasPozo");

        // Verificar que los datos no sean null
        if (tablero == null) {
            System.err.println("Error: Tablero no válido");
            return;
        }
        if (fichasTablero == null) {
            System.err.println("Error: Lista de fichas del tablero no válida");
            return;
        }
        if (fichasPozo == null) {
            System.err.println("Error: Lista de fichas del pozo no válida");
            return;
        }

        // Mostrar los datos para depuración
        System.out.println("Datos recibidos:");
        System.out.println("Tablero: " + tablero);
        System.out.println("Fichas del tablero: " + fichasTablero);
        System.out.println("Fichas del pozo: " + fichasPozo);

        // Crear la respuesta con los datos del tablero
        Evento respuesta = new Evento("DATOS_TABLERO");
        respuesta.agregarDato("tablero", tablero);
        respuesta.agregarDato("fichasTablero", fichasTablero);
        respuesta.agregarDato("fichasPozo", fichasPozo);
server.enviarDatosTablero(tablero, fichasTablero, fichasPozo, cliente);
    }

    public void iniciarPartida(Socket cliente, Evento evento) {
        // Extraer sala del evento
        Sala sala = (Sala) evento.obtenerDato("sala");

        if (sala == null) {
            System.err.println("[ERROR] Sala es nula al iniciar partida");
            return;
        }

        try {
            // Crear nueva partida
            Partida partida = new Partida();
            partida.setId(UUID.randomUUID().toString());  // Añadir ID único
            partida.setCantJugadores(sala.getCantJugadores());
            partida.setCantFichas(calcularFichasPozo(sala));  // Método para calcular fichas del pozo
            partida.setEstado("EN_CURSO");
            partida.setJugadores(sala.getJugador());
            partida.setTablero(new Tablero());

            // Inicializar servicio de pozo
            ServicioPozo servicioPozo = new ServicioPozo(); // Usar singleton
            servicioPozo.iniciarNuevoJuego(sala.getJugador());
            partida.setPozo(servicioPozo.getPozo());

            // Actualizar sala
            sala.setPartida(partida);
            sala.setEstado("EN_JUEGO");

            // Crear evento de inicio de partida
            Evento eventoInicio = new Evento("INICIAR_PARTIDA");
            eventoInicio.agregarDato("partida", partida);
            eventoInicio.agregarDato("sala", sala);

            // Registro de inicio de partida
            System.out.println("[INFO] Partida iniciada - ID: " + partida.getId()
                    + ", Jugadores: " + sala.getJugador().size());

            // Agregar partida
            server.agregarPartida(partida, cliente);

        } catch (Exception e) {
            System.err.println("[ERROR] Error al iniciar partida: " + e.getMessage());
            e.printStackTrace();
        }
    }

// Método para calcular fichas del pozo (ejemplo)
    private int calcularFichasPozo(Sala sala) {
        int totalFichas = sala.getNumeroFichas();
        return Math.max(0, totalFichas - 27);  // 27 menos que el total de fichas
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
        System.out.println("UNIRSE SALA METODO : SALA ES:" + sala);
        Jugador jugador = (Jugador) evento.obtenerDato("jugador");
        System.out.println("UNIRSE SALA METODO : JUGADOR ES:" + jugador);

        // Verificar que la sala no sea null
        if (sala == null) {
            System.err.println("Error: Sala no válida");
            return;
        }

        // Verificar que el jugador no sea null
        if (jugador == null) {
            System.err.println("Error: Jugador no válido");
            return;
        }

        Evento respuesta = new Evento("UNIR_SALA");
        respuesta.agregarDato("jugador", jugador);
        respuesta.agregarDato("sala", sala);

        System.out.println(" SERVER COMUNICACION: CLUENTE ES:" + cliente);
        Socket socketJugador = cliente;

        System.out.println("SERVER COMUNICACION : " + jugador + "Se esta enviadno" + respuesta.getDatos());
        if (socketJugador != null) {
            server.unirseSala(respuesta, socketJugador);
        }

    }

    public Sala obtenerSalaPorId(String id) {
        try {
            Path salasPath = Paths.get("salas_multijugador.json");
            if (Files.exists(salasPath)) {
                // Leer el contenido del archivo JSON
                String json = new String(Files.readAllBytes(salasPath), StandardCharsets.UTF_8);

                // Convertir el JSON a lista de salas
                List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(json);

                // Buscar la sala por ID
                return salasCargadas.stream()
                        .filter(sala -> id.equals(sala.getId()))
                        .findFirst()
                        .orElse(null);
            }
        } catch (IOException e) {
            System.err.println("Error al buscar sala por ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
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
