/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Dominio.Sala;
import EventoJuego.Evento;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author INEGI
 */
public class GestorSalas {
       private static GestorSalas instance;
    private List<Sala> salasActivas;

    private GestorSalas() {
        salasActivas = new CopyOnWriteArrayList<>();
        cargarSalasDesdeArchivo();
    }

    public static synchronized GestorSalas getInstance() {
        if (instance == null) {
            instance = new GestorSalas();
        }
        return instance;
    }

    public synchronized void agregarSala(Sala sala) {
        if (!salasActivas.contains(sala)) {
            salasActivas.add(sala);
            guardarSalasEnArchivo();
            notificarNuevaSala(sala);
        }
    }

    public List<Sala> obtenerSalasActivas() {
        return new ArrayList<>(salasActivas);
    }

    private void cargarSalasDesdeArchivo() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("Sala.ser"))) {
            salasActivas = (List<Sala>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            salasActivas = new CopyOnWriteArrayList<>();
        }
    }

    private void guardarSalasEnArchivo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("Sala.ser"))) {
            oos.writeObject(salasActivas);
        } catch (IOException e) {
            System.err.println("Error guardando salas: " + e.getMessage());
        }
    }

    private void notificarNuevaSala(Sala sala) {
        // Lógica para notificar a todos los clientes conectados sobre la nueva sala
        Evento evento = new Evento("NUEVA_SALA");
        evento.agregarDato("sala", sala);
        // Enviar a todos los clientes
        
    }
    
    public synchronized void limpiarSalas() {
    salasActivas.clear(); // Limpiar todas las salas
    guardarSalasEnArchivo(); // Actualizar el archivo
}
}
