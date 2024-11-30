/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Controller.Controller;
import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Negocio.ServicioControlJuego;
import ServerLocal.ServerComunicacion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author INEGI
 */
public class Server {

   private GestorSalas gestorSalas;
    private ServerSocket servidor;
    private List<Socket> clientes;
    private Map<Socket, ObjectOutputStream> outputStreams;
    private Map<Socket, Jugador> jugadoresPorSocket;
    private Controller blackboardController;
    private ServerComunicacion serverComunicacion;
    private volatile boolean running;
    private boolean isRunning;
    private boolean isConnected = false;  // Indica si el servidor está listo
    private List<Sala> salas; // Lista de salas activas
    private static List<Sala> salasActivas;
    private List<Jugador> jugadoresRegistrados;
    // Thread pool for handling connections
    private final ExecutorService executorService;

    public Server() {
        this.gestorSalas = GestorSalas.getInstance();

        this.clientes = new CopyOnWriteArrayList<>();
        this.outputStreams = new ConcurrentHashMap<>();
        this.jugadoresPorSocket = new ConcurrentHashMap<>();
        this.blackboardController = new Controller(this);
        this.serverComunicacion = new ServerComunicacion(this);
        this.running = false;
        this.isConnected = false;
        this.salasActivas = new CopyOnWriteArrayList<>();
        this.jugadoresRegistrados = new CopyOnWriteArrayList<>();
        // Initialize thread pool with core and max thread counts
        this.executorService = Executors.newCachedThreadPool(new ThreadFactory() {
        private final AtomicInteger threadCounter = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "ServerThread-" + threadCounter.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    });
    }
public static List<Sala> cargarSalas() {
  try {
        Path path = Paths.get("salas.json");
        if (Files.exists(path)) {
            String json = new String(Files.readAllBytes(path));
            List<Sala> salas = ConversorJSON.convertirJsonASalas(json);
            System.out.println("Salas cargadas correctamente desde salas.json: " + salas.size());
            return salas;
        }
    } catch (IOException e) {
        System.err.println("Error al cargar las salas: " + e.getMessage());
    }
    return new CopyOnWriteArrayList<>(); // Devuelve una lista vacía si no hay archivo o si ocurre un error
}

public void guardarSalas() {
    try {
        String json = ConversorJSON.convertirSalasAJson(salasActivas);
        Files.write(Paths.get("salas.json"), json.getBytes());
        System.out.println("Salas guardadas exitosamente: " + salasActivas.size());
    } catch (IOException e) {
        System.err.println("Error al guardar las salas: " + e.getMessage());
    }
}
    public void cerrarServidor() {
        try {
            // Detener el servidor
            running = false;
            isConnected = false;

            // Cerrar todos los sockets de clientes
            for (Socket socket : clientes) {
                cerrarConexion(socket);
            }

            // Cerrar el ServerSocket
            if (servidor != null) {
                servidor.close();
            }

            gestorSalas = GestorSalas.getInstance();
            gestorSalas.limpiarSalas();

            // Cerrar el executor service
            if (executorService != null) {
                executorService.shutdown();
            }

            System.out.println("Servidor cerrado correctamente");
        } catch (IOException e) {
            System.err.println("Error cerrando el servidor: " + e.getMessage());
        }
    }


    public static synchronized List<Sala> getSalas() {
        return new ArrayList<>(salasActivas); // Devuelve una copia para evitar modificaciones externas
    }

    // Método getSalas()
    /**
     * Devuelve la lista de salas activas en el servidor.
     *
     * @param puerto
     * @return Una lista de salas disponibles.
     * @throws java.io.IOException
     */
    public void iniciarServidor(int puerto) throws IOException {
        try {
            servidor = new ServerSocket(puerto);
            isConnected = true;
            System.out.println("Servidor iniciado en dirección IP: 127.0.0.1, puerto: " + puerto);

            // Lanzar un hilo para aceptar conexiones
            Thread acceptThread = new Thread(() -> {
                while (isConnected) {
                    try {
                        Socket clienteSocket = servidor.accept();
                        System.out.println("Cliente conectado: " + clienteSocket.getInetAddress());

                        // Delegar el manejo de la conexión a un método específico
                        manejarNuevaConexion(clienteSocket);
                    } catch (IOException e) {
                        if (isConnected) {
                            System.err.println("Error aceptando conexión: " + e.getMessage());
                        }
                    }
                }
            });
            acceptThread.start();

        } catch (IOException e) {
            isConnected = false;
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            throw e;
        }
    }

