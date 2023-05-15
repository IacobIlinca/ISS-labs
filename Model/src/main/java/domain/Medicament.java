package domain;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@javax.persistence.Entity
@Table( name = "Medicamente" )
public class Medicament extends Entity<Integer> implements Serializable {
    private String denumire;
    private String detalii;

    @OneToMany(targetEntity=ElementComanda.class,mappedBy = "id")
    private List<ElementComanda> elementeComanda;


    public Medicament(Integer integer, String denumire, String detalii) {
        super(integer);
        this.denumire = denumire;
        this.detalii = detalii;
        this.elementeComanda = new ArrayList<ElementComanda>();

    }

    public Medicament() {

    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public String getDetalii() {
        return detalii;
    }

    public void setDetalii(String detalii) {
        this.detalii = detalii;
    }

    public List<ElementComanda> getElementeComanda() {
        return elementeComanda;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Medicament that = (Medicament) o;
        return Objects.equals(denumire, that.denumire) && Objects.equals(detalii, that.detalii) && Objects.equals(elementeComanda, that.elementeComanda)
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), denumire, detalii, elementeComanda);
    }

    public void basicAddToElementeComanda( ElementComanda elementComanda){
        this.elementeComanda.add(elementComanda);
    }

    public void addToElementeComanda(ElementComanda elementComanda)
    {
        this.basicAddToElementeComanda(elementComanda);
        elementComanda.setMedicament(this);
    }

    public void basicRemoveFromElementeComanda(ElementComanda elementComanda)
    {
        this.elementeComanda.remove(elementComanda);
    }

    public void removeFromElementeComanda(ElementComanda elementComanda){
        this.basicRemoveFromElementeComanda(elementComanda);
        elementComanda.setMedicament(null);
    }

    @Override
    public String toString() {
        return "";
    }
}
