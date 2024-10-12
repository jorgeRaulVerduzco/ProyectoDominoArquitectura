/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Presenctacion.PozoMVC;

import Dominio.Ficha;
import Negocio.ServicioPozo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author INEGI
 */
public class PozoView extends javax.swing.JDialog {
//eso es aviso
        private Consumer<Ficha> fichaSeleccionadaListener;

    private PozoModel pozoModel;
    private PozoController pozoController;

    public PozoView(Frame parent, boolean modal, PozoModel pozoModel) {
        super(parent, modal);
        this.pozoModel = pozoModel;
            this.pozoController = new PozoController(pozoModel, this, new ServicioPozo()); // Inicializar con ServicioPozo

        setBackground(Color.GREEN);
        getContentPane().setBackground(Color.GREEN);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(parent);
        initComponents();
        mostrarFichasEnPozo();
    }

    public void setController(PozoController pozoController) {
        this.pozoController = pozoController;
    }

    // Método para mostrar fichas en el pozo, si es necesario
    public void mostrarFichasEnPozo() {
        this.getContentPane().removeAll();
        List<Ficha> fichas = pozoModel.getFichasPozo();

        // Título del pozo
        JLabel titulo = new JLabel("Fichas en el Pozo: " + fichas.size());
        titulo.setFont(titulo.getFont().deriveFont(18.0f));
        this.getContentPane().add(titulo, BorderLayout.NORTH); // Añadir título

        JPanel panelFichas = new JPanel();
        panelFichas.setLayout(new FlowLayout());

        for (Ficha ficha : fichas) {
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFichas.add(panelFicha);
        }

        this.getContentPane().add(panelFichas, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
  public void setFichaSeleccionadaListener(Consumer<Ficha> listener) {
        this.fichaSeleccionadaListener = listener;
    }
    private JPanel crearPanelFicha(Ficha ficha) {
        JPanel panelFicha = new JPanel();
        panelFicha.setPreferredSize(new Dimension(120, 60));
        ImageIcon ladoIzquierdo = cargarImagenPorValor(ficha.getEspacio1());
        ImageIcon ladoDerecho = cargarImagenPorValor(ficha.getEspacio2());
        panelFicha.add(new JLabel(ladoIzquierdo));
        panelFicha.add(new JLabel(ladoDerecho));

        // Añadir evento de clic para eliminar la ficha
        panelFicha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (pozoController != null) {
                    pozoController.eliminarFicha(ficha);
                    mostrarFichasEnPozo(); // Actualiza la vista después de eliminar
                } else {
                    System.err.println("Error: PozoController no ha sido inicializado.");
                }
            }
        });

        panelFicha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (fichaSeleccionadaListener != null) {
                    fichaSeleccionadaListener.accept(ficha);
                    setVisible(false); // Cierra la ventana del pozo después de seleccionar
                }
            }
        });
        return panelFicha;
    }

    public void actualizarFichasPozo(List<Ficha> fichasPozo) {
        this.getContentPane().removeAll();

        JLabel titulo = new JLabel("Fichas en el Pozo: " + fichasPozo.size());
        titulo.setFont(titulo.getFont().deriveFont(18.0f));
        this.getContentPane().add(titulo, BorderLayout.NORTH);

        JPanel panelFichas = new JPanel();
        panelFichas.setLayout(new FlowLayout());

        for (Ficha ficha : fichasPozo) {
            JPanel panelFicha = crearPanelFicha(ficha);
            panelFichas.add(panelFicha);
        }

        this.getContentPane().add(panelFichas, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }

    void mostrarMensaje(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje);
    }

    private ImageIcon cargarImagenPorValor(int valor) {
        String rutaBase = "C:\\Users\\INEGI\\Documents\\NetBeansProjects\\ProyectoDominoArquitectura\\DominoPresentacion\\src\\imagenes\\";
        String rutaImagen = rutaBase + valor + ".png";
        ImageIcon icon = new ImageIcon(rutaImagen);
        if (icon.getIconWidth() == -1) {
            System.out.println("Imagen no encontrada para valor: " + valor + " en ruta: " + rutaImagen);
        }
        return icon;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new java.awt.FlowLayout());

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            PozoModel model = new PozoModel(); // Se crea un modelo vacío para pruebas
            PozoView view = new PozoView(new Frame(), true, model);
            view.setVisible(false); // Oculta la vista del pozo
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