    private void aceptarConexiones() {
        while (running) {
            try {
                Socket clienteSocket = servidor.accept();
                System.out.println("[CONEXIÓN] Nueva conexión aceptada: " + clienteSocket.getInetAddress());
                // Asegúrate de registrar el socket correctamente y de no cerrarlo inmediatamente
                executorService.submit(() -> manejarNuevaConexion(clienteSocket));
            } catch (IOException e) {
                if (running) {
                    System.err.println("[ERROR] Error aceptando conexión: " + e.getMessage());
                    isConnected = false;
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }

    public void conectarSocket() throws IOException {
        Socket socket = new Socket("localhost", 51114);  // Conéctalo al puerto de tu servidor
        if (socket.isConnected()) {
            System.out.println("Conexión establecida con el servidor.");
        } else {
            System.err.println("Error: No se pudo conectar al servidor.");
        }
    }

    public void agregarSala(Sala sala) {
      gestorSalas.agregarSala(sala);
    Evento evento = new Evento("NUEVA_SALA");
    evento.agregarDato("sala", sala);
    enviarEvento(evento);
    
    // Guardar salas después de agregar
    guardarSalas();
    }

    public List<Sala> obtenerSalasActivas() {
        return gestorSalas.obtenerSalasActivas();
    }

    public static List<Sala> cargarSalas2() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sala.ser"))) {
            salasActivas = (List<Sala>) ois.readObject();
            System.out.println("Salas cargadas correctamente desde salas.ser." + salasActivas);
            return salasActivas;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar las salas: " + e.getMessage());
            salasActivas = new CopyOnWriteArrayList<>(); // Lista vacía si ocurre un error
        }
        return salasActivas;

    }

    private void manejarNuevaConexion(Socket clienteSocket) {
        try {
            // Configurar el socket para que tenga un tiempo de espera para la conexión
            clienteSocket.setSoTimeout(5000);

            ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clienteSocket.getInputStream());

            // Registrar el output stream para enviar mensajes a este cliente
            synchronized (outputStreams) {
                outputStreams.put(clienteSocket, out);
                clientes.add(clienteSocket);
            }

            // Enviar confirmación de conexión
            Evento confirmacion = new Evento("CONEXION_EXITOSA");
            enviarMensajeACliente(clienteSocket, confirmacion);

            // Iniciar un hilo para escuchar mensajes de este cliente
            executorService.submit(() -> escucharCliente(clienteSocket, in));

        } catch (IOException e) {
            System.err.println("[ERROR] Error estableciendo conexión: " + e.getMessage());
            cerrarConexion(clienteSocket);
            isConnected = false;
        }
    }

