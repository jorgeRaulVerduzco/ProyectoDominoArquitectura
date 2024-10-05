/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Serva
 */
public class Tablero extends javax.swing.JFrame {

    private boolean isDragging = false; // Estado de arrastre
    private Point dragStartPoint;
    private TableroModel tableroModel; // Instancia del modelo
    private Ficha fichaSeleccionada; // Para guardar la ficha seleccionada
    private int indiceSeleccionado = -1; // Para guardar el índice de la ficha seleccionada

    public Tablero() {
        initComponents();
        tableroModel = new TableroModel(); // Inicializar el modelo
        cargarFichas();
        mostrarFichasEnTablero();
    }

    private void cargarFichas() {
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            int ladoIzquierdo = random.nextInt(7);
            int ladoDerecho = random.nextInt(7);
            Ficha nuevaFicha = new Ficha(ladoIzquierdo, ladoDerecho);
            tableroModel.agregarFicha(nuevaFicha, "derecho"); // Siempre agregar al final
        }

    }

    private void mostrarFichasEnTablero() {
        this.setLayout(new FlowLayout());
        this.getContentPane().removeAll(); // Limpiar el panel anterior

        for (int i = 0; i < tableroModel.getFichasTablero().size(); i++) {
            Ficha ficha = tableroModel.getFichasTablero().get(i);
            JPanel panelFicha = crearPanelFicha(ficha);
            int index = i;

            // Agregar MouseListener para detectar clics en la ficha
            panelFicha.addMouseListener(new MouseAdapter() {
                private JPanel panelFichaArrastrada;

                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1) { // Click izquierdo para seleccionar
                        fichaSeleccionada = tableroModel.getFichasTablero().get(index); // Guardar la ficha seleccionada
                        indiceSeleccionado = index; // Guardar su índice
                        isDragging = true; // Iniciar arrastre
                        dragStartPoint = e.getPoint(); // Guardar el punto inicial

                        // Guarda la referencia al panel de ficha
                        panelFichaArrastrada = (JPanel) e.getSource();
                    } else if (e.getButton() == MouseEvent.BUTTON3 && indiceSeleccionado != -1) { // Click derecho para mover
                        moverFicha(indiceSeleccionado, index); // Mover la ficha seleccionada a la posición actual
                        fichaSeleccionada = null; // Restablecer la selección
                        indiceSeleccionado = -1;
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isDragging && fichaSeleccionada != null && panelFichaArrastrada != null) {
                        // Actualiza la posición del panel de ficha arrastrada
                        panelFichaArrastrada.setLocation(e.getXOnScreen() - dragStartPoint.x, e.getYOnScreen() - dragStartPoint.y);
                        panelFichaArrastrada.setVisible(true);
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isDragging) {
                        isDragging = false; // Detener arrastre
                        // Determinar el índice de destino basado en la posición
                        int indexDestino = calcularIndiceDestino(e.getX(), e.getY());

                        if (indexDestino != -1 && indexDestino != indiceSeleccionado) {
                            moverFicha(indiceSeleccionado, indexDestino); // Mover la ficha a la nueva posición
                        } else {
                            // Si no hay un destino válido, puedes optar por devolver la ficha a su lugar original si lo deseas.
                            mostrarFichasEnTablero(); // Volver a mostrar las fichas
                        }
                        fichaSeleccionada = null; // Limpiar la selección
                        indiceSeleccionado = -1; // Reiniciar el índice seleccionado
                        panelFichaArrastrada = null; // Limpiar la referencia del panel arrastrado
                    }
                }

                private int calcularIndiceDestino(int x, int y) {
                    for (int i = 0; i < tableroModel.getFichasTablero().size(); i++) {
                        // Calcula la posición de cada panel de ficha
                        JPanel panelFicha = crearPanelFicha(tableroModel.getFichasTablero().get(i));
                        panelFicha.setSize(panelFicha.getPreferredSize()); // Establece el tamaño del panel
                        panelFicha.setLocation(i * 130, 0); // Ajusta según cómo se alinean las fichas

                        // Si el mouse está dentro de los límites del panel
                        if (x >= panelFicha.getX() && x <= panelFicha.getX() + panelFicha.getWidth()
                                && y >= panelFicha.getY() && y <= panelFicha.getY() + panelFicha.getHeight()) {
                            return i; // Devuelve el índice de la ficha que está bajo el mouse
                        }
                    }
                    return -1; // No se encontró un índice válido
                }
            });

            this.add(panelFicha);
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
        String rutaBase = "C:\\Users\\INEGI\\Documents\\NetBeansProjects\\ProyectoDominoArquitectura\\DominoPresentacion\\src\\imagenes";
        String rutaImagen = rutaBase + valor + ".png";
        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() == -1) {
            System.out.println("Imagen no encontrada: " + rutaImagen);
        }
        return icon;
    }

    public void moverFicha(int indexOrigen, int indexDestino) {
        if (indexOrigen < 0 || indexOrigen >= tableroModel.getFichasTablero().size()
                || indexDestino < 0 || indexDestino >= tableroModel.getFichasTablero().size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites del tablero.");
        }

        // Mover la ficha en el modelo
        tableroModel.moverFicha(indexOrigen, indexDestino);

        // Actualizar la visualización
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
            .addGap(0, 718, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 369, Short.MAX_VALUE)
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
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Tablero.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Tablero().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
