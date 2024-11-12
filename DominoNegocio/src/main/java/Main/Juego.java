/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Main;

import Presenctacion.CrearSalaMVC.CrearSalaController;
import Presenctacion.CrearSalaMVC.CrearSalaModel;
import Presenctacion.CrearSalaMVC.CrearSalaView;
import Presenctacion.Mediador;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioController;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioView;
import Presenctacion.PozoMVC.PozoModel;
import Presenctacion.PozoMVC.PozoView;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroModel;
import PresentacionTableroMVC.TableroView;
import java.awt.Frame;

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
                if ("Nimbus".equals(info.getName())) {
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
        // Crear modelo y vista del pozo
        PozoModel pozoModel = new PozoModel();
        pozoModel.getPozo().inicializarFichas();
        PozoView pozoView = new PozoView(new Frame(), true, pozoModel);
        pozoView.setVisible(false); // Mantener el pozo invisible inicialmente

        // Crear vista de usuario
        CrearUsuarioView crearUsuarioView = new CrearUsuarioView();

        // Crear modelo y vista de la sala
        CrearSalaModel crearSalaModel = new CrearSalaModel();
        CrearSalaView crearSalaView = new CrearSalaView();

        // Crear modelo y vista del tablero
        TableroModel tableroModel = new TableroModel();
        TableroView tableroView = new TableroView(new Frame(), true, tableroModel, pozoModel);

        // Crear controladores
        CrearUsuarioController crearUsuarioController = new CrearUsuarioController(crearUsuarioView);
        CrearSalaController crearSalaController = new CrearSalaController(crearSalaModel, crearSalaView);
        TableroController tableroController = new TableroController(tableroModel, tableroView);

        // Crear el mediador y pasarle los controladores y vistas
        Mediador mediador = new Mediador(
                crearUsuarioController, 
                crearSalaController, 
                crearSalaView, 
                tableroController, 
                tableroView
        );

        // Configurar los controladores para que usen el mediador
        crearUsuarioController.setMediator(mediador);
        crearSalaController.setMediator(mediador);
        tableroController.setMediator(mediador);

        // Iniciar la aplicación mostrando la vista de creación de usuario
        mediador.iniciarAplicacion();
    }
}