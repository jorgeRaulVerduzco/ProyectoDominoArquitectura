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
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaController;
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaModel;
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaView;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroModel;
import PresentacionTableroMVC.TableroView;
import Server.Server;
import java.awt.Frame;
import java.io.IOException;

/**
 *
 * @author INEGI
 */
public class Juego {

    private static final int PUERTO_SERVIDOR = 49674;
    private static Server server;

    public static void main(String[] args) {
        // Iniciar el servidor en un hilo separado
        new Thread(() -> {
            try {
                server = new Server();
                server.iniciarServidor(PUERTO_SERVIDOR);
            } catch (IOException e) {
                System.err.println("Error al iniciar el servidor: " + e.getMessage());
                System.exit(1);
            }
        }).start();

        // Esperar un momento para asegurar que el servidor esté iniciado
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Configurar el look and feel
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
        javax.swing.SwingUtilities.invokeLater(() -> {
            initializeApplication();
        });
    }

    private static void initializeApplication() {
        // Verificar que el servidor esté iniciado
        if (server == null) {
            System.err.println("Error: El servidor no se inició correctamente");
            System.exit(1);
        }

        // Inicializar los componentes del juego
        PozoModel pozoModel = new PozoModel();
        pozoModel.getPozo().inicializarFichas();

        PozoView pozoView = new PozoView(new Frame(), true, pozoModel);
        pozoView.setVisible(false);

        CrearUsuarioView crearUsuarioView = new CrearUsuarioView();
        CrearSalaModel crearSalaModel = new CrearSalaModel();
        CrearSalaView crearSalaView = new CrearSalaView();
        crearSalaView.setModel(crearSalaModel); // Conectar modelo y vista
        crearSalaModel.addObserver(crearSalaView);
        TableroModel tableroModel = new TableroModel();
        TableroView tableroView = new TableroView(new Frame(), true, tableroModel, pozoModel);

        // Inicializar controladores
        CrearUsuarioController crearUsuarioController = new CrearUsuarioController(crearUsuarioView);
        CrearSalaController crearSalaController = new CrearSalaController(crearSalaModel, crearSalaView);
        TableroController tableroController = new TableroController(tableroModel, tableroView);
        UnirseAlaSalaModel unirseAlaSalaModel = new UnirseAlaSalaModel();
        UnirseAlaSalaView unirseAlaSalaView = new UnirseAlaSalaView();
        UnirseAlaSalaController unirseAlaSalaController = new UnirseAlaSalaController(
                unirseAlaSalaModel,
                unirseAlaSalaView
        );
        // Crear el mediador
        Mediador mediador = new Mediador(
                crearUsuarioController,
                crearSalaController,
                crearSalaView,
                tableroController,
                tableroView,
                unirseAlaSalaController
        );
        crearUsuarioView.setMediator(mediador);

        // Configurar el servidor en el mediador y controladores
        mediador.setServer(server);
        crearSalaController.setServer(server);
        // Configurar el mediador en los controladores
        crearUsuarioController.setMediator(mediador);
        crearSalaController.setMediator(mediador);
        // ... resto del código ...
        unirseAlaSalaController.setMediator(mediador);
        unirseAlaSalaController.setServer(server);
        tableroController.setMediator(mediador);

        // Iniciar la aplicación
        mediador.iniciarAplicacion();

        System.out.println("Aplicación iniciada correctamente");

    }
}
