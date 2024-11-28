/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author INEGI
 */
public class ConfiguracionSocket {
    // Instancia única de la clase (Singleton)
    private static ConfiguracionSocket instance;
    
    // Puerto por defecto
    private int puertoSocket = 51114;
    
    // Constructor privado para prevenir instanciación directa
    private ConfiguracionSocket() {}
    
    // Método estático para obtener la instancia única
    public static synchronized ConfiguracionSocket getInstance() {
        if (instance == null) {
            instance = new ConfiguracionSocket();
        }
        return instance;
    }
    
    // Método para establecer el puerto
    public void setPuertoSocket(int puerto) {
        // Validar rango de puertos
        if (puerto < 1024 || puerto > 65535) {
            throw new IllegalArgumentException("Puerto inválido. Debe estar entre 1024 y 65535.");
        }
        this.puertoSocket = puerto;
    }
    
    // Método para obtener el puerto actual
    public int getPuertoSocket() {
        return puertoSocket;
    }
    
    // Método para crear un socket con el puerto configurado
    public Socket crearSocket() throws IOException {
        return new Socket("localhost", puertoSocket);
    }
}
