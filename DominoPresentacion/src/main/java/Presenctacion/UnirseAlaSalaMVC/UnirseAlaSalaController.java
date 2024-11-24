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
        // Esperar un breve momento antes de solicitar las salas

    });
}
    public void cargarSalasDisponibles() {
    if (server != null && server.isConnected()) {
        System.out.println("Solicitando salas disponibles al servidor...");
        Evento solicitud = new Evento("SOLICITAR_SALAS");
        server.enviarEventoATodos(solicitud); // Solicitud al servidor

        // Simular espera de respuesta del servidor
        new Thread(() -> {
            try {
                Thread.sleep(500); // Simula un pequeño retraso de respuesta
                SwingUtilities.invokeLater(() -> model.imprimirEstadoActual());
                SwingUtilities.invokeLater(() -> view.actualizarTablaSalas());
            } catch (InterruptedException e) {
                System.err.println("Error cargando salas disponibles: " + e.getMessage());
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
    public void procesarRespuestaServer(Evento evento) {
    System.out.println("Controller: Recibido evento tipo: " + evento.getTipo());
    if ("RESPUESTA_SALAS".equals(evento.getTipo())) {
        try {
            List<Sala> salas = (List<Sala>) evento.obtenerDato("salas");
            System.out.println("Controller: Recibidas " + (salas != null ? salas.size() : "0") + " salas del servidor");
            model.actualizarSalasDisponibles(salas); // Actualiza el modelo con las salas
        } catch (Exception e) {
            System.err.println("Controller: Error procesando respuesta de salas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
public void actualizarTablaConSalas() {
    try {
        List<Sala> salas = model.getSalasDisponibles();
        if (salas == null || salas.isEmpty()) {
            System.out.println("No hay salas disponibles para mostrar.");
            return;
        }

        // Llamar al método de la vista para actualizar la tabla
        view.agregarBotonRefrescar();
    } catch (Exception e) {
        System.err.println("Error al actualizar la tabla con salas: " + e.getMessage());
        e.printStackTrace();
    }
}


    /**
     * Solicita al servidor la lista actualizada de salas disponibles. Verifica
     * la conexión al servidor antes de enviar la solicitud.
     */
   public void solicitarActualizacionSalasConConsola() {
    if (server != null && server.isConnected()) {
        System.out.println("Solicitando actualización de salas...");
        Evento evento = new Evento("SOLICITAR_SALAS");
        server.enviarEvento(evento);

        // Imprimir en consola el estado actual después de una breve espera
        new Timer(500, e -> model.imprimirEstadoActual()).start();
    } else {
        System.err.println("No se puede solicitar actualización: servidor no conectado");
    }
}

}
