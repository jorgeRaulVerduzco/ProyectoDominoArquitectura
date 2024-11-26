/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Presenctacion.Mediador;
import Server.Server;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author INEGI
 */
public class UnirseAlaSalaController {

    private UnirseAlaSalaModel model;
    private UnirseAlaSalaView view;
    private Mediador mediador;
    private Server server;

    /**
     * Constructor que inicializa el controlador con un modelo y una vista.
     * También configura la relación entre la vista y el modelo.
     *
     * @param model instancia del modelo a utilizar.
     * @param view instancia de la vista a utilizar.
     */
    public UnirseAlaSalaController(UnirseAlaSalaModel model, UnirseAlaSalaView view) {
        this.model = model;
        this.view = view;
        this.view.setModel(model);
        this.model.addObserver(view); // Aseguramos que la vista observe al modelo
    }

    /**
     * Establece el mediador para coordinar la interacción con otros módulos.
     *
     * @param mediador instancia del mediador a utilizar.
     */
    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    /**
     * Configura la conexión con el servidor y la sincroniza con el modelo.
     *
     * @param server instancia del servidor a utilizar.
     */
    public void setServer(Server server) {
        this.server = server;
        model.setServer(server);
    }

    /**
     * Muestra la vista asociada y solicita una actualización de las salas
     * disponibles.
     */
    public void mostrarVista() {
    SwingUtilities.invokeLater(() -> {
        view.setVisible(true);
       
    });
}
   public void cargarSalasDisponibles() {
    if (server != null && server.isServidorActivo()) {
        System.out.println("Solicitando salas disponibles al servidor...");
        Evento evento = new Evento("SOLICITAR_SALAS");
        server.enviarEvento(evento); // Solicita salas al servidor

        // Simula una respuesta del servidor después de un breve retraso
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simula la espera de respuesta
                SwingUtilities.invokeLater(() -> model.imprimirEstadoActual());
                SwingUtilities.invokeLater(() -> view.actualizarTablaSalas());
            } catch (InterruptedException e) {
                System.err.println("Error al cargar salas: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }).start();
    } else {
        System.err.println("Error: El servidor no está conectado.");
    }
}


    

    /**
     * Procesa la respuesta recibida del servidor. En particular, maneja eventos
     * de tipo "SOLICITAR_SALAS" y actualiza el modelo con las salas recibidas.
     *
     * @param evento el evento recibido del servidor.
     */


public void actualizarTablaConSalas() {
    try {
        List<Sala> salas = model.getSalasDisponibles();
        if (salas == null || salas.isEmpty()) {
            System.out.println("No hay salas disponibles para mostrar.");
            return;
        }

        // Llamar al método de la vista para actualizar la tabla
    
    } catch (Exception e) {
        System.err.println("Error al actualizar la tabla con salas: " + e.getMessage());
        e.printStackTrace();
    }
}



}
