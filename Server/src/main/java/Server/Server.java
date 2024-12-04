/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import BlackBoard.BlackBoard;
import Controller.Controller;
import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import ServerLocal.ServerComunicacion;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
    private BlackBoard blackBoard;
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
        salas = new CopyOnWriteArrayList<>();
        this.blackBoard = new BlackBoard(this);
        this.serverComunicacion = new ServerComunicacion(this);
        this.running = false;
        this.isConnected = false;
        this.salasActivas = new CopyOnWriteArrayList<>();
        this.jugadoresRegistrados = new CopyOnWriteArrayList<>();
        cargarDatosMultijugador();
        cargarSalasMultijugador();
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

    public void guardarSalas() {
        try {
            // Primero, leer las salas existentes si el archivo ya existe
            List<Sala> salasExistentes = new ArrayList<>();
            Path salasPath = Paths.get("salas_multijugador.json");

            if (Files.exists(salasPath)) {
                String jsonExistente = new String(Files.readAllBytes(salasPath), StandardCharsets.UTF_8);
                if (!jsonExistente.trim().isEmpty()) {
                    salasExistentes = ConversorJSON.convertirJsonASalas(jsonExistente);
                }
            }

            // Crear un nuevo conjunto de salas que incluya las existentes y las nuevas
            Set<Sala> conjuntoSalas = new LinkedHashSet<>(salasExistentes);
            conjuntoSalas.addAll(salas);

            // Convertir el conjunto de salas a una lista para mantener el orden
            List<Sala> salasFinales = new ArrayList<>(conjuntoSalas);

            // Guardar todas las salas
            String json = ConversorJSON.convertirSalasAJson(salasFinales);

            try (FileWriter writer = new FileWriter("salas_multijugador.json")) {
                writer.write(json);
                System.out.println("Salas guardadas exitosamente: " + salasFinales.size());
            }
        } catch (IOException e) {
            System.err.println("Error al guardar las salas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Sala> cargarSalasMultijugador() {
        try {
            Path salasPath = Paths.get("salas_multijugador.json");

            if (Files.exists(salasPath)) {
                // Leer todo el contenido del archivo
                String json = new String(Files.readAllBytes(salasPath), StandardCharsets.UTF_8);

                // Convertir JSON a lista de salas
                List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(json);

                // Limpiar lista actual y agregar salas cargadas
                salas.clear();
                salas.addAll(salasCargadas);

                System.out.println("Salas cargadas: " + salasCargadas.size());
                return salasCargadas;
            } else {
                System.out.println("No se encontró archivo de salas. Iniciando con lista vacía.");
                return new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("Error al cargar salas: " + e.getMessage());
            return new ArrayList<>();
        }

    }

// Método para obtener salas como lista JSON para enviar a clientes
    public String obtenerSalasComoJSON() {
        return ConversorJSON.convertirSalasAJson(salas);
    }

    // Método para registrar jugadores en el servidor desde BlackBoard
    public void registrarJugadores(Map<String, Jugador> jugadoresBlackBoard) {
        System.out.println("SERBER  : Socket del jugador actual" + jugadoresBlackBoard);
        // Agregar los jugadores de BlackBoard a la lista de jugadores del servidor
        jugadoresRegistrados.addAll(jugadoresBlackBoard.values());

        // Imprimir el número de jugadores registrados
        System.out.println("Jugadores registrados en el servidor: " + jugadoresRegistrados.size());
    }

    public void registrarSalas(Map<String, Sala> salasBlackBoard) {
        System.out.println("SERBER  : Socket del jugador actual" + salasBlackBoard);

        // Opción 1: Limpiar la lista antes de agregar
        salas.clear();
        salas.addAll(salasBlackBoard.values());

        // Opción 2 (alternativa): Reemplazar completamente la lista
        // salas = new ArrayList<>(salasBlackBoard.values());
        System.out.println("salas registrados en el servidor: " + salas.size());
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

    public void cerrarServidor() {
        try {
            // Detener el servidor
            running = false;
            isConnected = false;

            // Cerrar todos los sockets de clientes
            for (Socket socket : clientes) {
//                cerrarConexion(socket);
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

    public Map<Socket, Jugador> getJugadoresPorSocket() {
        return jugadoresPorSocket;
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

    public void unirseSala(Evento evento, Socket clienteSocket) {
        try {
            // Verificar si la sala fue enviada en el evento
            Sala sala = (Sala) evento.obtenerDato("sala");
            if (sala == null) {
                // Intentar recuperar la sala desde el archivo JSON usando el ID
                String salaId = (String) evento.obtenerDato("id");
                if (salaId == null || salaId.isEmpty()) {
                    throw new IllegalArgumentException("El ID de la sala no puede ser nulo o vacío.");
                }

                Path salasPath = Paths.get("salas_multijugador.json");
                if (Files.exists(salasPath)) {
                    String jsonSalas = Files.readString(salasPath, StandardCharsets.UTF_8);
                    List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(jsonSalas);
                    sala = salasCargadas.stream()
                            .filter(s -> salaId.equals(s.getId()))
                            .findFirst()
                            .orElse(null);
                }
            }

            if (sala == null) {
                throw new IllegalArgumentException("La sala no es válida o no existe.");
            }

            // Validar el jugador
            Jugador jugador = jugadoresPorSocket.get(clienteSocket);
            if (jugador == null) {
                throw new IllegalStateException("No se encontró un jugador asociado al socket.");
            }

            // Enviar evento al BlackBoard
            System.out.println("Sala encontrada: " + sala.getId() + ". Enviando evento al BlackBoard.");
            evento.agregarDato("sala", sala);
            evento.agregarDato("jugador", jugador);
            blackBoard.enviarEventoBlackBoard(clienteSocket, evento);
        } catch (Exception e) {
            System.err.println("Error en unirseSala: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void agregarSala(Sala sala, Socket socket) {
        gestorSalas.agregarSala(sala);
        Evento evento = new Evento("CREAR_SALA");
        evento.agregarDato("sala", sala);
        System.out.println("DATOS DEL EVENTO DE AGREGAR SALAS");
        System.out.println(evento.getDatos());
        System.out.println("SI SE GUARDAN LOS DATOS DEL EVENTO");
        System.out.println("PARA CALARLE AGREGAR SALA");
        //enviarEvento(evento, socket);
        Controller controller = new Controller(this);
        blackBoard.setController(controller);
        blackBoard.enviarEventoBlackBoard(socket, evento);
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
//            cerrarConexion(clienteSocket);
            isConnected = false;
        }
    }

    public void persistirDatosMultijugador() {
        try {
            // Persistir jugadores
            String jsonJugadores = ConversorJSON.convertirJugadoresAJson(jugadoresRegistrados);
            Files.write(Paths.get("jugadores_multijugador.json"),
                    jsonJugadores.getBytes(StandardCharsets.UTF_8));

            // Persistir salas
            String jsonSalas = ConversorJSON.convertirSalasAJson(salas);
            Files.write(Paths.get("salas_multijugador.json"),
                    jsonSalas.getBytes(StandardCharsets.UTF_8));

            System.out.println("Datos de jugadores y salas persistidos exitosamente.");
        } catch (IOException e) {
            System.err.println("Error al persistir datos de multijugador: " + e.getMessage());
        }
    }

    public void cargarDatosMultijugador() {
        try {
            // Ensure jugadoresRegistrados is initialized
            if (jugadoresRegistrados == null) {
                jugadoresRegistrados = new CopyOnWriteArrayList<>();
            }

            // Cargar jugadores
            Path jugadoresPath = Paths.get("jugadores_multijugador.json");
            if (Files.exists(jugadoresPath)) {
                String jsonJugadores = Files.readString(jugadoresPath, StandardCharsets.UTF_8);
                List<Jugador> jugadoresCargados = ConversorJSON.convertirJsonAJugadores(jsonJugadores);

                // Clear and add loaded players
                jugadoresRegistrados.clear();
                jugadoresRegistrados.addAll(jugadoresCargados);

                System.out.println("Jugadores cargados: " + jugadoresCargados.size());
            }
            // Cargar salas
            Path salasPath = Paths.get("salas_multijugador.json");
            if (Files.exists(salasPath)) {
                String jsonSalas = Files.readString(salasPath, StandardCharsets.UTF_8);
                List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(jsonSalas);

                // Limpiar lista actual y agregar salas cargadas
                salas.clear();
                salas.addAll(salasCargadas);

                System.out.println("Salas cargadas: " + salasCargadas.size());
            }
        } catch (IOException e) {
            System.err.println("Error al cargar datos de multijugador: " + e.getMessage());
        }
    }

    public List<Jugador> cargarClientesExistentes() {
        List<Jugador> clientes = new ArrayList<>();

        try {
            Path filePath = Path.of("clientes.json");
            if (Files.exists(filePath)) {
                // Lee el contenido del archivo JSON
                String json = Files.readString(filePath, StandardCharsets.UTF_8);

                // Convierte el JSON en una lista de jugadores
                clientes = ConversorJSON.convertirJsonAJugadores(json);

                // Add null check
                if (clientes == null) {
                    clientes = new ArrayList<>();
                    System.out.println("[CARGA] No se pudieron cargar los clientes. Inicializando lista vacía.");
                } else {
                    System.out.println("[CARGA] Clientes cargados correctamente: " + clientes.size());
                }
            } else {
                System.out.println("[CARGA] No se encontró el archivo de clientes. Se creará uno nuevo.");
            }
        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo cargar el archivo de clientes: " + e.getMessage());
            clientes = new ArrayList<>();
        }

        return clientes;
    }

    public void registrarJugador(Socket socket, Jugador jugador) {
        synchronized (jugadoresPorSocket) {
            // Verifica si el cliente ya está registrado

            clientes.add(socket);
            jugadoresPorSocket.put(socket, jugador);

            System.out.println("[REGISTRO] Nuevo cliente registrado: " + socket);

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
                return; // Si falla aquí, no intentamos persistir
            }

            // Persistir la lista de clientes después de un registro exitoso
            persistirDatosMultijugador();
            // Enviar eventro al BlackBoard
            System.out.println("[BLACKBOARD] Enviando evento de registro.");
            Evento nuevoJugadorEvento = new Evento("REGISTRO_USUARIO");
            nuevoJugadorEvento.agregarDato("jugador", jugador);

            Controller controller = new Controller(this);
            blackBoard.setController(controller);

            blackBoard.enviarEventoBlackBoard(socket, nuevoJugadorEvento);
        }
    }

    public void debugJugadoresCargados() {
        System.out.println("=== Jugadores Recuperados ===");
        for (Map.Entry<Socket, Jugador> entry : jugadoresPorSocket.entrySet()) {
            System.out.println("Socket: " + entry.getKey() + " -> Jugador: " + entry.getValue().getNombre());
        }
        System.out.println("=============================");
    }

    public void cargarClientesPersistidos() {
        try {
            Path filePath = Path.of("clientes.json");

            // Log para verificar la ubicación de lectura
            System.out.println("[CARGA] Intentando cargar clientes desde: " + filePath.toAbsolutePath());

            if (Files.exists(filePath)) {
                String json = Files.readString(filePath);
                List<Socket> clientesCargados = ConversorJSON.convertirJsonASockets(json);
                clientes.addAll(clientesCargados);
                System.out.println("[CARGA] Clientes cargados correctamente.");
            } else {
                System.out.println("[CARGA] No se encontró el archivo de clientes persistidos.");
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Error al cargar los clientes persistidos: " + e.getMessage());
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
//                cerrarConexion(socket);
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
//            cerrarConexion(cliente);
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
//    public Controller getController() {
//        return this.blackboardController;
//    }
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

    public void enviarMensajeATodosLosClientes(String mensaje) {
        System.out.println("Se envio el mensaje a todos los clientes");
        // Iterar sobre todos los clientes conectados (suponiendo que 'clientes' es una lista de sockets)
        System.out.println("cliente socket:" + clientes);
        for (Socket clienteSocket : clientes) {
            try {
                // Obtener el OutputStream del socket del cliente
                PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
                // Enviar el mensaje como texto
                out.println(mensaje);

                System.out.println("Clientes conectados" + clientes.size());
            } catch (IOException e) {
                System.err.println("Error al enviar mensaje al cliente: " + e.getMessage());
            }
        }
    }

    /**
     * Envía un evento a todos los clientes conectados.
     *
     * @param evento El evento a enviar.
     * @param socketCliente
     */
    public void enviarEvento(Evento evento, Socket socketCliente) {
        System.out.println("Iniciando envío de evento: " + evento.getTipo());

        List<Socket> clientesDesconectados = new ArrayList<>();

        synchronized (outputStreams) {
            System.out.println("synchronized (outputStreams)" + outputStreams);

            // Si el socket proporcionado está en el mapa de outputStreams, solo enviamos el evento a ese cliente
            if (outputStreams.containsKey(socketCliente)) {
                System.out.println("Enviando evento al cliente: " + socketCliente.getInetAddress());
                ObjectOutputStream out = outputStreams.get(socketCliente);

                try {
                    synchronized (out) {
                        System.out.println("Preparando para escribir en el cliente: " + socketCliente.getInetAddress());

                        // Enviamos el evento al cliente específico
                        out.writeObject(evento);

                        System.out.println("out.flush();");
                        out.flush();
                    }

                    System.out.println("Evento enviado exitosamente a: " + socketCliente.getInetAddress());
                } catch (IOException e) {
                    System.err.println("Error enviando evento a " + socketCliente.getInetAddress() + ": " + e.getMessage());
                    clientesDesconectados.add(socketCliente);
                }
            } else {
                System.err.println("El cliente no está conectado: " + socketCliente.getInetAddress());
            }
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
//            cerrarConexion(cliente);
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
//                cerrarConexion(cliente);
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
//    public void cerrarConexion(Socket cliente) {
//        try {
//            cliente.close();
//            clientes.remove(cliente);
//            outputStreams.remove(cliente);
//            Jugador jugador = jugadoresPorSocket.remove(cliente);
//            if (jugador != null) {
//                blackboardController.procesarDesconexion(jugador);
//            }
//        } catch (IOException e) {
//            System.err.println("Error al cerrar la conexión: " + e.getMessage());
//        }
//    }
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
//                    blackboardController.procesarEvento(cliente, evento);
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

    public List<Jugador> getUsuariosConectados() {
        // Devolver una lista con los jugadores actualmente conectados
        synchronized (jugadoresPorSocket) {
            return new ArrayList<>(jugadoresPorSocket.values());
        }
    }

    public Jugador getJugadorConectado() {
        // Devolver un solo jugador (puedes elegir el primero o el que desees)
        synchronized (jugadoresPorSocket) {
            if (!jugadoresPorSocket.isEmpty()) {
                return jugadoresPorSocket.values().iterator().next(); // Obtiene el primer jugador
            }
        }
        return null; // Retorna null si no hay jugadores conectados
    }

    public List<Socket> getClientes() {
        return clientes;
    }

    public void setClientes(List<Socket> clientes) {
        this.clientes = clientes;
    }

    public void solicitarSalas(Socket socket) {
        Evento solicitud = new Evento("RESPUESTA_SALAS");
        enviarEvento(solicitud, socket);
    }

    public void agregarSocketAJugador(Socket socket, Jugador jugador) {
        // Verificación de parámetros nulos
        if (socket == null || jugador == null) {
            System.err.println("[ERROR] Socket o jugador nulo");
            return;
        }

        try {
            // Sincronización para operaciones concurrentes seguras
            synchronized (jugadoresPorSocket) {
                // Añadir el socket al mapa de jugadores por socket
                jugadoresPorSocket.put(socket, jugador);

                // Opcional: añadir a la lista de clientes si no está ya presente
                if (!clientes.contains(socket)) {
                    clientes.add(socket);
                }

                // Configurar stream de salida para este socket
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                outputStreams.put(socket, out);

                System.out.println("[REGISTRO] Socket agregado para jugador: " + jugador.getNombre());
            }

        } catch (IOException e) {
            System.err.println("[ERROR] No se pudo agregar socket para jugador " + jugador.getNombre() + ": " + e.getMessage());
        }
    }

    public void actualizarSala(Sala salaActualizada) {
        try {
            Path salasPath = Paths.get("salas_multijugador.json");
            List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(Files.readString(salasPath, StandardCharsets.UTF_8));

            boolean salaEncontrada = false;
            for (int i = 0; i < salasCargadas.size(); i++) {
                if (salasCargadas.get(i).getId().equals(salaActualizada.getId())) {
                    salasCargadas.set(i, salaActualizada);
                    salaEncontrada = true;
                    break;
                }
            }

            if (salaEncontrada) {
                String jsonActualizado = ConversorJSON.convertirSalasAJson(salasCargadas);
                Files.writeString(salasPath, jsonActualizado, StandardCharsets.UTF_8);
                System.out.println("JSON actualizado correctamente: " + jsonActualizado);
            } else {
                System.err.println("No se encontró la sala con ID: " + salaActualizada.getId());
            }
        } catch (IOException e) {
            System.err.println("Error al actualizar la sala: " + e.getMessage());
            e.printStackTrace();
        }
    }

 public List<String> obtenerJugadoresPorIdSala(String idSala) {
    try {
        // Ruta del archivo JSON que contiene las salas
        Path salasPath = Paths.get("salas_multijugador.json");

        // Verificar que el archivo exista
        if (Files.exists(salasPath)) {
            // Leer el contenido del archivo
            String json = Files.readString(salasPath, StandardCharsets.UTF_8);

            // Convertir el JSON a una lista de objetos Sala
            List<Sala> salasCargadas = ConversorJSON.convertirJsonASalas(json);

            // Buscar la sala por ID
            for (Sala sala : salasCargadas) {
                if (sala.getId().equals(idSala)) {
                    // Retornar los nombres de los jugadores en la sala
                    return sala.getJugador().stream()
                            .map(Jugador::getNombre)
                            .collect(Collectors.toList());
                }
            }
        } else {
            System.err.println("El archivo salas_multijugador.json no existe.");
        }
    } catch (IOException e) {
        System.err.println("Error al leer el archivo JSON: " + e.getMessage());
    } catch (Exception e) {
        System.err.println("Error al procesar las salas: " + e.getMessage());
    }

    // Si no se encuentra la sala, retornar una lista vacía
    return new ArrayList<>();
}
}
