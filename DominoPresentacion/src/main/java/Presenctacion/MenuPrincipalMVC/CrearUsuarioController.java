/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import Presenctacion.Mediador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Serva
 */
public class CrearUsuarioController {
 private CrearUsuarioView view;
    private Mediador mediador;

    public CrearUsuarioController(CrearUsuarioView view) {
        this.view = view;
        this.view.setCreateUserListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
            }
        });
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    public void mostrarVista() {
        view.setVisible(true);
    }

    private void crearUsuario() {
        String nombre = view.getNombre();
        String avatar = view.getSelectedAvatar();

        if (nombre == null || nombre.trim().isEmpty() || avatar == null || avatar.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }
    // Crear el usuario
    CrearUsuarioModel usuario = new CrearUsuarioModel(nombre, avatar);
    
    // Notificar al mediador que se ha creado un usuario
    mediador.usuarioCreado(usuario);

      
    }
    public void ocultarVista() {
    view.setVisible(false);
}
}
