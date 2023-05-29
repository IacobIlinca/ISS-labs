package domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

public enum StatusComanda implements Serializable {
    IN_ASTEPTARE(0),
    ONORATA(1);

    private final int value;

    StatusComanda(int value) {
        this.value = value;
    }

    public static StatusComanda valueOf(int value){
        Optional<StatusComanda> stat = Arrays.stream(values()).filter(x->x.value == value).findFirst();
        return stat.orElse(null);
    }
}
