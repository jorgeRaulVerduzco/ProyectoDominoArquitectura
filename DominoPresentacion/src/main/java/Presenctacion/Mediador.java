/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion;

import Presenctacion.CrearSalaMVC.CrearSalaView;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioController;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioModel;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioView;
import Presenctacion.CrearSalaMVC.CrearSalaController;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroView;
import Server.Server;

/**
 *
 * @author INEGI
 */
public class Mediador {

    private CrearUsuarioController crearUsuarioController;
    private CrearSalaController crearSalaController;
    private CrearSalaView crearSalaView;
    private TableroController tableroController;
    private TableroView tableroView;
    private Server server;

    public Mediador(
            CrearUsuarioController crearUsuarioController,
            CrearSalaController crearSalaController,
            CrearSalaView crearSalaView,
            TableroController tableroController,
            TableroView tableroView) {
        this.crearUsuarioController = crearUsuarioController;
        this.crearSalaController = crearSalaController;
        this.crearSalaView = crearSalaView;
        this.tableroController = tableroController;
        this.tableroView = tableroView;
    }

    public void setServer(Server server) {
        if (server == null) {
            System.out.println("Error: El servidor no puede ser nulo.");
            throw new IllegalArgumentException("El servidor no puede ser nulo.");
        }
        this.server = server;
        System.out.println("Servidor configurado correctamente.");
        // Pasar el servidor a los controladores que lo necesiten
        crearUsuarioController.setServer(server);
        // Si otros controladores necesitan el servidor, puedes pasarlo aquí
        crearSalaController.setServer(server);  // Si es necesario en este controlador
        tableroController.setServer(server);    // Si es necesario en este controlador
    }

    public void iniciarAplicacion() {
        crearUsuarioController.mostrarVista();
    }

    public void usuarioCreado(CrearUsuarioModel usuario) {
        crearUsuarioController.ocultarVista();
        mostrarCrearSala(usuario);
    }

    private void mostrarCrearSala(CrearUsuarioModel usuario) {
        System.out.println("Mostrando vista de Crear Sala...");
        if (crearSalaView != null) {
            crearSalaView.setVisible(true);
        } else {
            System.out.println("Error: crearSalaView es null.");
        }
    }

    public void salaCreada() {
        crearSalaView.setVisible(false);
        iniciarJuego();
    }

    private void iniciarJuego() {
        tableroView.setVisible(true);
    }

    public void finalizarJuego() {
        tableroView.setVisible(false);
        crearUsuarioController.mostrarVista();
    }
}
