/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion.EsperaMVC;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Presenctacion.ConfiguracionSocket;
import Presenctacion.Mediador;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Serva
 */
public class EsperaController {

    private final EsperaModel model;
    private final EsperaView view;
    private final Server server;
    private final String salaId; // ID de la sala proporcionado en el constructor
    private Mediador mediador;

    public EsperaController(EsperaModel model, EsperaView view, Server server, String salaId) {
        this.model = model;
        this.view = view;
        this.server = server;
        this.salaId = salaId; // Almacenar el ID de la sala
        model.addObserver(view);
        iniciarEscucha();
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    public void iniciarEscucha() {
        new Thread(() -> {
            while (!model.isPartidaIniciada()) {
                try {
                    // Usar el ID de sala proporcionado en el constructor
                    List<String> jugadores = server.obtenerJugadoresPorIdSala(salaId);

                    if (!jugadores.isEmpty()) {
                        for (String jugador : jugadores) {
                            // Verificar si el jugador ya está en el modelo
                            if (!model.getJugadoresConectados().contains(jugador)) {
                                model.agregarJugador(jugador);
                            }
                        }
                        view.actualizarEstado("Jugadores actualizados para la sala: " + salaId);
                    } else {
                        view.actualizarEstado("No se encontraron jugadores en la sala: " + salaId);
                    }

                    // Pausa entre solicitudes para evitar sobrecargar el servidor
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Escucha interrumpida: " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error al procesar la solicitud de jugadores: " + e.getMessage());
                }
            }
        }).start();
    }

    public void mostrarVista() {
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);

        });
    }

    public void iniciarPartida() {
    try {
        System.out.println("Iniciando la partida...");
        ServerComunicacion serverC = new ServerComunicacion(server);
        int puerto = ConfiguracionSocket.getInstance().getPuertoSocket();
        Socket cliente = new Socket("localhost", puerto);

        // Obtiene la sala desde el servidor
        Sala sala = serverC.obtenerSalaPorId(salaId);

        // Obtiene las fichas del pozo de la sala
        List<Ficha> fichasPozo = sala.getFichasPozo();

        // Repartir las fichas a los jugadores
        List<Jugador> jugadores = sala.getJugador();
        int fichasPorJugador = 6;

        if (fichasPozo.size() < fichasPorJugador * jugadores.size()) {
            throw new IllegalStateException("No hay suficientes fichas en el pozo para todos los jugadores.");
        }

        for (Jugador jugador : jugadores) {
            List<Ficha> fichasJugador = new ArrayList<>();
            for (int i = 0; i < fichasPorJugador; i++) {
                fichasJugador.add(fichasPozo.remove(0)); // Asigna una ficha del pozo al jugador
            }  
             jugador.setFichasJugador(fichasJugador); // Actualiza las fichas del jugador
        } 

        // Actualiza el estado del pozo en la sala
        sala.setFichasPozo(fichasPozo);

        // Inicia la partida en el servidor
        Evento evento = new Evento("INICIAR_PARTIDA");
        evento.agregarDato("sala", sala);
        serverC.iniciarPartida(cliente, evento);

        // Usa el mediador para abrir la vista del tablero
        view.dispose(); // Cierra la ventana de espera
    } catch (IOException ex) {
        Logger.getLogger(EsperaController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}
