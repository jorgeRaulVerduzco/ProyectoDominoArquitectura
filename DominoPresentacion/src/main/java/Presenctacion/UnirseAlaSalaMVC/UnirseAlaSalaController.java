/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Sala;
import EventoJuego.Evento;
import Presenctacion.ConfiguracionSocket;
import Presenctacion.Mediador;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author INEGI
 */
public class UnirseAlaSalaController {

    private UnirseAlaSalaModel model;
    private UnirseAlaSalaView view;
    private Mediador mediador;
    private Server server;

    // Constructor donde se inicializa el modelo y la vista
    public UnirseAlaSalaController(UnirseAlaSalaModel model, UnirseAlaSalaView view) {
        this.model = model;
        this.view = view;

        // Verificar que el modelo no sea null
        if (this.model == null) {
            System.err.println("El modelo es null, no se puede inicializar.");
        }

        // Aquí asignamos el listener al botón de actualizar
        this.view.addActualizarListener(e -> actualizarTabla());
    }

    public void unirseASala(Integer salaId) {
        if (salaId == null) {
            System.err.println("Error: No se proporcionó un ID válido para la sala.");
            return;
        }

        System.out.println("Controlador: Intentando unirse a la sala con ID " + salaId);

        try {
            // Enviar el evento al servidor para unirse a la sala
            model.unirseASala(salaId, null); // Supone que el modelo maneja la lógica
        } catch (Exception e) {
            System.err.println("Error al intentar unirse a la sala: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para actualizar la tabla
    private void actualizarTabla() {
        try {
            // Verifica que el modelo no sea null
            if (model != null) {
                List<Sala> salas = model.getSalasDisponibles();  // Obtener las salas disponibles
                view.actualizarTablaSalas();  // Actualiza la vista de la tabla con las salas
            } else {
                System.err.println("El modelo es null. No se puede actualizar la tabla.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

                int puertoSocket = ConfiguracionSocket.getInstance().getPuertoSocket();
                Socket cliente = new Socket("localhost", puertoSocket);
                Evento solicitudSalas = new Evento("RESPUESTA_SALAS");

                ServerComunicacion servercito = new ServerComunicacion(server);
                System.out.println("se ven al millon");
                servercito.procesarEvento(cliente, solicitudSalas);
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
