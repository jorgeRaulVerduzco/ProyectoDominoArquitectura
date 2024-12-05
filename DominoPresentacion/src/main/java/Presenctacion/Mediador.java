/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion;

import Dominio.Jugador;
import EventoJuego.Evento;
import Presenctacion.CrearSalaMVC.CrearSalaView;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioController;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioModel;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioView;
import Presenctacion.CrearSalaMVC.CrearSalaController;
import Presenctacion.CrearSalaMVC.CrearSalaModel;
import Presenctacion.SeleccionJuego.OpcionesDeJuegoView;
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaController;
import Presentacion.EsperaMVC.EsperaController;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroView;
import Server.Server;
import javax.swing.JOptionPane;

/**
 *
 * @author INEGI
 */
public class Mediador {

    private UnirseAlaSalaController unirseAlaSalaController;
private EsperaController esperaController;
    private CrearUsuarioController crearUsuarioController;
    private CrearSalaController crearSalaController;
    private CrearSalaView crearSalaView;
    private TableroController tableroController;
    private TableroView tableroView;
    private Server server;
    private CrearUsuarioModel usuarioActual;
    private OpcionesDeJuegoView opcionesDeJuegoView;

    public Mediador(
            CrearUsuarioController crearUsuarioController,
            CrearSalaController crearSalaController,
            CrearSalaView crearSalaView,
            TableroController tableroController,
            TableroView tableroView,
            UnirseAlaSalaController unirseAlaSalaController,
            OpcionesDeJuegoView opcionesDeJuegoView, EsperaController esperaController) {
        this.esperaController  = esperaController;
        this.crearUsuarioController = crearUsuarioController;
        this.crearSalaController = crearSalaController;
        this.crearSalaView = crearSalaView;
        this.tableroController = tableroController;
        this.tableroView = tableroView;
        this.unirseAlaSalaController = unirseAlaSalaController;
        this.opcionesDeJuegoView = opcionesDeJuegoView; // Inicializar
        // Establecer el mediador en la vista de opciones de juego
        opcionesDeJuegoView.setMediator(this);
    }

    public void setServer(Server server) {
        if (server == null) {
            System.out.println("Error: El servidor no puede ser nulo.");
            throw new IllegalArgumentException("El servidor no puede ser nulo.");
        }
        this.server = server;
        System.out.println("Servidor configurado correctamente.");
        crearUsuarioController.setServer(server);

        // Pasar el servidor a los controladores que lo necesiten
        crearUsuarioController.setServer(server);
        opcionesDeJuegoView.setServer(server);
        // Si otros controladores necesitan el servidor, puedes pasarlo aquí
        crearSalaController.setServer(server);  // Si es necesario en este controlador
        tableroController.setServer(server);    // Si es necesario en este controlador
        unirseAlaSalaController.setServer(server); // Añade esta línea

    }

    public void iniciarAplicacion() {
        crearUsuarioController.mostrarVista();

    }

    // Método para mostrar la vista de OpcionesDeJuego
    public void mostrarOpcionesDeJuego(Jugador jugador) {
        if (opcionesDeJuegoView != null) {
            opcionesDeJuegoView.setJugador(jugador);  // Pasar el jugador a la vista
            opcionesDeJuegoView.setServer(server);    // Pasar el servidor a la vista
            opcionesDeJuegoView.setLocationRelativeTo(null);  // Centrar la ventana
            opcionesDeJuegoView.setVisible(true);  // Mostrar la vista
        } else {
            System.out.println("OpcionesDeJuegoView no está inicializado.");
        }
    }

    public void usuarioCreado(Jugador jugador) {
        System.out.println("Usuario creado: " + jugador);

        if (server != null) {
            boolean usuarioExistente = server.contieneJugador(jugador.getNombre());
            System.out.println("¿Usuario existe? " + usuarioExistente);
        } else {
            JOptionPane.showMessageDialog(null,
                    "El servidor no está disponible.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return; // Detenemos el flujo si el servidor no está inicializado
        }

        // Cambiar de vista si el usuario no existe
        crearUsuarioController.ocultarVista();
        // Aquí, en lugar de mostrar CrearSalaView, mostramos OpcionesDeJuegoView
        mostrarOpcionesDeJuego(jugador);  // Llamamos al método que muestra OpcionesDeJuegoView
    }

    public void mostrarCrearSala(Jugador jugador) {
        System.out.println("Mostrando vista de Crear Sala...");
        if (crearSalaView != null) {
            CrearSalaModel modelo = crearSalaController.getModel();
            modelo.setJugadorActual(jugador); // Pasar el jugador al modelo
            crearSalaView.pack();
            crearSalaView.setLocationRelativeTo(null);
            crearSalaView.setVisible(true);
        } else {
            System.out.println("Vista de crear sala es null.");
        }
    }
//         Método para mostrar UnirseAlaSala después de OpcionesDeJuego

    public void mostrarUnirseAlaSala() {
        unirseAlaSalaController.mostrarVista();  // Llamar al controlador para mostrar UnirseAlaSala
    }

    public void salaCreada() {
        crearSalaView.setVisible(false);
        
        esperaController.mostrarVista();

       

    }

public void iniciarJuego() {
    if (tableroView != null) {
        tableroView.setVisible(true); // Muestra la vista del tablero
    } else {
        System.err.println("TableroView no está inicializado.");
    }
}

    public void finalizarJuego() {
        tableroView.setVisible(false);
        crearUsuarioController.mostrarVista();
    }

}
