/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Controller.Controller;
import Dominio.Jugador;
import EventoJuego.Evento;
import ServerLocal.ServerComunicacion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author INEGI
 */
public class Server {

    private ServerSocket servidor;
    private List<Socket> clientes;
    private Map<Socket, ObjectOutputStream> outputStreams;
    private Map<Socket, Jugador> jugadoresPorSocket;
    private Controller blackboardController;
    private ServerComunicacion serverComunicacion;
    private volatile boolean running;
    private boolean isRunning;
    private volatile boolean isConnected;

    /**
     * Constructor de la clase Server. Inicializa las estructuras de datos
     * necesarias y crea instancias del controlador y de la clase de
     * comunicación.
     */
    public Server() {
   this.clientes = new ArrayList<>();
        this.outputStreams = new HashMap<>();
        this.jugadoresPorSocket = new HashMap<>();
        this.blackboardController = new Controller(this);
        this.serverComunicacion = new ServerComunicacion(this);
        this.running = false;
        this.isConnected = false;
    }

    /**
     * Inicia el servidor en el puerto especificado. El servidor escucha
     * conexiones entrantes indefinidamente, aceptando nuevos clientes y
     * manejándolos.
     *
     * @param puerto El número de puerto en el que el servidor escuchará.
     * @throws IOException Si ocurre un error al crear el ServerSocket.
     */
    public void iniciarServidor(int puerto) throws IOException {
      servidor = new ServerSocket(puerto, 0, InetAddress.getByName("127.0.0.1"));
        running = true;
        isConnected = true;
        System.out.println("Servidor iniciado en dirección IP: 127.0.0.1, puerto: " + puerto);

        // Iniciar thread para aceptar conexiones
        new Thread(() -> {
            while (running) {
                try {
                    Socket clienteSocket = servidor.accept();
                    System.out.println("Nueva conexión aceptada: " + clienteSocket.getInetAddress());
                    manejarNuevaConexion(clienteSocket);
                } catch (IOException e) {
                    if (running) {
                        System.err.println("Error aceptando conexión: " + e.getMessage());
                        isConnected = false;
                    }
                }
            }
        }).start();
    }
  public boolean isConnected() {
        return isConnected;
    }
    public void enviarEventoATodos(Evento evento) {
        for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
            enviarMensajeACliente(entry.getKey(), evento);
        }
    }

    /**
     * Maneja una nueva conexión de cliente. Agrega el socket del cliente a la
     * lista de clientes conectados, y lanza un nuevo hilo para manejar la
     * comunicación con dicho cliente.
     *
     * @param clienteSocket El socket del cliente que se conectó.
     */
    private void manejarNuevaConexion(Socket clienteSocket) {
       try {
            ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clienteSocket.getInputStream());
            
            synchronized (outputStreams) {
                outputStreams.put(clienteSocket, out);
                clientes.add(clienteSocket);
            }
            
            // Enviar confirmación de conexión
            Evento confirmacion = new Evento("CONEXION_EXITOSA");
            enviarMensajeACliente(clienteSocket, confirmacion);
            
            // Iniciar thread para escuchar mensajes
            new Thread(() -> escucharCliente(clienteSocket, in)).start();
            
        } catch (IOException e) {
            System.err.println("Error estableciendo conexión con cliente: " + e.getMessage());
            cerrarConexion(clienteSocket);
            isConnected = false;
        }
    
    }

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
    private void manejarCliente(Socket cliente) {
        try {
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
            while (true) {
                Evento evento = (Evento) in.readObject();
                serverComunicacion.procesarEvento(cliente, evento);
            }
        } catch (IOException | ClassNotFoundException e) {
            manejarErrorComunicacion();
        }
    }

    /**
     * Devuelve el controlador del servidor.
     *
     * @return El controlador blackboardController.
     */
    public Controller getController() {
        return this.blackboardController;
    }

    /**
     * Registra a un jugador en el mapa de jugadores por socket.
     *
     * @param socket El socket asociado al jugador.
     * @param jugador El jugador a registrar.
     */
    public void registrarJugador(Socket socket, Jugador jugador) {
        jugadoresPorSocket.put(socket, jugador);
    }

    /**
     * Obtiene el socket asociado a un jugador dado.
     *
     * @param jugador El jugador cuyo socket se desea obtener.
     * @return El socket asociado al jugador, o null si no existe.
     */
    public Socket getSocketJugador(Jugador jugador) {
        return jugadoresPorSocket.entrySet().stream()
                .filter(entry -> entry.getValue().equals(jugador))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    /**
     * Envía un evento a todos los clientes conectados.
     *
     * @param evento El evento a enviar.
     */
    public void enviarEvento(Evento evento) {
        System.out.println("Enviando evento: " + evento.getTipo());
        List<Socket> clientesDesconectados = new ArrayList<>();

        synchronized (outputStreams) {
            for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
                try {
                    ObjectOutputStream out = entry.getValue();
                    out.writeObject(evento);
                    out.flush();
                    System.out.println("Evento enviado a cliente: " + entry.getKey().getInetAddress());
                } catch (IOException e) {
                    System.err.println("Error enviando evento a cliente: " + e.getMessage());
                    clientesDesconectados.add(entry.getKey());
                }
            }
        }

        // Remover clientes desconectados
        clientesDesconectados.forEach(this::cerrarConexion);
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
                out.writeObject(mensaje);
                out.flush();
                System.out.println("Mensaje enviado exitosamente al cliente");
            } else {
                System.err.println("Error: No se encontró el stream de salida para el cliente");
            }
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje al cliente: " + e.getMessage());
            manejarErrorComunicacion();
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

    private void procesarEvento(Socket cliente, Evento evento) {
        System.out.println("Procesando evento: " + evento.getTipo());
        switch (evento.getTipo()) {
            case "CREAR_SALA":
                serverComunicacion.procesarEvento(cliente, evento);
                break;
            case "UNIRSE_SALA":
            case "ABANDONAR_SALA":
            case "JUGADA":
                blackboardController.procesarEvento(cliente, evento);
                break;
            default:
                System.out.println("Evento no reconocido: " + evento.getTipo());
        }
    }

}
