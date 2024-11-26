/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;



import Dominio.Jugador;
import EventoJuego.Evento;
import Server.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try {
            // Iniciar el servidor en un hilo separado
            Server server = new Server();
            Thread serverThread = new Thread(() -> {
                try {
                    server.iniciarServidor(51114);
                } catch (Exception e) {
                    System.err.println("[ERROR] Error al iniciar el servidor: " + e.getMessage());
                    e.printStackTrace();
                }
            });
            serverThread.start();

            // Dar tiempo al servidor para inicializar
            Thread.sleep(1000);

            // Simular un cliente que se conecta y registra un jugador
            simularClienteRegistro("JugadorPrueba");

        } catch (Exception e) {
            System.err.println("[ERROR] Error en el main: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void simularClienteRegistro(String nombreJugador) {
        try (Socket socket = new Socket("localhost", 51114);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

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
