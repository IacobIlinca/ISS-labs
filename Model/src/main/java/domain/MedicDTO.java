package domain;

import java.io.Serializable;

public class MedicDTO implements Serializable {
    private String email;
    private String parola;

    private String sectie;
    public MedicDTO(String email, String parola, String sectie) {
        this.email = email;
        this.parola = parola;
        this.sectie = sectie;
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

    public String getSectie() {
        return sectie;
    }

    public void setSectie(String sectie) {
        this.sectie = sectie;
    }
}
