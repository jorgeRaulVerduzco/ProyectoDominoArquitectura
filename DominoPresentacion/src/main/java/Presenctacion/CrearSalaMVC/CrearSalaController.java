/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.CrearSalaMVC;

import EventoJuego.Evento;
import Presenctacion.Mediador;
import Server.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author INEGI
 */
public class CrearSalaController {

    private CrearSalaModel model;
    private CrearSalaView view;
    private Mediador mediador;
    private Server server;

    public CrearSalaController(CrearSalaModel model, CrearSalaView view) {
        this.model = model;
        this.view = view;
model.setServer(server);
        // Inicializar listeners
        this.view.addCrearSalaListener(new CrearSalaListener());
        this.view.addRegresarListener(new RegresarListener());
        this.view.addNumeroJugadoresListener(new NumeroJugadoresListener());
        this.view.addNumeroFichasListener(new NumeroFichasListener());
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    class CrearSalaListener implements ActionListener {
   @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int numFichas = Integer.parseInt(view.getNumeroFichas());
                if (numFichas <= 0) {
                    JOptionPane.showMessageDialog(view, "El número de fichas debe ser positivo");
                    return;
                }
                model.setNumeroFichas(numFichas);
                model.setNumeroJugadores(Integer.parseInt(view.getNumeroJugadores()));
                model.crearSala();
                
                // Verificar la creación de la sala
                System.out.println("Sala enviada al servidor");
                mediador.salaCreada();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Por favor ingrese números válidos");
            }
        }
    }

    class RegresarListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            view.dispose();
        }
    }

    class NumeroJugadoresListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                model.setNumeroJugadores(Integer.parseInt(view.getNumeroJugadores()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Número de jugadores inválido");
            }
        }
    }

    class NumeroFichasListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int numFichas = Integer.parseInt(view.getNumeroFichas());
                model.setNumeroFichas(numFichas);
            } catch (NumberFormatException ex) {
                // El manejo se hace en el botón crear
            }
        }
    }
}
