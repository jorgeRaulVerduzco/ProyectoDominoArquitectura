/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import Presenctacion.PozoMVC.PozoModel;
import Presenctacion.PozoMVC.PozoView;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
public class TableroView extends javax.swing.JFrame {

    private JButton btnAbrirPozo;
    private JButton btnPasarTurno;
    private JButton btnTerminarJuego;
    private TableroModel tableroModel;
    private TableroController tableroController;
    private PozoModel pozoModel;
    private List<Ficha> fichasJugadores1;
    private List<Ficha> fichasJugadores2;
    private Ficha fichaSeleccionada;
    private int jugadorActual = 1; // 1 para jugador 1, 2 para jugador 2
    private PozoView pozoView;

    public TableroView(Frame parent, boolean modal, TableroModel tableroModel, PozoModel pozoModel) {
        this.pozoModel = pozoModel;
        this.tableroModel = tableroModel;
        initComponents();
        setBackground(Color.GREEN);
        getContentPane().setBackground(Color.GREEN);
        tableroController = new TableroController(tableroModel, this);
        this.pozoView = new PozoView(this, true, pozoModel);

        repartirFichas();
        mostrarFichasEnTablero();
        agregarPanelBotones();
    }

    private void agregarPanelBotones() {
        // Crear un JPanel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS)); // Organizar verticalmente

        // Crear botones
        btnAbrirPozo = new JButton("Abrir Pozo");
        btnPasarTurno = new JButton("Pasar Turno");
        btnTerminarJuego = new JButton("Terminar Juego");

        // Configurar colores
        btnAbrirPozo.setBackground(Color.BLUE);
        btnAbrirPozo.setForeground(Color.WHITE);
        btnPasarTurno.setBackground(Color.GRAY);  // Color neutral para "Pasar Turno"
        btnPasarTurno.setForeground(Color.WHITE);
        btnTerminarJuego.setBackground(Color.RED);
        btnTerminarJuego.setForeground(Color.WHITE);

        // Añadir botones al panel
        panelBotones.add(btnAbrirPozo);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        panelBotones.add(btnPasarTurno);
        panelBotones.add(Box.createVerticalStrut(10)); // Espacio entre botones
        panelBotones.add(btnTerminarJuego);

        // Establecer ubicación del panel (más a la derecha)
        panelBotones.setBounds(this.getWidth() - 150, 70, 130, 200);  // Ajusta la coordenada X (más a la derecha)

        // Añadir el panel a la ventana principal
        this.setLayout(null); // Usar layout nulo para posicionamiento absoluto
        this.add(panelBotones);

        // Añadir listeners para las acciones de los botones
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
        if (jugadorActual == 1) {
            fichasJugadores1.add(ficha);
        } else {
            fichasJugadores2.add(ficha);
        }
    }

    private void pasarTurno() {
        // Lógica para pasar el turno
        cambiarTurno();
        mostrarFichasEnTablero();
    }

    private void terminarJuego() {
        // Lógica para terminar el juego
        JOptionPane.showMessageDialog(this, "El juego ha terminado.");
        System.exit(0);  // Terminar el juego y cerrar la aplicación
    }

