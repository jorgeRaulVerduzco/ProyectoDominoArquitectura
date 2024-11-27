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
import ServerLocal.ServerComunicacion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.out.println("llegua al metodo de CARGARSDALAS EN UNIRSESALACONTROLLER");
        if (server != null && server.isServidorActivo()) {
            try {
                Socket socket = new Socket("localhost", 51114);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                Evento solicitudSalas = new Evento("RESPUESTA_SALAS");
                
                  ServerComunicacion servercito = new ServerComunicacion(server);
        System.out.println("se ven al millon");
                 servercito.procesarEvento(socket, solicitudSalas);
            } catch (IOException ex) {
                Logger.getLogger(UnirseAlaSalaController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        // Agregar esta línea
        view.actualizarTablaSalas();
    } catch (Exception e) {
        System.err.println("Error al actualizar la tabla con salas: " + e.getMessage());
        e.printStackTrace();
    }
    }




}
