/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Server.Server;
import java.io.IOException;

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

        // Configurar el look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al configurar el look and feel: " + e.getMessage());
        }
    }
    
}
