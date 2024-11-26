/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import Mediadores.CrearUsuarioMediador;
import Presenctacion.CrearSalaMVC.CrearSalaView;
import Presenctacion.Mediador;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 *
 * @author Serva
 */
public class CrearUsuarioView extends javax.swing.JFrame {

    private javax.swing.JComboBox<String> comboBoxAvatares;
    private CrearUsuarioMediador mediadorCrearUsuario;  // Mediador para la lógica de usuario
    private Mediador mediador; // Mediador general, si es necesario

    private CrearSalaView crearSalaView; // Asegúrate de tener esta vista para la creación de sala

    /**
     * Creates new form CrearUsuarioView
     */
    public CrearUsuarioView() {
        this.mediadorCrearUsuario = new CrearUsuarioMediador();

        initComponents();
        initComboBox();

    }

    private void initComboBox() {
        comboBoxAvatares = new javax.swing.JComboBox<>();
        comboBoxAvatares.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Avatar 1", "Avatar 2", "Avatar 3"}));

        comboBoxAvatares.setRenderer(new javax.swing.ListCellRenderer<String>() {
            @Override
            public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = new JLabel();
                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(list.getSelectionBackground());
                    label.setForeground(list.getSelectionForeground());
                } else {
                    label.setBackground(list.getBackground());
                    label.setForeground(list.getForeground());
                }

                ImageIcon icon = null;
                switch (index) {
                    case 0:
                        icon = new ImageIcon("C:\\Users\\INEGI\\Documents\\NetBeansProjects\\ProyectoDominoArquitectura\\DominoPresentacion\\src\\imagenes\\sticker-png-login-icon-system-administrator-user-user-profile-icon-design-avatar-face-head.png");
                        break;
                    case 1:
                        icon = new ImageIcon("C:\\Users\\INEGI\\Documents\\NetBeansProjects\\ProyectoDominoArquitectura\\DominoPresentacion\\src\\imagenes\\pngtree-user-vector-avatar-png-image_4830521.jpg");
                        break;
                    case 2:
                        icon = new ImageIcon("C:\\Users\\INEGI\\Documents\\NetBeansProjects\\ProyectoDominoArquitectura\\DominoPresentacion\\src\\imagenes\\png-transparent-female-avatar-girl-face-woman-user-flat-classy-users-icon.png");
                        break;
                }

                if (icon != null) {
                    Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    label.setIcon(new ImageIcon(img));
                } else {
                    label.setIcon(null);
                }

                label.setText(value);
                return label;
            }
        });

        getContentPane().add(comboBoxAvatares, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 160, -1));
    }
 public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        CheckBoxMembresia = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        BtnJugar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 153, 0));
        setMaximumSize(new java.awt.Dimension(389, 506));
        setMinimumSize(new java.awt.Dimension(389, 506));
        setPreferredSize(new java.awt.Dimension(389, 506));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Nombre");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Crear Usuario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 120, 20));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, 210, 30));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Avatar");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Activar Membresia");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 370, -1, 20));
        getContentPane().add(CheckBoxMembresia, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 370, 20, 20));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Domino");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        BtnJugar.setText("Jugar");
        BtnJugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnJugarActionPerformed(evt);
            }
        });
        getContentPane().add(BtnJugar, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 440, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BtnJugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnJugarActionPerformed
     // Obtener los datos de la vista
    String nombre = txtNombre.getText();
    System.out.println("[DEBUG] Nombre ingresado: " + nombre);

    String avatar = (String) comboBoxAvatares.getSelectedItem();
    System.out.println("[DEBUG] Avatar seleccionado: " + avatar);

    // Validación inicial
    if (nombre == null || nombre.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingresa un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("[ERROR] Nombre vacío.");
        return;
    }

    if (avatar == null || avatar.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, selecciona un avatar.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("[ERROR] Avatar no seleccionado.");
        return;
    }

    // Crear el modelo de usuario
    CrearUsuarioModel usuario = new CrearUsuarioModel(nombre, avatar);
    System.out.println("[DEBUG] Modelo de usuario creado: " + usuario);

     CrearUsuarioController usuarioCTL = new CrearUsuarioController(this);
     
    // Llamar al método crearUsuario del controlador
    try {
        if (mediador != null) {
            mediador.usuarioCreado(usuario);
        } else {
            System.out.println("Error: mediador es null");
        }
        
        
    } catch (Exception ex) {
        System.out.println("[ERROR] Ocurrió un error al crear el usuario: " + ex.getMessage());
        ex.printStackTrace();
    }
    }//GEN-LAST:event_BtnJugarActionPerformed
    // Mostrar vista de Crear Sala
    private void mostrarCrearSala(CrearUsuarioModel usuario) {
        if (crearSalaView != null) {
            System.out.println("Mostrando vista de Crear Sala...");
            crearSalaView.setVisible(true);
        } else {
            System.out.println("crearSalaView is null.");
        }
    }

    public String getNombre() {
        return txtNombre.getText(); // Obtener el texto del campo de nombre
    }

    public String getSelectedAvatar() {
        return (String) comboBoxAvatares.getSelectedItem(); // Obtener el avatar seleccionado
    }

    public void addCreateUserListener(ActionListener listener) {
        BtnJugar.addActionListener(listener);
    }

    public void setCreateUserListener(ActionListener actionListener) {
        BtnJugar.addActionListener(actionListener);
    }

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
            java.util.logging.Logger.getLogger(CrearUsuarioView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CrearUsuarioView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CrearUsuarioView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CrearUsuarioView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CrearUsuarioView().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnJugar;
    private javax.swing.JCheckBox CheckBoxMembresia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables

}
