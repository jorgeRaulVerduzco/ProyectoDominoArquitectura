/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

/**
 *
 * @author INEGI
 */
public class FichaDTO {

    private int espacio1;
    private int espacio2;
    private boolean colocada = false;

    public FichaDTO() {
    }

    public boolean isColocada() {
        return colocada;
    }

    public void setColocada(boolean colocada) {
        this.colocada = colocada;
    }

    public FichaDTO(int espacio1, int espacio2) {
        this.espacio1 = espacio1;
        this.espacio2 = espacio2;
    }

    public int getEspacio1() {
        return espacio1;
    }

    public void setEspacio1(int espacio1) {
        this.espacio1 = espacio1;
    }

    public int getEspacio2() {
        return espacio2;
    }

    public void setEspacio2(int espacio2) {
        this.espacio2 = espacio2;
    }

    public boolean esMula() {
        return espacio1 == espacio2;
    }

    @Override
    public String toString() {
        return "Ficha{" + "espacio1=" + espacio1 + ", espacio2=" + espacio2 + '}';
    }

}
