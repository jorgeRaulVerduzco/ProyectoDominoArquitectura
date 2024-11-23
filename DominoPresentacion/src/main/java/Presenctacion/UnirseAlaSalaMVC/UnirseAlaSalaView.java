/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Jugador;
import Dominio.Sala;
import Presenctacion.Observer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author INEGI
 */
public class UnirseAlaSalaView extends javax.swing.JFrame implements Observer {

    private UnirseAlaSalaModel model;
    private DefaultTableModel tableModel;

    /**
     * Constructor de la clase que inicializa los componentes y configura la
     * tabla.
     */
    public UnirseAlaSalaView() {
        initComponents();
        configurarTabla(); // Configuramos la tabla después de inicializar los componentes

        actualizarTablaSalas();
    }

    /**
     * Asigna el modelo a la vista y registra la vista como observador del
     * modelo.
     *
     * @param model El modelo asociado a la vista.
     */
    public void setModel(UnirseAlaSalaModel model) {
        this.model = model;
        model.addObserver(this);
        actualizarTablaSalas();
    }

    /**
     * Configura el modelo y las propiedades de la tabla de salas. Define las
     * columnas y asigna renderizadores y editores personalizados.
     */
    private void configurarTabla() {
        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna del botón es editable
            }
        };

        // Definir las columnas de la tabla
        tableModel.addColumn("ID");
        tableModel.addColumn("Jugadores");
        tableModel.addColumn("Fichas");
        tableModel.addColumn("Acción");

        // Asignar el modelo de la tabla
        tblUnirseSala.setModel(tableModel);

        // Configurar la columna "Acción" para que tenga el botón de "Unirse"
        tblUnirseSala.getColumn("Acción").setCellRenderer(new BotonRenderer());
        tblUnirseSala.getColumn("Acción").setCellEditor(new BotonEditor(new JCheckBox()));
    }

    /**
     * Método que se ejecuta cuando el modelo notifica un cambio. Actualiza la
     * tabla de salas disponibles.
     */
    @Override
    public void update() {
        System.out.println("Actualizando vista: las salas han cambiado.");
        // Solo actualizar la tabla si hay salas disponibles
        if (model != null && !model.getSalasDisponibles().isEmpty()) {
            actualizarTablaSalas();
        }
    }

    /**
     * Actualiza los datos de la tabla de salas con la información más reciente
     * proporcionada por el modelo.
     */
  private void actualizarTablaSalas() {
    if (!SwingUtilities.isEventDispatchThread()) {
        SwingUtilities.invokeLater(this::actualizarTablaSalas);
        return;
    }

    try {
        List<Sala> salas = model != null ? model.getSalasDisponibles() : Collections.emptyList();
        System.out.println("Actualizando tabla con " + salas.size() + " salas");
        
        DefaultTableModel tableModel = (DefaultTableModel) tblUnirseSala.getModel();
        tableModel.setRowCount(0);
        
        for (Sala sala : salas) {
          
                Object[] rowData = new Object[]{
                    sala.getId(),
                    sala.getJugador().size() + "/" + sala.getCantJugadores(),
                    sala.getNumeroFichas(),
                    "Unirse"
                };
                tableModel.addRow(rowData);
                System.out.println("Agregada sala a la tabla: " + sala.getId());
            }
        
        
        tblUnirseSala.repaint();
    } catch (Exception e) {
        System.err.println("Error actualizando tabla de salas: " + e.getMessage());
        e.printStackTrace();
    }
}


    /**
     * Clase interna que define un renderizador de celdas para mostrar un botón
     * en la tabla.
     */
    class BotonRenderer extends JButton implements TableCellRenderer {

        public BotonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Unirse" : value.toString());
            return this;
        }
    }

    /**
     * Clase interna que define un editor de celdas para manejar las
     * interacciones con el botón en la tabla.
     */
    class BotonEditor extends DefaultCellEditor {

        private JButton button;
        private String label;
        private boolean isPushed;
        private int selectedRow;

        public BotonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            label = (value == null) ? "Unirse" : value.toString();
            button.setText(label);
            selectedRow = row;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Obtener el ID de la sala seleccionada
                String salaId = (String) tblUnirseSala.getValueAt(selectedRow, 0);
                model.unirseASala(salaId, null); // Aquí deberías pasar el jugador actual
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    public void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUnirseSala = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 204, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Salas disponibles");

        tblUnirseSala.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null},
                    {null, null},
                    {null, null},
                    {null, null}
                },
                new String[]{
                    "ID", "Unirse Boton"
                }
        ));
        jScrollPane1.setViewportView(tblUnirseSala);

        btnRegresar.setBackground(new java.awt.Color(204, 0, 0));
        btnRegresar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnRegresar.setText("Regresar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(224, 224, 224)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(51, 51, 51)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addContainerGap(52, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25, 25, 25))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
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
            java.util.logging.Logger.getLogger(UnirseAlaSalaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UnirseAlaSalaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UnirseAlaSalaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UnirseAlaSalaView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UnirseAlaSalaView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegresar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUnirseSala;
    // End of variables declaration//GEN-END:variables
}
