/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mediadores;

import Server.Server;
import EventoJuego.Evento;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioModel;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Serva
 */
public class CrearUsuarioMediador {

    private Server server;
    private CountDownLatch latch;

    // Constructor para inicializar el latch
    public CrearUsuarioMediador() {
        // Inicializa el latch con 1 para bloquear hasta que el servidor esté listo
        this.latch = new CountDownLatch(1);
    }

    public void setServer(Server server) {
        this.server = server;
        if (server != null) {
            latch.countDown();  // Llama a countDown cuando el servidor esté listo
            System.out.println("Servidor asignado correctamente.");
        } else {
            System.out.println("Advertencia: El servidor no se ha asignado.");
        }
    }

    public void esperarServidor() throws InterruptedException {
        latch.await();  // Espera hasta que el servidor esté inicializado
    }

    public void crearJugador(CrearUsuarioModel usuario) throws InterruptedException {
        esperarServidor();  // Asegura que el servidor esté listo antes de continuar
        if (server == null) {
            System.out.println("Error: El servidor no está inicializado.");
            throw new IllegalStateException("Server no está inicializado.");
        }
        System.out.println("Servidor está inicializado. Creando el jugador...");

        Evento evento = new Evento("REGISTRO_USUARIO");
        evento.agregarDato("nombre", usuario.getNombre());
        evento.agregarDato("avatar", usuario.getAvatar());

        server.enviarEvento(evento);
    }
}
