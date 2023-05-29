package domain;

import java.io.Serializable;

public class FarmacistDTO implements Serializable {
    private String email;
    private String parola;

    public FarmacistDTO(String email, String parola) {
        this.email = email;
        this.parola = parola;
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
}
