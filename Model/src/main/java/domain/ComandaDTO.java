package domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ComandaDTO implements Serializable {
    private String sectie;
    private StatusComanda statusComanda;
    private Integer zi;
    private Integer luna;
    private Integer an;
    private Integer ora;
    private Integer minut;
    private Medic creator;
    private Map<Medicament, ElementComandaDTO> medicamentElementComandDTOMap;

    public ComandaDTO(String sectie, StatusComanda statusComanda, Medic medic){
        this.sectie = sectie;
        this.statusComanda = statusComanda;
        this.creator = medic;
        this.medicamentElementComandDTOMap = new HashMap<>();
    }
    public ComandaDTO(String sectie, StatusComanda statusComanda, Integer zi, Integer luna, Integer an, Integer ora, Integer minut, Medic creator) {
        this.sectie = sectie;
        this.statusComanda = statusComanda;
        this.zi = zi;
        this.luna = luna;
        this.an = an;
        this.ora = ora;
        this.minut = minut;
        this.creator = creator;
        this.medicamentElementComandDTOMap = new HashMap<>();
    }

    public void addTomedicamentElementComandDTOMap(Medicament med, ElementComandaDTO el){
        this.medicamentElementComandDTOMap.put(med, el);
    }

    public String getSectie() {
        return sectie;
    }

    public void setSectie(String sectie) {
        this.sectie = sectie;
    }

    public StatusComanda getStatusComanda() {
        return statusComanda;
    }

    public void setStatusComanda(StatusComanda statusComanda) {
        this.statusComanda = statusComanda;
    }

    public Integer getZi() {
        return zi;
    }

    public void setZi(Integer zi) {
        this.zi = zi;
    }

    public Integer getLuna() {
        return luna;
    }

    public void setLuna(Integer luna) {
        this.luna = luna;
    }

    public Integer getAn() {
        return an;
    }

    public void setAn(Integer an) {
        this.an = an;
    }

    public Integer getOra() {
        return ora;
    }

    public void setOra(Integer ora) {
        this.ora = ora;
    }

    public Integer getMinut() {
        return minut;
    }

    public void setMinut(Integer minut) {
        this.minut = minut;
    }

    public Medic getCreator() {
        return creator;
    }

    public void setCreator(Medic creator) {
        this.creator = creator;
    }

    public Map<Medicament, ElementComandaDTO> getMedicamentElementComandDTOMap() {
        return medicamentElementComandDTOMap;
    }

    public void setMedicamentElementComandDTOMap(Map<Medicament, ElementComandaDTO> medicamentElementComandDTOMap) {
        this.medicamentElementComandDTOMap = medicamentElementComandDTOMap;
    }
}
