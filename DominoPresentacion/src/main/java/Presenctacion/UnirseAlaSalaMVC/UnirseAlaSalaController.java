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

    public UnirseAlaSalaController(UnirseAlaSalaModel model, UnirseAlaSalaView view) {
        this.model = model;
        this.view = view;
        this.view.setModel(model);
        this.model.addObserver(view); // Aseguramos que la vista observe al modelo
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    public void setServer(Server server) {
        this.server = server;
        model.setServer(server);
    }

    public void mostrarVista() {
        view.setVisible(true);
        solicitarActualizacionSalas();
    }

    public void solicitarActualizacionSalas() {
        if (server != null && server.isConnected()) {
            Evento evento = new Evento("SOLICITAR_SALAS");
            server.enviarEvento(evento);
        }
    }

    public void procesarRespuestaServer(Evento evento) {
        evento = new Evento("SOLICITAR_SALAS");

        if ("SOLICITAR_SALAS".equals(evento.getTipo())) {
            try {
                List<Sala> salas = (List<Sala>) evento.obtenerDato("salas");
                if (salas != null) {
                    System.out.println("Salas recibidas: " + salas.size());
                    model.actualizarSalasDisponibles(salas);
                } else {
                    System.out.println("Salas recibidas son nulas");
                    model.actualizarSalasDisponibles(Collections.emptyList());
                }
            } catch (Exception e) {
                System.err.println("Error procesando salas: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

}
