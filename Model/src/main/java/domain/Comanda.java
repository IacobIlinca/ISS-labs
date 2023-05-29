package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@javax.persistence.Entity
@Table( name = "Comenzi" )
public class Comanda extends Entity<Integer> implements Serializable {
    private String sectie;
    private StatusComanda statusComanda;

    @ManyToOne()
    @JoinColumn(name = "data",referencedColumnName = "id", nullable = false)
    private DateTime data;

    @ManyToOne
    @JoinColumn(name = "creator",referencedColumnName = "id", nullable = false)
    private Medic creator;

    @ManyToOne
    @JoinColumn(name = "farmacist",referencedColumnName = "id")
    private Farmacist farmacist;

    @OneToMany(targetEntity=ElementComanda.class, mappedBy = "id")
    private List<ElementComanda> elementeComanda;

    public Comanda(Integer integer, String sectie, StatusComanda statusComanda, DateTime data, Medic creator) {
        super(integer);
        this.sectie = sectie;
        this.statusComanda = statusComanda;
        this.data = data;
        this.creator = creator;
        this.farmacist = null;
        this.elementeComanda = new ArrayList<ElementComanda>();
    }

    public Comanda() {

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

    public DateTime getData() {
        return data;
    }

    public void setData(DateTime data) {
        this.data = data;
    }

    public Medic getCreator() {
        return creator;
    }

    public void setCreator(Medic creator) {
        this.creator = creator;
    }

    public Farmacist getFarmacist() {
        return farmacist;
    }

    public void setFarmacist(Farmacist farmacist) {
        this.farmacist = farmacist;
    }

    public List<ElementComanda> getElementeComanda() {
        return elementeComanda;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Comanda comanda = (Comanda) o;
        return Objects.equals(sectie, comanda.sectie) && statusComanda == comanda.statusComanda && Objects.equals(data, comanda.data) && Objects.equals(creator, comanda.creator) && Objects.equals(farmacist, comanda.farmacist) && Objects.equals(elementeComanda, comanda.elementeComanda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sectie, statusComanda, data, creator, farmacist, elementeComanda);
    }

    public void basicAddToElementeComanda( ElementComanda elementComanda){
        this.elementeComanda.add(elementComanda);
    }

    public void addToElementeComanda(ElementComanda elementComanda)
    {
        this.basicAddToElementeComanda(elementComanda);
        elementComanda.setComadaReferita(this);
    }

    public void basicRemoveFromElementeComanda(ElementComanda elementComanda)
    {
        this.elementeComanda.remove(elementComanda);
    }

    public void removeFromElementeComanda(ElementComanda elementComanda){
        this.basicRemoveFromElementeComanda(elementComanda);
        elementComanda.setComadaReferita(null);
    }

    @Override
    public String toString() {
        return "";
    }
}