    public void registrarJugador(Socket socket, Jugador jugador) {
        synchronized (jugadoresPorSocket) {
            // Verifica si ya existe un jugador con el mismo nombre en el servidor
            for (Jugador j : jugadoresPorSocket.values()) {
                if (j.getNombre().equalsIgnoreCase(jugador.getNombre())) {
                    System.err.println("[REGISTRO] Jugador ya registrado: " + jugador.getNombre());
                    return;  // Si el jugador ya está registrado, no lo vuelvas a registrar
                }
            }

            // Si no existe, registra el jugador con su socket
            jugadoresPorSocket.put(socket, jugador);
            System.out.println("[REGISTRO] Jugador registrado en el servidor: " + jugador.getNombre());

            try {
                // Crear y registrar el ObjectOutputStream para este socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                synchronized (outputStreams) {
                    outputStreams.put(socket, out);
                    jugadoresRegistrados.add(jugador);

                    System.out.println("[REGISTRO] Stream de salida registrado para: " + jugador.getNombre());
                }
            } catch (IOException e) {
                System.err.println("[REGISTRO] Error al crear el stream de salida para: " + jugador.getNombre());
                e.printStackTrace();
            }

            // Notificar sobre el registro del nuevo jugador
            Evento nuevoJugadorEvento = new Evento("NUEVO_JUGADOR_REGISTRADO");
            nuevoJugadorEvento.agregarDato("jugador", jugador);
            enviarEvento(nuevoJugadorEvento);
        }
    }

    /**
     * Verifica si el servidor está conectado y operativo.
     *
     * @return true si el servidor está conectado, false en caso contrario.
     */
    public boolean isServidorActivo() {
        return isConnected;  // isConnected debe ser actualizado cuando el servidor esté activo
    }

    /**
     * Envía un evento a todos los clientes actualmente conectados al servidor.
     *
     * @param evento el objeto de tipo Evento que se enviará a todos los
     * clientes.
     */
    public void enviarNuevoCliente(Evento evento) {
        System.out.println("Intentando enviar evento de nuevo cliente");

        // Si no hay clientes, simplemente imprimimos un mensaje
        if (outputStreams == null || outputStreams.isEmpty()) {
            System.out.println("Primer registro de usuario. No hay clientes para notificar.");
            return;
        }

        // Resto del método de envío a clientes existentes
        synchronized (outputStreams) {
            List<Socket> clientesDesconectados = new ArrayList<>();

            for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
                Socket cliente = entry.getKey();
                ObjectOutputStream out = entry.getValue();

                try {
                    synchronized (out) {
                        out.writeObject(evento);
                        out.reset();
                        out.flush();
                    }

                    System.out.println("Evento enviado exitosamente a: " + cliente.getInetAddress());
                } catch (IOException e) {
                    System.err.println("Error enviando evento a " + cliente.getInetAddress() + ": " + e.getMessage());
                    clientesDesconectados.add(cliente);
                }

                System.out.println("Exitoso. Tamaño: " + outputStreams.size());
            }

            // Limpiar clientes desconectados
            for (Socket socket : clientesDesconectados) {
                cerrarConexion(socket);
            }
        }

    }

    /**
     * Escucha continuamente los mensajes enviados por un cliente a través de su
     * conexión socket. Los mensajes son leídos como objetos Evento y procesados
     * según su tipo. Si ocurre un error durante la lectura o el cliente se
     * desconecta, la conexión se cierra.
     *
     * @param cliente el socket que representa la conexión con el cliente.
     * @param in el flujo de entrada asociado al cliente, desde el cual se
     * leerán los objetos.
     */
    private void escucharCliente(Socket cliente, ObjectInputStream in) {
        try {
            while (isRunning) {
                Evento evento = (Evento) in.readObject();
                procesarEvento(cliente, evento);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error leyendo evento del cliente: " + e.getMessage());
            cerrarConexion(cliente);
        }
    }

    /**
     * Maneja la comunicación con un cliente. Escucha eventos enviados por el
     * cliente y los procesa.
     *
     * @param cliente El socket del cliente.
     */
    /**
     * Devuelve el controlador del servidor.
     *
     * @return El controlador blackboardController.
     */
    public Controller getController() {
        return this.blackboardController;
    }

    /**
     * Obtiene el socket asociado a un jugador dado.
     *
     * @param jugador El jugador cuyo socket se desea obtener.
     * @return El socket asociado al jugador, o null si no existe.
     */
    public Socket getSocketJugador(Jugador jugador) {
        // Cambiar la implementación para manejar casos donde no se encuentra el socket
        return jugadoresPorSocket.entrySet().stream()
                .filter(entry -> entry.getValue().equals(jugador))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);  // Cambiar .get() por .orElse(null)
    }

    /**
     * Verifica si un jugador con el nombre dado ya está registrado en el
     * servidor.
     *
     * @param nombre El nombre del jugador a buscar.
     * @return true si el jugador ya está registrado, false en caso contrario.
     */
    public boolean contieneJugador(String nombre) {
        synchronized (jugadoresPorSocket) {
            // Verificar si algún jugador con el mismo nombre ya está registrado
            return jugadoresPorSocket.values().stream()
                    .anyMatch(jugador -> jugador.getNombre().equalsIgnoreCase(nombre)); // Comparar solo por nombre
        }
    }

    /**
     * Envía un evento a todos los clientes conectados.
     *
     * @param evento El evento a enviar.
     */
    public void enviarEvento(Evento evento) {
        System.out.println("Iniciando envío de evento: " + evento.getTipo());

        List<Socket> clientesDesconectados = new ArrayList<>();

        synchronized (outputStreams) {
            System.out.println("synchronized (outputStreams)");

            for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
                System.out.println("llegue al for");
                Socket cliente = entry.getKey();
                ObjectOutputStream out = entry.getValue();
                ServicioControlJuego servicioControlJuego = ServicioControlJuego.getInstance();

                try {
                    synchronized (out) {
                        System.out.println("Preparando para escribir en el cliente: " + cliente.getInetAddress());

                        out.writeObject(evento);

                        System.out.println("out.flush();");
                        out.flush();
                    }

                    System.out.println("Evento enviado exitosamente a: " + cliente.getInetAddress());
                } catch (IOException e) {
                    System.err.println("Error enviando evento a " + cliente.getInetAddress() + ": " + e.getMessage());
                    clientesDesconectados.add(cliente);
                }
            }

        }
        System.out.println("SALTO DE CERRAR CONEXION");
