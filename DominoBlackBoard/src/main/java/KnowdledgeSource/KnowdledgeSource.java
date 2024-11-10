/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package KnowdledgeSource;

import EventoJuego.Evento;
import java.net.Socket;

/**
 *
 * @author INEGI
 */
public interface KnowdledgeSource {

    boolean puedeProcesar(Evento evento);

    void procesarEvento(Socket cliente, Evento evento);
}
