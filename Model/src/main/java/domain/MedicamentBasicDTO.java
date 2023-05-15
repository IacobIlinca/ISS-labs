package domain;

import java.io.Serializable;

public class MedicamentBasicDTO implements Serializable {
    private Integer medicamentId;
    private String denumire;
    private String detalii;

    public MedicamentBasicDTO(Integer medicamentId, String denumire, String detalii) {
        this.medicamentId = medicamentId;
        this.denumire = denumire;
        this.detalii = detalii;
    }

    public Integer getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Integer medicamentId) {
        this.medicamentId = medicamentId;
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
