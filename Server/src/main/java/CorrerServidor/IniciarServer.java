/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package CorrerServidor;

import Server.Server;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author INEGI
 */
public class IniciarServer {

   private static final int PUERTO_SERVIDOR = 51114;
    private static Server server;

    public static void main(String[] args) {
        // Iniciar el servidor en un hilo separado
        new Thread(() -> {
            try {
                server = new Server();
                server.iniciarServidor(PUERTO_SERVIDOR);
            } catch (IOException e) {
                System.err.println("Error al iniciar el servidor: " + e.getMessage());
                System.exit(1);
            }
        }).start();

        // Esperar activamente a que el servidor esté listo
        while (server == null || !server.isServidorActivo()) {
            try {
                System.out.println("Esperando a que el servidor esté listo...");
                Thread.sleep(500);  // Espera medio segundo antes de volver a comprobar
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Configurar la aplicación después de que el servidor esté listo
         System.out.println("Servidor está listo para aceptar conexiones.");
       System.out.println("Teclee 'salir' para cerrar el servidor.");

       // Leer entrada del usuario para cerrar el servidor
       try (Scanner scanner = new Scanner(System.in)) {
           while (true) {
               String entrada = scanner.nextLine();
               if ("salir".equalsIgnoreCase(entrada)) {
                   System.out.println("Cerrando servidor...");
                   if (server != null) {
                       server.cerrarServidor();
                   }
                   System.exit(0);
               }
           }
       }
     
    }
    
}
