/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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

    private void manejarCliente(Socket cliente) {
        try {
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
            while (true) {
                String evento = (String) in.readObject();
                deserializarEvento(cliente, evento);
            }
        } catch (IOException | ClassNotFoundException e) {
            manejarErrorComunicacion();
        }
    }

    public void manejarEvento(String evento) {

    }

    //provicional
    public void enviarEvento(String evento) {
        for (Map.Entry<Socket, ObjectOutputStream> entry : outputStreams.entrySet()) {
            try {
                entry.getValue().writeObject(evento);
                entry.getValue().flush();
            } catch (IOException e) {
                manejarErrorComunicacion();
            }
        }
    }

    public void deserializarEvento(Socket cliente, String evento) {

    }

    public void enviarMensajeAClientes(Socket cliente, String mensaje) {
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

    public void cerrarServidor() {
        try {
            for (Socket cliente : clientes) {
                cliente.close();
            }
            servidor.close();
        } catch (IOException e) {
            manejarErrorComunicacion();
        }
    }

    public void manejarErrorComunicacion() {
        System.err.println("Error en la comunicacion del servidor");

    }
}
