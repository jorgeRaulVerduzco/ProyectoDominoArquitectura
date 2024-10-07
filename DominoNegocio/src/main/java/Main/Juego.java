/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import Presenctacion.Mediador;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioController;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioView;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroModel;
import PresentacionTableroMVC.TableroView;

/**
 *
 * @author INEGI
 */
public class Juego {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        // Configurar el look and feel de la aplicación
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName()) ){
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al configurar el look and feel: " + e.getMessage());
        }

        // Lanzar la aplicación en el Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                initializeApplication();
            }
        });
    }

    private static void initializeApplication() {
        // Crear las vistas
      CrearUsuarioView crearUsuarioView = new CrearUsuarioView();
    TableroView tableroView = new TableroView();

    // Crear los controladores
    CrearUsuarioController crearUsuarioController = new CrearUsuarioController(crearUsuarioView);
    TableroModel tableroModel = new TableroModel();
    TableroController tableroController = new TableroController(tableroModel, tableroView);

    // Crear el mediador y pasarle los controladores y la vista del tablero
    Mediador mediador = new Mediador(crearUsuarioController, tableroController, tableroView);

    // Configurar los controladores para que usen el mediador
    crearUsuarioController.setMediator(mediador);
    tableroController.setMediator(mediador);

    // Mostrar la vista inicial (en este caso, CrearUsuarioView)
    mediador.iniciarAplicacion();
    }
}