/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion;

import Presenctacion.MenuPrincipalMVC.CrearUsuarioController;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioModel;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioView;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroView;

/**
 *
 * @author INEGI
 */
public class Mediador {
     private CrearUsuarioController crearUsuarioController;
    private TableroController tableroController;
    private TableroView tableroView;

    public Mediador(CrearUsuarioController crearUsuarioController, TableroController tableroController, TableroView tableroView) {
        this.crearUsuarioController = crearUsuarioController;
        this.tableroController = tableroController;
        this.tableroView = tableroView;
    }

    public void iniciarAplicacion() {
        crearUsuarioController.mostrarVista();
    }

    public void usuarioCreado(CrearUsuarioModel usuario) {
        // Ocultar la vista de creación de usuario
        crearUsuarioController.ocultarVista();
        
        // Iniciar el juego
        iniciarJuego(usuario);
    }

    private void iniciarJuego(CrearUsuarioModel usuario) {
        // Aquí puedes inicializar el juego con el usuario creado
        // Por ejemplo, configurar el tablero con el nombre del jugador
        
        // Mostrar la vista del tablero
        tableroView.setVisible(true);
    }

    public void finalizarJuego() {
        tableroView.setVisible(false);
        crearUsuarioController.mostrarVista();
    }
}
