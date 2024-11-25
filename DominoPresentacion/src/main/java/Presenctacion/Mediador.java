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
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaController;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroView;
import Server.Server;

/**
 *
 * @author INEGI
 */
public class Mediador {

    private UnirseAlaSalaController unirseAlaSalaController;

    private CrearUsuarioController crearUsuarioController;
    private CrearSalaController crearSalaController;
    private CrearSalaView crearSalaView;
    private TableroController tableroController;
    private TableroView tableroView;
    private Server server;
private CrearUsuarioModel usuarioActual;
    public Mediador(
            CrearUsuarioController crearUsuarioController,
            CrearSalaController crearSalaController,
            CrearSalaView crearSalaView,
            TableroController tableroController,
            TableroView tableroView,
            UnirseAlaSalaController unirseAlaSalaController) {
        this.crearUsuarioController = crearUsuarioController;
        this.crearSalaController = crearSalaController;
        this.crearSalaView = crearSalaView;
        this.tableroController = tableroController;
        this.tableroView = tableroView;
        this.unirseAlaSalaController = unirseAlaSalaController;

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
            unirseAlaSalaController.setServer(server); // Añade esta línea

    }

    public void iniciarAplicacion() {
        crearUsuarioController.mostrarVista();
          
    }

     public void usuarioCreado(CrearUsuarioModel usuario) {
        this.usuarioActual = usuario;
        crearUsuarioController.ocultarVista();
        mostrarCrearSala(usuario);
    }
    
    public void mostrarCrearSala(CrearUsuarioModel usuario) {
        System.out.println("Mostrando vista de Crear Sala...");
        if (crearSalaView != null) {
            // Convertir el usuario a jugador y establecerlo en el modelo
            Jugador jugador = new Jugador();
            jugador.setNombre(usuario.getNombre());
            jugador.setEstado("ACTIVO");
            
            CrearSalaModel modelo = crearSalaController.getModel();
            modelo.setJugadorActual(jugador);
            
            crearSalaView.pack();
            crearSalaView.setLocationRelativeTo(null);
            crearSalaView.setVisible(true);
        } else {
            System.out.println("Vista de crear sala es null.");
        }
    }
  
       public void salaCreada() {
        crearSalaView.setVisible(false);
        // Mostrar la vista principal (puedes configurar cuál es la principal)
    unirseAlaSalaController.mostrarVista();

    // Cargar las salas disponibles en la tabla al iniciar
    unirseAlaSalaController.cargarSalasDisponibles();
        
    }

    private void iniciarJuego() {
        tableroView.setVisible(true);
    }

    public void finalizarJuego() {
        tableroView.setVisible(false);
        crearUsuarioController.mostrarVista();
    }
    
      
     public void mostrarUnirseASala() {
         
        unirseAlaSalaController.mostrarVista();
    
    }
     
}
