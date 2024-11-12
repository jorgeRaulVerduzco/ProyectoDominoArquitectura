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

    public void iniciarAplicacion() {
        crearUsuarioController.mostrarVista();
    }

    public void usuarioCreado(CrearUsuarioModel usuario) {
        crearUsuarioController.ocultarVista();
        mostrarCrearSala(usuario);
    }

    private void mostrarCrearSala(CrearUsuarioModel usuario) {
        crearSalaView.setVisible(true);
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