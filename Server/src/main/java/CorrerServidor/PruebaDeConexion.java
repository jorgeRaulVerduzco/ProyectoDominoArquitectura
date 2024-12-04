/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package CorrerServidor;

import EventoJuego.Evento;
import Server.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Serva
 */
public class PruebaDeConexion {

    /**
     * @param args the command line arguments
     */
  public static void main(String[] args) {
        // Configurar el servidor en un puerto específico
        int puerto = 51114;

        // Crear una instancia del servidor y ejecutarla en un hilo separado
        Thread serverThread = new Thread(() -> {
            Server servidor = new Server();
            try {
                servidor.iniciarServidor(puerto);
            } catch (IOException e) {
                System.err.println("Error iniciando el servidor: " + e.getMessage());
            }
        });
        serverThread.start();

        // Esperar un momento para asegurar que el servidor esté activo
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Error en el delay: " + e.getMessage());
        }

        // Probar la conexión del cliente
        try (Socket cliente = new Socket("127.0.0.1", puerto);
             ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(cliente.getInputStream())) {

            System.out.println("Cliente conectado al servidor.");

            // Enviar un mensaje al servidor
            Evento evento = new Evento("PRUEBA_CONEXION");
            out.writeObject(evento);
            out.flush();
            System.out.println("Mensaje enviado al servidor: " + evento.getTipo());

            // Leer la respuesta del servidor
            Evento respuesta = (Evento) in.readObject();
            System.out.println("Respuesta del servidor: " + respuesta.getTipo());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error en la conexión del cliente: " + e.getMessage());
        }
    }
}
