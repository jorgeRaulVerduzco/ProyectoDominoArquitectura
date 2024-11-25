/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion.EsperaMVC;

import Server.Server;
import ServerLocal.ServerComunicacion;

/**
 *
 * @author Serva
 */
public class EsperaController {

    private EsperaModel model;
    private EsperaView view;
    private Server server;

    public EsperaController(EsperaModel model, EsperaView view, Server server) {
        this.model = model;
        this.view = view;
        this.server = server;

        model.addObserver(view);
        iniciarEscucha();
    }

    public void iniciarEscucha() {
        new Thread(() -> {
            while (!model.isPartidaIniciada()) {
                // Escucha eventos del servidor
                Object evento = server.isConnected();

                if (evento instanceof String eventoTexto) {
                    if (eventoTexto.startsWith("NUEVO_JUGADOR:")) {
                        String nuevoJugador = eventoTexto.split(":")[1];
                        model.agregarJugador(nuevoJugador);
                    } else if (eventoTexto.equals("PARTIDA_INICIADA")) {
                        model.setPartidaIniciada(true);
                        view.actualizarEstado("¡La partida ha iniciado!");
                        iniciarPartida();
                    }
                }
            }
        }).start();
    }

    public void iniciarPartida() {
        // Lógica para iniciar el tablero
        System.out.println("Iniciando la partida...");
    }
}
