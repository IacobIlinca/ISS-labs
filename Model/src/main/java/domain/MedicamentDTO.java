package domain;

import java.io.Serializable;

public class MedicamentDTO implements Serializable {
    private String denumire;
    private String detalii;

    public MedicamentDTO(String denumire, String detalii) {
        this.denumire = denumire;
        this.detalii = detalii;
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