//        // Limpiar clientes desconectados
        for (Socket socket : clientesDesconectados) {
            cerrarConexion(socket);
        }
    }

    /**
     * Envía un evento a un jugador específico.
     *
     * @param jugador El jugador que recibirá el evento.
     * @param evento El evento a enviar.
     */
    public void enviarEventoAJugador(Jugador jugador, Evento evento) {

        Socket socket = getSocketJugador(jugador);
        if (socket != null) {
            enviarMensajeACliente(socket, evento);
        }
    }

    public void enviarEventoATodos(Evento evento) {
        System.out.println("enviarEventoATodos");
        for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
            enviarMensajeACliente(entry.getKey(), evento);
            System.out.println("SE ENVIO");
        }
    }

    /**
     * Envía un mensaje a un cliente específico.
     *
     * @param cliente El socket del cliente.
     * @param mensaje El mensaje a enviar.
     */
    public void enviarMensajeACliente(Socket cliente, Evento mensaje) {
        try {
            ObjectOutputStream out = outputStreams.get(cliente);
            if (out != null) {
                synchronized (out) {
                    out.writeObject(mensaje);
                    out.reset();  // Importante para evitar problemas de caché
                    out.flush();
                }
                System.out.println("Mensaje enviado exitosamente: " + mensaje.getTipo());
                procesarEvento(cliente, mensaje);
            } else {
                System.err.println("No se encontró stream de salida para el cliente");
            }
        } catch (IOException e) {
            System.err.println("Error enviando mensaje: " + e.getMessage());
            cerrarConexion(cliente);
        }
    }

    public void enviarMensajeATodosLosClientes(Evento mensaje) {
        // Creamos una copia de los clientes para evitar modificaciones concurrentes
        Set<Socket> clientes = new HashSet<>(outputStreams.keySet());

        for (Socket cliente : clientes) {
            try {
                ObjectOutputStream out = outputStreams.get(cliente);
                if (out != null) {
                    synchronized (out) {
                        out.writeObject(mensaje);
                        out.reset();  // Importante para evitar problemas de caché
                        out.flush();
                    }
                    System.out.println("Mensaje enviado exitosamente a cliente: " + cliente.getInetAddress() + " - " + mensaje.getTipo());
                    procesarEvento(cliente, mensaje);
                } else {
                    System.err.println("No se encontró stream de salida para el cliente: " + cliente.getInetAddress());
                }
            } catch (IOException e) {
                System.err.println("Error enviando mensaje al cliente " + cliente.getInetAddress() + ": " + e.getMessage());
                cerrarConexion(cliente);
            }
        }
    }

    /**
     * Maneja los errores de comunicación con los clientes. Muestra un mensaje
     * de error en la consola.
     */
    public void manejarErrorComunicacion() {
        System.err.println("Error en la comunicación");
    }

    /**
     * Cierra la conexión con un cliente específico. Elimina al cliente de las
     * estructuras de datos y notifica al controlador sobre la desconexión del
     * jugador.
     *
     * @param cliente El socket del cliente a desconectar.
     */
    public void cerrarConexion(Socket cliente) {
        try {
            cliente.close();
            clientes.remove(cliente);
            outputStreams.remove(cliente);
            Jugador jugador = jugadoresPorSocket.remove(cliente);
            if (jugador != null) {
                blackboardController.procesarDesconexion(jugador);
            }
        } catch (IOException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }

    /**
     * Procesa un evento recibido desde un cliente y lo delega al componente
     * correspondiente según el tipo de evento. Este método actúa como un
     * controlador central para manejar distintos tipos de eventos que pueden
     * generarse en el sistema.
     *
     * @param cliente el socket del cliente que envió el evento. Este parámetro
     * identifica la conexión desde la cual se originó el evento.
     * @param evento el objeto Evento que contiene el tipo y los datos asociados
     * al evento que debe ser procesado.
     */
    private void procesarEvento(Socket cliente, Evento evento) {
        System.out.println("[DEBUG] Procesando evento en Server: " + evento.getTipo());
        System.out.println("[DEBUG] Cliente: " + cliente);
        System.out.println("[DEBUG] Evento detalles: " + evento.getDatos());

        try {
            switch (evento.getTipo()) {
                case "CREAR_SALA":
                    System.out.println("[DEBUG] Delegando evento CREAR_SALA a serverComunicacion");
                    serverComunicacion.procesarEvento(cliente, evento);
                    break;
                case "RESPUESTA_SALAS":
                    serverComunicacion.procesarEvento(cliente, evento);
                    break;
                case "UNIRSE_SALA":
                case "ABANDONAR_SALA":
                case "JUGADA":
                    blackboardController.procesarEvento(cliente, evento);
                    break;
                case "REGISTRO_USUARIO":
                    System.out.println("[DEBUG] Recibido evento REGISTRO_USUARIO");
                    serverComunicacion.procesarEvento(cliente, evento);
                    break;

                default:
                    System.out.println("Evento no reconocido: " + evento.getTipo());
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Error procesando evento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void solicitarSalas() {
        Evento solicitud = new Evento("RESPUESTA_SALAS");
        enviarEvento(solicitud);
    }
}
