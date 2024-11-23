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
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;

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
        view.setVisible(true);
        solicitarActualizacionSalas();
    }

    /**
     * Procesa la respuesta recibida del servidor. En particular, maneja eventos
     * de tipo "SOLICITAR_SALAS" y actualiza el modelo con las salas recibidas.
     *
     * @param evento el evento recibido del servidor.
     */
    public void procesarRespuestaServer(Evento evento) {
        if ("SOLICITAR_SALAS".equals(evento.getTipo())) {
            try {
                List<Sala> salas = (List<Sala>) evento.obtenerDato("salas");
                System.out.println("Salas recibidas en controller: " + (salas != null ? salas.size() : "null"));
                model.actualizarSalasDisponibles(salas);
                model.imprimirEstadoActual(); // Agregamos depuración
            } catch (Exception e) {
                System.err.println("Error procesando salas: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Solicita al servidor la lista actualizada de salas disponibles. Verifica
     * la conexión al servidor antes de enviar la solicitud.
     */
    public void solicitarActualizacionSalas() {
        if (server != null && server.isConnected()) {
            System.out.println("Solicitando actualización de salas...");
            Evento evento = new Evento("SOLICITAR_SALAS");
            server.enviarEvento(evento);
        } else {
            System.err.println("No se puede solicitar actualización: servidor no conectado");
        }
    }

}
