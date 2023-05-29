package domain;

import java.io.Serializable;

public class ElemCmd implements Serializable {
    private Integer id;
    private Integer cantitate;
    private String observatii;
    private String numeMedicament;
    private Integer comandaReferita;

    public ElemCmd(Integer id, Integer cantitate, String observatii, String numeMedicament, Integer comandaReferita) {
        this.id = id;
        this.cantitate = cantitate;
        this.observatii = observatii;
        this.numeMedicament = numeMedicament;
        this.comandaReferita = comandaReferita;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCantitate() {
        return cantitate;
    }

    public void setCantitate(Integer cantitate) {
        this.cantitate = cantitate;
    }

    public String getObservatii() {
        return observatii;
    }

    public void setObservatii(String observatii) {
        this.observatii = observatii;
    }

    public String getNumeMedicament() {
        return numeMedicament;
    }

    public void setNumeMedicament(String numeMedicament) {
        this.numeMedicament = numeMedicament;
    }

    public Integer getComandaReferita() {
        return comandaReferita;
    }

    public void setComandaReferita(Integer comandaReferita) {
        this.comandaReferita = comandaReferita;
    }
}
