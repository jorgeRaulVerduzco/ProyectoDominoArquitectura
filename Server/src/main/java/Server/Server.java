/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Dominio.Jugador;
import EventoJuego.Evento;
import ServerLocal.ServerComunicacion;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    /**
     * El servidor que escucha las conexiones entrantes de los clientes.
     */
    private ServerSocket servidor;
    /**
     * Lista de sockets de clientes conectados al servidor.
     */
    private List<Socket> clientes;
    /**
     * Mapa que asocia cada socket de cliente con un ObjectOutputStream, que se
     * utiliza para enviar objetos a los clientes.
     */
    private Map<Socket, ObjectOutputStream> outputStreams;
    /**
     * Mapa que asocia cada socket de cliente con un objeto Jugador, utilizado
     * para relacionar jugadores con sus conexiones de red.
     */
    private Map<Socket, Jugador> jugadoresPorSocket;

    /**
     * Constructor de la clase Server. Inicializa las colecciones de clientes,
     * outputStreams y jugadoresPorSocket.
     */
    public Server() {
        this.clientes = new ArrayList<>();
        this.outputStreams = new HashMap<>();
        this.jugadoresPorSocket = new HashMap<>();
    }

    /**
     * Inicia el servidor, escucha las conexiones entrantes en el puerto
     * especificado y maneja a cada cliente en un hilo separado.
     *
     * @param puerto El puerto en el que el servidor escuchará las conexiones
     * entrantes.
     * @throws IOException Si ocurre un error al crear el ServerSocket o al
     * aceptar una conexión.
     */
    public void iniciarServidor(int puerto) throws IOException {
        servidor = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en puerto: " + puerto);

        while (true) {
            Socket clienteSocket = servidor.accept();
            clientes.add(clienteSocket);
            ObjectOutputStream out = new ObjectOutputStream(clienteSocket.getOutputStream());
            outputStreams.put(clienteSocket, out);

            new Thread(() -> manejarCliente(clienteSocket)).start();
        }
    }

    /**
     * Maneja la comunicación con un cliente en un hilo separado. Lee los
     * eventos enviados por el cliente y los procesa.
     *
     * @param cliente El socket del cliente con el que se establece la
     * comunicación.
     */
    private void manejarCliente(Socket cliente) {
        try {
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
            while (true) {
                Evento evento = (Evento) in.readObject();
                new ServerComunicacion(this).procesarEvento(cliente, evento);
            }
        } catch (IOException | ClassNotFoundException e) {
            manejarErrorComunicacion();
        }
    }

    /**
     * Busca el socket correspondiente a un jugador dado.
     *
     * @param jugador El jugador cuyo socket se busca.
     * @return El socket asociado al jugador, o null si no se encuentra.
     */
    public Socket getSocketJugador(Jugador jugador) {
        for (Map.Entry<Socket, Jugador> entry : jugadoresPorSocket.entrySet()) {
            if (entry.getValue().equals(jugador)) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Envía un evento a todos los clientes conectados.
     *
     * @param evento El evento que se enviará a todos los clientes.
     */
    public void enviarEvento(Evento evento) {
        for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
            try {
                entry.getValue().writeObject(evento);
                entry.getValue().flush();
            } catch (IOException e) {
                manejarErrorComunicacion();
            }
        }
    }

    /**
     * Maneja los errores de comunicación, mostrando un mensaje de error en la
     * consola.
     */
    public void manejarErrorComunicacion() {
        System.err.println("Error en la comunicacion del servidor");

    }

    /**
     * Envía un mensaje específico a un cliente.
     *
     * @param cliente El socket del cliente al que se enviará el mensaje.
     * @param mensaje El mensaje que se enviará al cliente.
     */
    public void enviarMensajeAClientes(Socket cliente, Evento mensaje) {
        try {
            ObjectOutputStream out = outputStreams.get(cliente);
            if (out != null) {
                out.writeObject(mensaje);
                out.flush();
            }
        } catch (IOException e) {
            manejarErrorComunicacion();
        }
    }
}
