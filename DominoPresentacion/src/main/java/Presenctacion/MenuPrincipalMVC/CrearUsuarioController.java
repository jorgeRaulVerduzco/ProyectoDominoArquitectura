/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Serva
 */
public class CrearUsuarioController {

    private CrearUsuarioView view;

    public CrearUsuarioController(CrearUsuarioView view) {
        this.view = view;

        // Añadir listener al botón Jugar
        this.view.addCreateUserListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los datos del usuario
                String nombre = view.getNombre();
                String avatar = view.getSelectedAvatar();

                // Validaciones
                if (nombre == null || nombre.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Por favor, ingrese un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }

                if (avatar == null || avatar.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Por favor, seleccione un avatar.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; 
                }

                // Si todos los datos son válidos, ir al menú principal
                MenuPrincipalView menuView = new MenuPrincipalView(); 
                menuView.setVisible(true);
                view.dispose();
            }
        });
    }
}
