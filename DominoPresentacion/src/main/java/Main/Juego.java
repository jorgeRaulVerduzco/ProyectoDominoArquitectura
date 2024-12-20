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
import Presenctacion.SeleccionJuego.OpcionesDeJuegoView;
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaController;
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaModel;
import Presenctacion.UnirseAlaSalaMVC.UnirseAlaSalaView;
import Presentacion.EsperaMVC.EsperaController;
import Presentacion.EsperaMVC.EsperaModel;
import Presentacion.EsperaMVC.EsperaView;
import PresentacionTableroMVC.TableroController;
import PresentacionTableroMVC.TableroModel;
import PresentacionTableroMVC.TableroView;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.awt.Frame;

/**
 *
 * @author INEGI
 */
public class Juego {

    private Server server;
    private String salaId;
    private ServerComunicacion serverC;

    public static void main(String[] args) {
        Juego juego = new Juego();
        juego.iniciar();
    }

    private void iniciar() {
        // Iniciar el servidor
        server = new Server();

        // Lanzar la aplicación en el Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(this::initializeApplication);
    }

    private void initializeApplication() {
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
        OpcionesDeJuegoView opcionesDeJuegoView = new OpcionesDeJuegoView(new Frame(), true); // Instancia del diálogo
        EsperaModel esperaModel = new EsperaModel();
        EsperaView esperaView = new EsperaView(esperaModel);

        // Inicializar controladores
        CrearUsuarioController crearUsuarioController = new CrearUsuarioController(crearUsuarioView);
        CrearSalaController crearSalaController = new CrearSalaController(crearSalaModel, crearSalaView);
        TableroController tableroController = new TableroController(tableroModel, tableroView, serverC);
        EsperaController esperaController = new EsperaController(esperaModel, esperaView, server, salaId);
        UnirseAlaSalaModel unirseAlaSalaModel = new UnirseAlaSalaModel();
        UnirseAlaSalaView unirseAlaSalaView = new UnirseAlaSalaView(unirseAlaSalaModel, server);
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
                unirseAlaSalaController,
                opcionesDeJuegoView,
                esperaController,
                esperaView
        );
        crearUsuarioView.setMediator(mediador);

        // Configurar el servidor en el mediador y controladores
        mediador.setServer(server);
        opcionesDeJuegoView.setServer(server);
        crearSalaController.setServer(server);

        // Configurar el mediador en los controladores
        crearUsuarioController.setMediator(mediador);
        crearSalaController.setMediator(mediador);
        unirseAlaSalaController.setMediator(mediador);
        unirseAlaSalaController.setServer(server);
        esperaController.setMediator(mediador);
        esperaView.setMediator(mediador);
        tableroController.setMediator(mediador);
        tableroController.setServer(server);
        tableroView.setServer(server);

        // Iniciar la aplicación
        mediador.iniciarAplicacion();

        System.out.println("Aplicación iniciada correctamente");
    }
}