    private void inicializarPozo() {
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                Ficha ficha = new Ficha(i, j);
                pozoModel.getFichasPozo().add(ficha);
            }
        }
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

        // Actualizar el pozo con las fichas restantes
        pozoModel.setFichasPozo(fichas);
    }

    private void mostrarFichasEnTablero() {
        // Mantener el panel de botones
        Component[] components = this.getContentPane().getComponents();
        boolean panelBotonesVisible = false;

        // Eliminar solo componentes de fichas y del tablero
        for (Component component : components) {
            // Verificar si el componente es un JPanel y que no sea nulo
            if (component instanceof JPanel) {
                String componentName = component.getName();
                // Eliminar componentes que no son el panel de botones
                if (componentName == null || !componentName.equals("panelBotones")) {
                    this.getContentPane().remove(component);
                }
            }
            // Asegurarse de que el panel de botones siga estando visible
            if (!panelBotonesVisible) {
                agregarPanelBotones(); // Solo agregar si no está visible
            }
        }

        // Mostrar fichas en el tablero
        mostrarFichasJugador(fichasJugadores1, 1);
        mostrarFichasJugador(fichasJugadores2, 2);
        mostrarTablero();

        // Redibujar la interfaz
        this.revalidate();
        this.repaint();
    }

    private void mostrarFichasJugador(List<Ficha> fichasJugador, int numJugador) {
        int y = numJugador == 1 ? 10 : this.getHeight() - 70;
        for (int i = 0; i < fichasJugador.size(); i++) {
            Ficha ficha = fichasJugador.get(i);
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(i * 110, y, 100, 50);
            final int index = i;
            panelFicha.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (jugadorActual == numJugador) {
                        seleccionarFicha(ficha, index, numJugador);
                    }
                }
            });
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

        // Aquí colocamos las fichas
        List<Ficha> fichasTablero = tableroModel.getFichasTablero();
        int x = 10;  // Comenzamos en un margen
        for (Ficha ficha : fichasTablero) {
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(x, 25, 100, 50);  // Centrado en el panel
            tableroPanel.add(panelFicha);
            x += 110;  // Espaciado entre fichas
        }

        // Establecer un límite para el tamaño del tablero
        int maxPanelWidth = this.getWidth() - 20; // Ancho máximo permitido
        if (x > maxPanelWidth) {
            x = maxPanelWidth; // Limitar el ancho si es necesario
        }

        // Ajustar el tamaño del tablero
        tableroPanel.setPreferredSize(new Dimension(x, 200)); // Ajustamos el tamaño a las fichas
        tableroPanel.setSize(new Dimension(x, 200)); // Establecer el tamaño del panel basado en el ancho calculado

        // Asegúrate de que el panel se ajuste a la ventana
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

    private void seleccionarFicha(Ficha ficha, int index, int numJugador) {
        fichaSeleccionada = ficha;
        // Añadir efecto visual para la ficha seleccionada
    }

    private void colocarFichaEnTablero(Point point) {
        if (fichaSeleccionada != null) {
            // Pedir al jugador que elija el lado donde quiere colocar la ficha
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

            String lado = (eleccion == 0) ? "izquierdo" : "derecho"; // Elección del jugador

            // Colocar la ficha en el lado seleccionado
            if (tableroController.colocarFichaEnTablero(fichaSeleccionada, lado)) {
                // Eliminar la ficha seleccionada de las fichas del jugador actual
                if (jugadorActual == 1) {
                    fichasJugadores1.remove(fichaSeleccionada);
                } else {
                    fichasJugadores2.remove(fichaSeleccionada);
                }

                fichaSeleccionada = null;  // Limpiar la selección de ficha
                cambiarTurno();  // Cambiar el turno al otro jugador
                mostrarFichasEnTablero();  // Actualizar el tablero con las nuevas fichas
                verificarFinJuego();  // Verificar si el juego ha terminado
            } else {
                // Mensaje en caso de que no se pueda colocar la ficha
                JOptionPane.showMessageDialog(this, "No se puede colocar la ficha en el lado seleccionado.");
            }
        }
    }

    private void cambiarTurno() {
        jugadorActual = jugadorActual == 1 ? 2 : 1;
    }

    private void verificarFinJuego() {
        if (fichasJugadores1.isEmpty() || fichasJugadores2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Juego terminado! Ganó el jugador " + jugadorActual);
            // Aquí puedes añadir lógica para reiniciar el juego si lo deseas
        }
    }

    private ImageIcon cargarImagenPorValor(int valor) {
        String rutaBase = "C:\\Users\\Serva\\Downloads\\ProyectoDominoArquitectura-main\\ProyectoDominoArquitectura-main\\DominoPresentacion\\src\\imagenes\\";
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
        java.awt.EventQueue.invokeLater(() -> {
            PozoModel pozoModel = new PozoModel(); // Crear un modelo de pozo
            pozoModel.getPozo().inicializarFichas(); // Asegúrate de inicializar las fichas aquí

            PozoView pozoView = new PozoView(new Frame(), true, pozoModel);
            pozoView.setVisible(false); // Mantener el pozo invisible

            TableroModel tableroModel = new TableroModel(); // Crear un modelo de tablero
            TableroView tableroView = new TableroView(new Frame(), true, tableroModel, pozoModel);

            // Repartir fichas a los jugadores
            tableroView.repartirFichas(); // Asegúrate de que este método se ejecute después de inicializar el pozo

            tableroView.setVisible(true); // Mostrar la vista del tablero
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
