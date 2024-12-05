/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion.EsperaMVC;

import Presenctacion.Mediador;
import Presenctacion.Observer;
import Presenctacion.PozoMVC.PozoModel;
import PresentacionTableroMVC.TableroModel;
import PresentacionTableroMVC.TableroView;
import Server.Server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Serva
 */
public class EsperaView extends javax.swing.JFrame implements Observer {

    private JList<String> listaJugadores;
    private DefaultListModel<String> jugadoresModel;
    private JLabel lblEstado;
    private JButton btnCancelar;
    private JButton btnIniciarPartida;
    private EsperaModel model;
    private Mediador mediador;
    private Server server;

    public void setServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("El servidor no puede ser nulo.");
        }
        this.server = server;
        System.out.println("Servidor asignado correctamente.");
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    public EsperaView(EsperaModel model) {
        this.model = model;
        model.addObserver(this); // Registra la vista como observador

        setTitle("Esperando Jugadores");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Cambiar el color de fondo de la ventana
        getContentPane().setBackground(new Color(34, 139, 34)); // Color azul oscuro

        jugadoresModel = new DefaultListModel<>();
        listaJugadores = new JList<>(jugadoresModel);
        listaJugadores.setBackground(new Color(34, 139, 34)); // Color azul claro para la lista
        listaJugadores.setForeground(Color.BLACK); // Texto negro
        btnCancelar = new JButton("Cancelar");
        btnIniciarPartida = new JButton("Iniciar Partida");
        btnIniciarPartida.setVisible(false); // Inicialmente no visible

        lblEstado = new JLabel("Esperando más jugadores...", JLabel.CENTER);
        lblEstado.setForeground(Color.WHITE); // Texto blanco
        lblEstado.setFont(new Font("Arial", Font.BOLD, 16)); // Cambia la fuente

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCancelar);
        panelBotones.add(btnIniciarPartida);

        add(new JScrollPane(listaJugadores), BorderLayout.CENTER);
        add(lblEstado, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnCancelar.addActionListener(e -> cancelarEspera());
        btnIniciarPartida.addActionListener(e -> iniciarPartida());
    }

    /**
     * Actualiza la lista de jugadores en la vista.
     */
    public void actualizarJugadores(List<String> jugadores) {
        jugadoresModel.clear();
        for (String jugador : jugadores) {
            jugadoresModel.addElement(jugador);
        }
    }

    TableroModel tableroModel = new TableroModel();
    PozoModel pozoModel = new PozoModel();

    private void iniciarPartida() {
        // Directly create and show the TableroView
        TableroView tableroView = new TableroView(new Frame(), true, tableroModel, pozoModel);
        tableroView.setVisible(true);
        this.dispose(); // Close the current waiting view
    }

    /**
     * Actualiza el estado de la partida en la vista.
     */
    public void actualizarEstado(String estado) {
        lblEstado.setText(estado);
    }

    /**
     * Cancela la espera y regresa a la pantalla anterior.
     */
    private void cancelarEspera() {
        System.out.println("Espera cancelada por el usuario.");
        dispose(); // Cierra esta ventana
        // Aquí puedes redirigir al usuario a la pantalla anterior
    }

    @Override
    public void update() {
        // Actualiza la lista de jugadores
        actualizarJugadores(model.getJugadoresConectados());

        // Verifica si hay más de un jugador y muestra el botón de iniciar
        if (model.getJugadoresConectados().size() > 1) {
            btnIniciarPartida.setVisible(true); // Hace visible el botón
            actualizarEstado("Listo para iniciar la partida.");
        } else {
            btnIniciarPartida.setVisible(false); // Esconde el botón si hay menos de 2 jugadores
            actualizarEstado("Esperando más jugadores...");
        }
    }

    public static void main(String[] args) {
        // Crear modelo
        EsperaModel model = new EsperaModel();

        // Crear y mostrar la vista
        EsperaView view = new EsperaView(model);
        view.setVisible(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            private int jugadorCount = 1;

            @Override
            public void run() {
                if (jugadorCount <= 4) {
                    model.agregarJugador("Jugador " + jugadorCount); // Verifica que este método exista
                    jugadorCount++;
                } else {
                    model.iniciarPartida(); // Verifica que este método exista
                    timer.cancel();
                }
            }
        }, 0, 2000); // Repite cada 2 segundos
    }
}
