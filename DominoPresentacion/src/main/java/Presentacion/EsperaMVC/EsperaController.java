/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion.EsperaMVC;

import Presenctacion.Mediador;
import Server.Server;
import java.util.List;
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

    
    
         public  void mostrarVista() {
        SwingUtilities.invokeLater(() -> {
            view.setVisible(true);

        });
    }

    public void iniciarPartida() {
        System.out.println("Iniciando la partida...");
        server.notificarInicioPartida(salaId); // Notifica al servidor

        // Usa el mediador para abrir la vista del tablero
        mediador.iniciarJuego();
        view.dispose(); // Cierra la ventana de espera
    }
}

