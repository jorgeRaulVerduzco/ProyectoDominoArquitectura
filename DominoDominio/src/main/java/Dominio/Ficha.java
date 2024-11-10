/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

/**
 *
 * @author INEGI
 */
public class Ficha {

    private int espacio1;
    private int espacio2;
    private boolean colocada = false;
    private String orientacion;//vertical u horizontal

    /**
     * Constructor por defecto de la clase Ficha. Inicializa una ficha sin
     * valores para sus espacios.
     */
    public Ficha() {
    }

    public boolean isColocada() {
        return colocada;
    }

    public void setColocada(boolean colocada) {
        this.colocada = colocada;
    }

    /**
     * Constructor sobrecargado de la clase Ficha. Permite crear una ficha
     * especificando los valores de los dos espacios.
     *
     * @param espacio1 Valor del primer lado de la ficha.
     * @param espacio2 Valor del segundo lado de la ficha.
     */
    public Ficha(int espacio1, int espacio2) {
        this.espacio1 = espacio1;
        this.espacio2 = espacio2;
    }

    /**
     * Método getter para obtener el valor del primer lado de la ficha.
     *
     * @return El valor de espacio1.
     */
    public int getEspacio1() {
        return espacio1;
    }

    /**
     * Método setter para modificar el valor del primer lado de la ficha.
     *
     * @param espacio1 El nuevo valor para el primer lado de la ficha.
     */
    public void setEspacio1(int espacio1) {
        this.espacio1 = espacio1;
    }

    /**
     * Método getter para obtener el valor del segundo lado de la ficha.
     *
     * @return El valor de espacio2.
     */
    public int getEspacio2() {
        return espacio2;
    }

    /**
     * Método setter para modificar el valor del segundo lado de la ficha.
     *
     * @param espacio2 El nuevo valor para el segundo lado de la ficha.
     */
    public void setEspacio2(int espacio2) {
        this.espacio2 = espacio2;
    }

    /**
     * Método que verifica si la ficha es una "mula" (es decir, si ambos lados
     * de la ficha tienen el mismo valor).
     *
     * @return true si la ficha es una mula, false en caso contrario.
     */
    public boolean esMula() {
        return espacio1 == espacio2;
    }

    /**
     * Método para regresar la orientacion de la ficha puede ser vertical u
     * horizontal
     *
     * @return orientacion
     */
    public String getOrientacion() {
        return orientacion;
    }

    /**
     * Método para poner la orientacion de la ficha puede ser vertical u
     * horizontal
     *
     * @param orientacion
     */
    public void setOrientacion(String orientacion) {
        this.orientacion = orientacion;
    }

    /**
     * Método sobreescrito para generar una representación en formato String de
     * la ficha, que incluye los valores de ambos lados (espacio1 y espacio2).
     *
     * @return Una cadena con el formato "Ficha{espacio1=valor, espacio2=valor,
     * orientacion =valor}".
     */
    @Override
    public String toString() {
        return "Ficha{" + "espacio1=" + espacio1 + ", espacio2=" + espacio2 + ", colocada=" + colocada + ", orientacion=" + orientacion + '}';
    }

}
