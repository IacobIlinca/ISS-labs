package domain;

import java.io.Serializable;

public class MedicaamentBasicDTO implements Serializable {
    private Integer id;
    private String denumire;
    private String detalii;

    public MedicaamentBasicDTO(Integer id, String denumire, String detalii) {
        this.id = id;
        this.denumire = denumire;
        this.detalii = detalii;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
