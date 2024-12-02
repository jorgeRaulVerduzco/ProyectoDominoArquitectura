/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

/**
 *
 * @author caarl
 */
public class ClienteInfo {
    private String ip;
    private int puerto;

    // Constructor
    public ClienteInfo(String ip, int puerto) {
        this.ip = ip;
        this.puerto = puerto;
    }

    // Getters y setters
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPuerto() {
        return puerto;
    }

    public void setPuerto(int puerto) {
        this.puerto = puerto;
    }

    @Override
    public String toString() {
        return "ClienteInfo{" +
                "ip='" + ip + '\'' +
                ", puerto=" + puerto +
                '}';
    }
}
