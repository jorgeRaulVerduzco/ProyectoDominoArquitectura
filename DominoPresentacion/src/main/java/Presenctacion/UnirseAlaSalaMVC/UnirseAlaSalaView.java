/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Jugador;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author INEGI
 */

import Dominio.Sala;
import Presenctacion.Observer;
import Presentacion.EsperaMVC.EsperaModel;
import Presentacion.EsperaMVC.EsperaView;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnirseAlaSalaView extends javax.swing.JFrame implements Observer {

    private UnirseAlaSalaController controller;
    private UnirseAlaSalaModel model;
    private DefaultTableModel tableModel;

    public UnirseAlaSalaView(UnirseAlaSalaModel model) {
        this.model = model;
        this.controller = new UnirseAlaSalaController(model, this);
        initComponents();
        configurarTabla();
        System.out.println("Tabla configurada. Columnas: " + tableModel.getColumnCount());
    }

    @Override
    public void update() {
        System.out.println("Vista: Notificación recibida del modelo. Actualizando tabla.");
        actualizarTablaSalas();
    }

    void actualizarTablaSalas() {
        try {
            List<Sala> salas = model.getSalasDisponibles();
            System.out.println("Vista: Actualizando tabla con " + (salas != null ? salas.size() : 0) + " salas.");

            tableModel.setRowCount(0); // Limpiar la tabla

            if (salas != null) {
                for (Sala sala : salas) {
                    // Convertir el ID a String explícitamente
                    String salaId = String.valueOf(sala.getId());
                    
                    // Solo agregamos las salas que tienen estado válido
                    if (sala.getEstado() != null && !sala.getEstado().isEmpty()) {
                        tableModel.addRow(new Object[]{
                            salaId,  // Usando String para el ID
                            sala.getJugador().size() + "/" + sala.getCantJugadores(),
                            sala.getNumeroFichas(),
                            "Unirse"
                        });
                    }
                }
            }

            tableModel.fireTableDataChanged(); // Refresca la tabla
        } catch (IOException ex) {
            Logger.getLogger(UnirseAlaSalaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addActualizarListener(ActionListener listener) {
        if (btnActualizar != null) {
            btnActualizar.addActionListener(listener);
        } else {
            System.err.println("Error: btnActualizar no está inicializado.");
        }
    }

    public void handleSalasDisponibles(List<Sala> salas) {
        System.out.println("Servidor: Recibida lista de salas. Cantidad: "
                + (salas != null ? salas.size() : "null"));
        // Actualizar el modelo
        model.actualizarSalas(salas);
    }

    private void configurarTabla() {
        
        UnirseAlaSalaController controller = new UnirseAlaSalaController(model, this);
        // Modelo de la tabla
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Solo la columna de "Acción" es editable
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return String.class; // ID ahora es String
                    case 1:
                        return String.class;  // Jugadores
                    case 2:
                        return Integer.class; // Fichas
                    case 3:
                        return String.class;  // Acción
                    default:
                        return Object.class;
                }
            }
        };

        // Configurar columnas
        tableModel.addColumn("ID");
        tableModel.addColumn("Jugadores");
        tableModel.addColumn("Fichas");
        tableModel.addColumn("Acción");

        tblUnirseSala.setModel(tableModel);

    
         // Modify how the button column is set up
        tblUnirseSala.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tblUnirseSala.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(controller));
    }

    // Custom ButtonEditor
    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private int selectedRow;
        private final UnirseAlaSalaController controller;
        private boolean isPushed = false;

        public ButtonEditor(UnirseAlaSalaController controller) {
            super(new JCheckBox());
            this.controller = controller;
            
            button = new JButton("Unirse");
            button.setOpaque(true);
            button.addActionListener(e -> {
                System.out.println("Button clicked for row: " + selectedRow);
                isPushed = true;
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, 
                boolean isSelected, int row, int column) {
            selectedRow = row;
            button.setText("Unirse");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                try {
                    // Get sala ID from the first column
                    String salaId = String.valueOf(tblUnirseSala.getValueAt(selectedRow, 0));
                    System.out.println("Attempting to join sala with ID: " + salaId);

                    // Call unirseASala method directly with a dummy Jugador if needed
                    // You'll need to replace this with your actual Jugador object
                    Jugador jugadorActual = new Jugador(); // Create or pass actual Jugador
                    controller.unirseASala(salaId, jugadorActual);

                    // Open waiting room or next view
                    EsperaView esperaView = new EsperaView(new EsperaModel());
                    esperaView.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Error processing 'Unirse' button: " + e.getMessage());
                }
                isPushed = false;
            }
            return "Unirse";
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
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
        btnActualizar = new javax.swing.JButton();  // Botón de actualizar

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

        btnActualizar.setBackground(new java.awt.Color(51, 153, 255));  // Color para el botón
        btnActualizar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnActualizar.setText("Actualizar Tabla");  // Texto del botón

        // Configurar el layout del panel
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(224, 224, 224)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE) // Botón de "Actualizar"
                                                        )
                                                )
                                        )
                                )
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
                                .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE) // Ubicamos el botón de actualizar
                                .addGap(18, 18, 18)
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
    }

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegresar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUnirseSala;
    // End of variables declaration//GEN-END:variables
}
