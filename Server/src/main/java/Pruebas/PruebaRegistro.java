/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Dominio.Jugador;
import EventoJuego.Evento;
import Server.Server;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Serva
 */
public class PruebaRegistro {
    public static void main(String[] args) {
        Server server = new Server();
        try {
            // Start server with explicit port
            server.iniciarServidor(9977);
            
            // Wait for server to fully establish
            Thread.sleep(3000);  // Give server time to start
            
            // Register a single player
            registrarJugador(server, "Juan");
            
            // Keep console open to view results
            System.out.println("\nPresiona Enter para salir...");
            new Scanner(System.in).nextLine();
            
        } catch (Exception e) {
            System.err.println("Error en configuración del servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void registrarJugador(Server server, String nombre) {
        try {
            // Create client socket
            Socket socketCliente = new Socket("localhost", 9977);
            socketCliente.setSoTimeout(10000);  // 10 second timeout
            
            // Create output and input streams
            ObjectOutputStream out = new ObjectOutputStream(socketCliente.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socketCliente.getInputStream());
            
            // Step 1: Wait for and verify connection confirmation
            System.out.println("Esperando confirmación de conexión...");
            Evento confirmarConexion = (Evento) in.readObject();
            System.out.println("Confirmación de conexión recibida: " + confirmarConexion.getTipo());
            
            // Step 2: Create registration event
            Evento registroEvento = new Evento("REGISTRO_USUARIO");
            registroEvento.agregarDato("nombre", nombre);
            
            // Step 3: Send registration event
            System.out.println("Enviando evento de registro para: " + nombre);
            out.writeObject(registroEvento);
            out.flush();
            
            // Step 4: Wait for and process registration response
            System.out.println("Esperando respuesta de registro...");
            Evento respuestaRegistro = (Evento) in.readObject();
            
            // Step 5: Analyze registration response
            if ("REGISTRO_USUARIO_CONFIRMADO".equals(respuestaRegistro.getTipo())) {
                System.out.println("✅ " + nombre + " - Registro confirmado");
                // Optionally, print out the registered player details
                Jugador jugadorRegistrado = (Jugador) respuestaRegistro.obtenerDato("jugador");
                System.out.println("Detalles del jugador:");
                System.out.println("  Nombre: " + jugadorRegistrado.getNombre());
            } else if ("REGISTRO_USUARIO_ERROR".equals(respuestaRegistro.getTipo())) {
                System.err.println("❌ " + nombre + " - Registro fallido: " 
                    + respuestaRegistro.obtenerDato("mensaje"));
            }
            
            // Close the socket
            socketCliente.close();
            
        } catch (Exception e) {
            System.err.println("❌ Error en el registro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}