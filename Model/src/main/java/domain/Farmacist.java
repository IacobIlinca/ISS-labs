package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@javax.persistence.Entity
@Table( name = "Farmacisti" )
public class Farmacist extends Entity<Integer> implements Serializable {
    private String nume;
    private String email;
    private String parola;

    @ManyToOne
    @JoinColumn(name = "spital",referencedColumnName = "id", nullable = false)
    private Spital spital;

    @OneToMany(targetEntity=Comanda.class,mappedBy = "id")
    private List<Comanda> comenziOnorate;

    public Farmacist(Integer integer, String nume, Spital spital, String email, String parola) {
        super(integer);
        this.nume = nume;
        this.spital = spital;
        this.comenziOnorate = new ArrayList<Comanda>();
        this.email = email;
        this.parola = parola;
    }

    public Farmacist() {

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public Spital getSpital() {
        return spital;
    }

    public void setSpital(Spital spital) {
        this.spital = spital;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public List<Comanda> getComenziOnorate() {
        return comenziOnorate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Farmacist farmacist = (Farmacist) o;
        return Objects.equals(nume, farmacist.nume) && Objects.equals(spital, farmacist.spital) && Objects.equals(comenziOnorate, farmacist.comenziOnorate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, spital, comenziOnorate);
    }

    public void basicAddToComenziOnorate(Comanda comanda) {
        this.comenziOnorate.add(comanda);
    }

    public void addToComenziOnorate(Comanda comanda) {
        this.basicAddToComenziOnorate(comanda);
        comanda.setFarmacist(this);
    }

    public void basicRemoveFromComenziOnorate(Comanda comanda) {
        this.comenziOnorate.remove(comanda);
    }

    public void removeFromComenziPlasate(Comanda comanda) {
        this.basicRemoveFromComenziOnorate(comanda);
        comanda.setCreator(null);
    }

    @Override
    public String toString() {
        return "";
    }
}
