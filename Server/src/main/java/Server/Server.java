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
        System.out.println("Servidor iniciado en dirección IP: 127.0.0.1, puerto: " + puerto);

        while (true) {
            Socket clienteSocket = servidor.accept();
            manejarNuevaConexion(clienteSocket);
        }
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
            clientes.add(clienteSocket);
            ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
            outputStreams.put(clienteSocket, out);
            new Thread(() -> manejarCliente(clienteSocket)).start();
        } catch (IOException e) {
            System.err.println("Error al establecer conexión con cliente: " + e.getMessage());
            manejarErrorComunicacion();
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
        boolean enviado = false;
        
        for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
            try {
                enviarMensajeACliente(entry.getKey(), evento);
                enviado = true;
            } catch (Exception e) {
                System.err.println("Error al enviar evento a cliente: " + e.getMessage());
                // Considera remover el cliente si hay error persistente
            }
        }
        
        if (!enviado) {
            System.err.println("Advertencia: El evento no se pudo enviar a ningún cliente");
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

}