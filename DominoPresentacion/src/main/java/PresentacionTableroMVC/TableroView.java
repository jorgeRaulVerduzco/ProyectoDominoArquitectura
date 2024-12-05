/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import Dominio.Jugador;
import Presenctacion.Observer;
import Presenctacion.PozoMVC.PozoModel;
import Presenctacion.PozoMVC.PozoView;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Serva
 */
public class TableroView extends javax.swing.JFrame implements Observer {

    private JButton btnAbrirPozo;
    private JButton btnPasarTurno;
    private JButton btnTerminarJuego;
    private TableroModel tableroModel;
    private TableroController tableroController;
    private PozoModel pozoModel;
    private List<Ficha> fichasJugadores1;
    private List<Ficha> fichasJugadores2;
    private Ficha fichaSeleccionada;
    private TableroModel model;

    private PozoView pozoView;
    private ServerComunicacion serverComunicacion;
    private Server server;

    private Jugador jugadorActual;

    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
    }

    public TableroView(Frame parent, boolean modal, TableroModel tableroModel, PozoModel pozoModel) {
        this.pozoModel = pozoModel;
        this.tableroModel = tableroModel;
        initComponents();
        setBackground(Color.GREEN);
        getContentPane().setBackground(Color.GREEN);
        tableroController = new TableroController(tableroModel, this, serverComunicacion);
        this.pozoView = new PozoView(this, true, pozoModel);
        tableroModel.addObserver(this);
        repartirFichas();
        mostrarFichasEnTablero();
        agregarPanelBotones();
        this.setServer(server);
    }
    
    public void setServer(Server server) {
    this.server = server;
}

    public Server getServer() {
        return server;
    }
    

    private void agregarPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));

        btnAbrirPozo = new JButton("Abrir Pozo");
        btnPasarTurno = new JButton("Pasar Turno");
        btnTerminarJuego = new JButton("Terminar Juego");

        btnAbrirPozo.setBackground(Color.BLUE);
        btnAbrirPozo.setForeground(Color.WHITE);
        btnPasarTurno.setBackground(Color.GRAY);
        btnPasarTurno.setForeground(Color.WHITE);
        btnTerminarJuego.setBackground(Color.RED);
        btnTerminarJuego.setForeground(Color.WHITE);

        panelBotones.add(btnAbrirPozo);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnPasarTurno);
        panelBotones.add(Box.createVerticalStrut(10));
        panelBotones.add(btnTerminarJuego);

        panelBotones.setBounds(this.getWidth() - 150, 70, 130, 200);
        this.setLayout(null);
        this.add(panelBotones);

        btnAbrirPozo.addActionListener(e -> abrirPozo());
        btnPasarTurno.addActionListener(e -> pasarTurno());
        btnTerminarJuego.addActionListener(e -> terminarJuego());
    }

    private void abrirPozo() {
        if (tableroModel.getFichasTablero().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debes colocar una ficha en el tablero antes de tomar del pozo.");
            return;
        }

        pozoView.actualizarFichasPozo(pozoModel.getFichasPozo());
        pozoView.setVisible(true);

        pozoView.setFichaSeleccionadaListener(ficha -> {
            if (ficha != null) {
                agregarFichaAJugadorActual(ficha);
                pozoModel.getFichasPozo().remove(ficha);
                pozoView.actualizarFichasPozo(pozoModel.getFichasPozo());
                mostrarFichasEnTablero();
            }
        });
    }

    private void agregarFichaAJugadorActual(Ficha ficha) {
        if (jugadorActual == null) {
            return;
        }

        if (jugadorActual.getPuntuacion() == 1) {
            fichasJugadores1.add(ficha);
        } else {
            fichasJugadores2.add(ficha);
        }
    }

    private void pasarTurno() {
        cambiarTurno();
        mostrarFichasEnTablero();
    }

    private void terminarJuego() {
        JOptionPane.showMessageDialog(this, "El juego ha terminado.");
        System.exit(0);
    }

    private void repartirFichas() {
        Random random = new Random();
        List<Ficha> fichas = new ArrayList<>(pozoModel.getFichasPozo());

        fichasJugadores1 = new ArrayList<>();
        fichasJugadores2 = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            fichasJugadores1.add(fichas.remove(random.nextInt(fichas.size())));
            fichasJugadores2.add(fichas.remove(random.nextInt(fichas.size())));
        }

        pozoModel.setFichasPozo(fichas);
    }

    private void mostrarFichasEnTablero() {
        Component[] components = this.getContentPane().getComponents();
        boolean panelBotonesVisible = false;

        for (Component component : components) {
            if (component instanceof JPanel) {
                String componentName = component.getName();
                if (componentName == null || !componentName.equals("panelBotones")) {
                    this.getContentPane().remove(component);
                }
            }
            if (!panelBotonesVisible) {
                agregarPanelBotones();
            }
        }

        mostrarFichasJugador(fichasJugadores1, 1);
        mostrarFichasJugador(fichasJugadores2, 2);
        mostrarTablero();

        this.revalidate();
        this.repaint();
    }

    private void mostrarFichasJugador(List<Ficha> fichasJugador, int numJugador) {
        int y = numJugador == 1 ? 10 : this.getHeight() - 70;
        for (int i = 0; i < fichasJugador.size(); i++) {
            Ficha ficha = fichasJugador.get(i);
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(i * 110, y, 100, 50);
            habilitarArrastrarYSoltarFicha(panelFicha, ficha);
            this.add(panelFicha);
        }
    }

    private void mostrarTablero() {
        JPanel tableroPanel = new JPanel();
        tableroPanel.setLayout(null);
        tableroPanel.setBounds(0, 100, this.getWidth(), 200);
        tableroPanel.setBackground(Color.LIGHT_GRAY);
        tableroPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (fichaSeleccionada != null) {
                    colocarFichaEnTablero(e.getPoint());
                }
            }
        });
        this.add(tableroPanel);

        List<Ficha> fichasTablero = tableroModel.getFichasTablero();
        int x = 10;
        for (Ficha ficha : fichasTablero) {
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(x, 25, 100, 50);
            tableroPanel.add(panelFicha);
            x += 110;
        }

        int maxPanelWidth = this.getWidth() - 20;
        if (x > maxPanelWidth) {
            x = maxPanelWidth;
        }

        tableroPanel.setPreferredSize(new Dimension(x, 200));
        tableroPanel.setSize(new Dimension(x, 200));

        this.add(tableroPanel);
        this.revalidate();
        this.repaint();
    }

    private JPanel crearPanelFicha(Ficha ficha) {
        JPanel panelFicha = new JPanel(new GridLayout(1, 2));
        JLabel label1 = new JLabel(cargarImagenPorValor(ficha.getEspacio1()));
        JLabel label2 = new JLabel(cargarImagenPorValor(ficha.getEspacio2()));
        panelFicha.add(label1);
        panelFicha.add(label2);
        panelFicha.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return panelFicha;
    }

    private void colocarFichaEnTablero(Point point) {
        if (fichaSeleccionada != null) {
            String[] opciones = {"Izquierdo", "Derecho"};
            int eleccion = JOptionPane.showOptionDialog(
                    this,
                    "¿En qué lado deseas colocar la ficha?",
                    "Elegir Lado",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            String lado = (eleccion == 0) ? "izquierdo" : "derecho";

            if (tableroController.colocarFichaEnTablero(fichaSeleccionada, lado)) {
                if (jugadorActual != null) {
                    if (jugadorActual.getPuntuacion() == 1) {
                        fichasJugadores1.remove(fichaSeleccionada);
                    } else {
                        fichasJugadores2.remove(fichaSeleccionada);
                    }
                }

                fichaSeleccionada = null;
                cambiarTurno();
                mostrarFichasEnTablero();
                cambiarTurno();

//                verificarFinJuego();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo colocar la ficha.");
            }
        }
    }



    private void cambiarTurno() {
        if (jugadorActual.getPuntuacion() == 1) {
            jugadorActual = new Jugador("2"); // Cambiar a jugador 2
        } else {
            jugadorActual = new Jugador("1"); // Cambiar a jugador 1
        }
    }

    private ImageIcon cargarImagenPorValor(int valor) {
        String rutaBase = "C:\\Users\\Serva\\Downloads\\ProyectoDominoArquitectura-CaminoColocarFicha (1)\\ProyectoDominoArquitectura-CaminoColocarFicha\\imagenes\\";
        String rutaImagen = rutaBase + valor + ".png";
        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() == -1) {
            System.out.println("Imagen no encontrada: " + rutaImagen);
            return null;
        }
        int width = 50;  // Ancho constante
        int height = 50;  // Alto constante
        return new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }

    public void actualizarVista() {
        mostrarFichasEnTablero();
    }
    
    
    private void habilitarArrastrarYSoltarFicha(JPanel panelFicha, Ficha ficha) {
    panelFicha.addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            JPanel panel = (JPanel) e.getSource();
            int newX = panel.getX() + e.getX();
            int newY = panel.getY() + e.getY();
            panel.setLocation(newX, newY);
        }
    });

   panelFicha.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseReleased(MouseEvent e) {
        Point dropPoint = e.getPoint();
        if (esZonaValidaParaColocar(dropPoint)) {
            // Obtener el socket del jugador
            Socket socketJugador = server.getSocketJugador(jugadorActual); // Suponiendo que tienes un método para esto
            
            if (socketJugador != null) {
                // Llamar al método con el socket del jugador
                tableroController.procesarJugadaArrastrada(ficha, obtenerLadoSegunZona(dropPoint), socketJugador);
            } else {
                JOptionPane.showMessageDialog(TableroView.this, "No se pudo obtener el socket del jugador.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(TableroView.this, "No puedes colocar la ficha aquí.", "Jugada inválida", JOptionPane.ERROR_MESSAGE);
        }
    }
});
    }

