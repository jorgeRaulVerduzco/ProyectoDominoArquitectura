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
public class PozoDTO {
      private List<FichaDTO> fichasPozo;

    public PozoDTO() {
        fichasPozo = new ArrayList<>();
    }

    public PozoDTO(List<FichaDTO> fichasPozo) {
        this.fichasPozo = fichasPozo;
    }

    public List<FichaDTO> getFichasPozo() {
        return fichasPozo;
    }

    public void setFichasPozo(List<FichaDTO> fichasPozo) {
        this.fichasPozo = fichasPozo;
    }

    @Override
    public String toString() {
        return "Pozo{" + "fichasPozo=" + fichasPozo + '}';
    }

}
