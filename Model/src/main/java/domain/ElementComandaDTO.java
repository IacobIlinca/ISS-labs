package domain;

import java.io.Serializable;

public class ElementComandaDTO implements Serializable {
    private Integer cantitate;
    private String observatii;

    public ElementComandaDTO(Integer cantitate, String observatii) {
        this.cantitate = cantitate;
        this.observatii = observatii;
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
}
