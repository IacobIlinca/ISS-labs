package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;
import javax.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "Medici" )
public class Medic extends domain.Entity<Integer> implements Serializable {
    private String nume;
    private String sectie;

    private String email;
    private String parola;


    @ManyToOne
    @JoinColumn(name = "spital",referencedColumnName = "id", nullable = false)
    private Spital spital;

    @OneToMany(targetEntity=Comanda.class,mappedBy = "id")
    private List<Comanda> comenziPlasate;

    public Medic(Integer integer, String nume, String sectie, Spital spital, String email, String parola) {
        super(integer);
        this.nume = nume;
        this.sectie = sectie;
        this.spital = spital;
        this.comenziPlasate = new ArrayList<Comanda>();
        this.email = email;
        this.parola = parola;
    }

    public Medic() {

    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getSectie() {
        return sectie;
    }

    public void setSectie(String sectie) {
        this.sectie = sectie;
    }

    public Spital getSpital() {
        return spital;
    }

    public void setSpital(Spital spital) {
        this.spital = spital;
    }


    public List<Comanda> getComenziPlasate() {
        return comenziPlasate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medic medic = (Medic) o;
        return Objects.equals(nume, medic.nume) && Objects.equals(sectie, medic.sectie) && Objects.equals(spital, medic.spital) && Objects.equals(comenziPlasate, medic.comenziPlasate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nume, sectie, spital, comenziPlasate);
    }

    public void basicAddToComenziPlasate(Comanda comanda){
        this.comenziPlasate.add(comanda);
    }

    public void addToComenziPlasate(Comanda comanda)
    {
        this.basicAddToComenziPlasate(comanda);
        comanda.setCreator(this);
    }

    public void basicRemoveFromComenziPlasate(Comanda comanda)
    {
        this.comenziPlasate.remove(comanda);
    }
    public void removeFromComenziPlasate(Comanda comanda)
    {
        this.basicRemoveFromComenziPlasate(comanda);
        comanda.setCreator(null);
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

    @Override
    public String toString() {
        return "";
    }
}
