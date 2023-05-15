package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@javax.persistence.Entity
@Table( name = "Elemente_Comanda" )
public class ElementComanda extends Entity<Integer> implements Serializable {
    private Integer cantitate;
    private String observatii;

    @ManyToOne
    @JoinColumn(name = "medicament",referencedColumnName = "id", nullable = false)
    private Medicament medicament;

    @ManyToOne
    @JoinColumn(name = "comandaReferita",referencedColumnName = "id", nullable = false)
    private Comanda comandaReferita;

    public ElementComanda(Integer integer, Integer cantitate, String observatii, Medicament medicament, Comanda comadaReferita) {
        super(integer);
        this.cantitate = cantitate;
        this.observatii = observatii;
        this.medicament = medicament;
        this.comandaReferita = comadaReferita;
    }

    public ElementComanda() {

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

    public Medicament getMedicament() {
        return medicament;
    }

    public void setMedicament(Medicament medicament) {
        this.medicament = medicament;
    }

    public Comanda getComadaReferita() {
        return comandaReferita;
    }

    public void setComadaReferita(Comanda comadaReferita) {
        this.comandaReferita = comadaReferita;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ElementComanda that = (ElementComanda) o;
        return Objects.equals(cantitate, that.cantitate) && Objects.equals(observatii, that.observatii) && Objects.equals(medicament, that.medicament) && Objects.equals(comandaReferita, that.comandaReferita);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cantitate, observatii, medicament, comandaReferita);
    }


    @Override
    public String toString() {
        return "";
    }
}
