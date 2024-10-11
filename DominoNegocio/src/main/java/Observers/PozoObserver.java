/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Observers;

import Dominio.Ficha;
import java.util.List;

/**
 *
 * @author INEGI
 */
public interface PozoObserver {

    void actualizarPozo(List<Ficha> fichas);

}
