package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.persistence.Entity
@Table(name = "Spitale")
public class Spital extends Entity<Integer> implements Serializable {
    private String nume;

    @OneToMany(targetEntity = Medic.class, mappedBy = "id")
    private List<Medic> medici;

    @OneToMany(targetEntity = Farmacist.class, mappedBy = "id")
    private List<Farmacist> farmacisti;

    public Spital(Integer integer, String nume) {
        super(integer);
        this.nume = nume;
        this.medici = new ArrayList<Medic>();
        this.farmacisti = new ArrayList<Farmacist>();
    }

    public Spital() {

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public List<Medic> getMedici() {
        return medici;
    }


    public List<Farmacist> getFarmacisti() {
        return farmacisti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Spital spital = (Spital) o;
        return Objects.equals(nume, spital.nume) && Objects.equals(medici, spital.medici) && Objects.equals(farmacisti, spital.farmacisti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, medici, farmacisti);
    }

    public void basicAddToMedici(Medic medic) {
        this.medici.add(medic);
    }

    public void addToMedici(Medic medic) {
        this.basicAddToMedici(medic);
        medic.setSpital(this);
    }

    public void basicAddToFarmacisti(Farmacist farmacist) {
        this.farmacisti.add(farmacist);
    }

    public void addToFarmacisti(Farmacist farmacist) {
        this.basicAddToFarmacisti(farmacist);
        farmacist.setSpital(this);
    }

    void basicRemoveFromMedici(Medic medic) {
        this.medici.remove(medic);
    }

    public void removeFromMedici(Medic medic) {
        this.basicRemoveFromMedici(medic);
        medic.setSpital(null);
    }

    public void basicRemoveFromFarmacisti(Farmacist farmacist) {
        this.farmacisti.remove(farmacist);
    }

    public void removeFromFarmacisti(Farmacist farmacist) {
        this.basicRemoveFromFarmacisti(farmacist);
        farmacist.setSpital(null);
    }

    @Override
    public String toString() {
        return "";
    }
}
