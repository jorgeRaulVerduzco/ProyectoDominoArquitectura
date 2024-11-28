/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Presenctacion.Observer;
import Server.Server;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.TableColumn;

/**
 *
 * @author INEGI
 */
public class UnirseAlaSalaView extends javax.swing.JFrame implements Observer {

   private UnirseAlaSalaModel model;  // Modelo de la vista
    private DefaultTableModel tableModel;

    // Constructor donde se pasa el modelo
    public UnirseAlaSalaView(UnirseAlaSalaModel model) {
        this.model = model;  // Asegúrate de asignar el modelo correctamente
        initComponents();
        configurarTabla();
        System.out.println("Tabla configurada. Columnas: " + tableModel.getColumnCount());
   
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
                // Solo agregamos las salas que tienen estado válido
                if (sala.getEstado() != null && !sala.getEstado().isEmpty()) {
                    tableModel.addRow(new Object[]{
                        sala.getId(),
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
    btnActualizar.addActionListener(listener); // Vincula el evento al botón de actualización
}



    // En tu clase de manejo de eventos del servidor
    public void handleSalasDisponibles(List<Sala> salas) {
        System.out.println("Servidor: Recibida lista de salas. Cantidad: "
                + (salas != null ? salas.size() : "null"));
        // Actualizar el modelo
        model.actualizarSalas(salas);
    }

    private void configurarTabla() {
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
                        return Integer.class; // ID
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
    }

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
        
        public void addActualizarListener(ActionListener listener) {
    btnActualizar.addActionListener(listener); // Vincula el evento al botón de actualización
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
                // Asegúrate de obtener el ID como Integer
                Integer salaId = (Integer) tblUnirseSala.getValueAt(selectedRow, 0);
                if (model != null) {
                    // Llama al método unirseASala con el ID correcto
                    model.unirseASala(salaId, null);
                }
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
                                                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)  // Botón de "Actualizar"
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
                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)  // Ubicamos el botón de actualizar
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
