/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class TableroDTO {
    private List<FichaDTO> fichasTablero;

    public TableroDTO() {
        fichasTablero = new ArrayList<>();
    }

    public List<FichaDTO> getFichasTablero() {
        return fichasTablero;
    }

    public void setFichasTablero(List<FichaDTO> fichasTablero) {
        this.fichasTablero = fichasTablero;
    }
}