private boolean esZonaValidaParaColocar(Point dropPoint) {
    // Define las zonas válidas para los lados izquierdo y derecho del tablero.
    int margenIzquierdo = 50;
    int margenDerecho = this.getWidth() - 150;

    return dropPoint.x <= margenIzquierdo || dropPoint.x >= margenDerecho;
}

private String obtenerLadoSegunZona(Point dropPoint) {
    int margenIzquierdo = 50;
    return dropPoint.x <= margenIzquierdo ? "izquierdo" : "derecho";
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 881, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 535, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(() -> {
//            PozoModel pozoModel = new PozoModel(); // Crear un modelo de pozo
//            pozoModel.getPozo().inicializarFichas(); // Asegúrate de inicializar las fichas aquí
//
//            PozoView pozoView = new PozoView(new Frame(), true, pozoModel);
//            pozoView.setVisible(false); // Mantener el pozo invisible
//
//            TableroModel tableroModel = new TableroModel(); // Crear un modelo de tablero
//            TableroView tableroView = new TableroView(new Frame(), true, tableroModel, pozoModel);
//
//            // Repartir fichas a los jugadores
//            tableroView.repartirFichas(); // Asegúrate de que este método se ejecute después de inicializar el pozo
//
//            tableroView.setVisible(true); // Mostrar la vista del tablero
//        });
    }

    @Override
    public void update() {
       mostrarFichasEnTablero();
       mostrarTablero();
               
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
