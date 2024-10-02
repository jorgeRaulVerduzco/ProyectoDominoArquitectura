/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;

/**
 *
 * @author INEGI
 */
public class ServicioFicha {
   public boolean esMula(Ficha ficha) {
        return ficha.getEspacio1() == ficha.getEspacio2();
    }

    public int sumarPuntosFicha(Ficha ficha) {
        return ficha.getEspacio1() + ficha.getEspacio2();
    }
}
