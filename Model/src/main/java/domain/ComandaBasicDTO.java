package domain;

import java.io.Serializable;

public class ComandaBasicDTO implements Serializable {
    private Integer idComanda;
    private String sectie;
    private StatusComanda statusComanda;

    public ComandaBasicDTO(Integer idComanda, String sectie, StatusComanda statusComanda) {
        this.idComanda = idComanda;
        this.sectie = sectie;
        this.statusComanda = statusComanda;
    }

    public Integer getIdComanda() {
        return idComanda;
    }

    public void setIdComanda(Integer idComanda) {
        this.idComanda = idComanda;
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
}
