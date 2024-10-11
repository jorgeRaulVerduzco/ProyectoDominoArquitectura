/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Serva
 */
public class TableroView extends javax.swing.JFrame {

    private TableroModel tableroModel;
    private TableroController tableroController;

    private List<Ficha> fichasJugadores1;
    private List<Ficha> fichasJugadores2;

    public TableroView() {
        initComponents();
        setBackground(Color.GREEN);
        getContentPane().setBackground(Color.GREEN);
        tableroModel = new TableroModel(); // Inicializar el modelo
        tableroController = new TableroController(tableroModel, this); // Inicializar el controlador
        cargarFichas();
        inicializarPozo();
        repartirFichas(); // Llamada al método para repartir las fichas
        mostrarFichasEnTablero();
    }

    private void cargarFichas() {
        Random random = new Random();

        // Inicializa una lista para controlar las fichas generadas
        List<Ficha> fichasGeneradas = new ArrayList<>();

        for (int i = 0; i < 7; i++) { // Genera 7 fichas
            int ladoIzquierdo;
            int ladoDerecho;

            // Genera un par de lados válidos
            if (tableroModel.getFichasTablero().isEmpty()) {
                ladoIzquierdo = random.nextInt(7); // Genera un número entre 0 y 6
                ladoDerecho = random.nextInt(7); // Genera un número entre 0 y 6
            } else {
                // Obtiene el extremo derecho y asegura coincidencia
                Ficha fichaDerecha = tableroModel.obtenerExtremoDerecho();
                ladoIzquierdo = fichaDerecha.getEspacio2(); // Asegurando coincidencia
                ladoDerecho = random.nextInt(7); // Lado derecho puede ser cualquier número del 0 al 6
            }

            // Crea una nueva ficha
            Ficha nuevaFicha = new Ficha(ladoIzquierdo, ladoDerecho);

            // Verifica que la ficha no se haya generado anteriormente
            if (!fichasGeneradas.contains(nuevaFicha)) {
                fichasGeneradas.add(nuevaFicha); // Agrega a la lista de fichas generadas
                tableroModel.agregarFicha(nuevaFicha, "derecho"); // Siempre agregar al final
            } else {
                i--; // Decrementa el contador para volver a intentar en caso de ficha duplicada
            }
        }
    }

    private void inicializarPozo() {
        // Inicializar el pozo con 28 fichas
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) {
                Ficha ficha = new Ficha(i, j);
                tableroModel.agregarFicha(ficha, "pozo"); // Añadir ficha al pozo
            }
        }
    }

    private void repartirFichas() {
        Random random = new Random();
        List<Ficha> fichas = new ArrayList<>(tableroModel.getFichasTablero());

        // Verifica si hay suficientes fichas para repartir
        if (fichas.size() < 14) { // Necesitas 14 fichas para repartir 7 a cada jugador
            throw new IllegalArgumentException("No hay suficientes fichas para repartir a los jugadores.");
        }

        // Seleccionar 7 fichas aleatorias para el jugador 1
        fichasJugadores1 = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(fichas.size());
            fichasJugadores1.add(fichas.remove(index)); // Quita la ficha de la lista original
        }

        // Seleccionar 7 fichas aleatorias para el jugador 2
        fichasJugadores2 = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(fichas.size());
            fichasJugadores2.add(fichas.remove(index)); // Quita la ficha de la lista original
        }

        // Actualiza el pozo con las fichas restantes
        tableroModel.setFichasPozo(fichas);
    }

    private void mostrarFichasEnTablero() {
        this.setLayout(null); // Usar layout nulo para controlar la posición manualmente
        this.getContentPane().removeAll(); // Limpiar el panel anterior

        for (int i = 0; i < fichasJugadores1.size(); i++) {
            Ficha ficha = fichasJugadores1.get(i);
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(i * 130, 0, 100, 50); // Posición para el jugador 1
            this.add(panelFicha); // Añadir el panel de la ficha al contenedor
        }

        // Mostrar fichas del jugador 2 en la fila inferior
        for (int i = 0; i < fichasJugadores2.size(); i++) {
            Ficha ficha = fichasJugadores2.get(i);
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(i * 130, 70, 100, 50); // Posición para el jugador 2
            this.add(panelFicha); // Añadir el panel de la ficha al contenedor
        }

        for (int i = 0; i < tableroModel.getFichasTablero().size(); i++) {
            Ficha ficha = tableroModel.getFichasTablero().get(i);
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFicha.setBounds(i * 130, 0, 100, 50); // Establecer tamaño y posición inicial
            int index = i;

            // Agregar MouseListener para detectar clics en la ficha
            panelFicha.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) { // Click izquierdo para seleccionar
                        if (!tableroController.isFichaColocada()) { // Check if a ficha is already placed
                            tableroController.iniciarArrastreFicha(index, e.getPoint());
                        }
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    tableroController.detenerArrastreFicha();
                }
            });

            // Agregar MouseMotionListener para permitir el arrastre de la ficha
            panelFicha.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    tableroController.moverFichaArrastrada(e);
                }
            });

            this.add(panelFicha); // Añadir el panel de la ficha al contenedor
        }

        this.revalidate();
        this.repaint();
    }

    private JPanel crearPanelFicha(Ficha ficha) {
        JPanel panelFicha = new JPanel();
        panelFicha.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelFicha.setPreferredSize(new Dimension(120, 60));

        ImageIcon ladoIzquierdo = cargarImagenPorValor(ficha.getEspacio1());
        ImageIcon ladoDerecho = cargarImagenPorValor(ficha.getEspacio2());

        JLabel labelIzquierdo = new JLabel(ladoIzquierdo);
        JLabel labelDerecho = new JLabel(ladoDerecho);

        panelFicha.add(labelIzquierdo);
        panelFicha.add(labelDerecho);

        return panelFicha;
    }

    private ImageIcon cargarImagenPorValor(int valor) {
        String rutaBase = "C:\\Users\\Serva\\Downloads\\ProyectoDominoArquitectura-main\\ProyectoDominoArquitectura-main\\ProyectoDominoArquitectura-main\\DominoPresentacion\\src\\imagenes\\";
        String rutaImagen = rutaBase + valor + ".png";
        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() == -1) {
            System.out.println("Imagen no encontrada: " + rutaImagen);
        }
        return icon;
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
            .addGap(0, 437, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableroView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TableroView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
