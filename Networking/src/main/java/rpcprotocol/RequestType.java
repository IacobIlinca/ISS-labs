package rpcprotocol;

import java.io.Serializable;

public enum RequestType implements Serializable {
    LOGIN_MEDIC, LOGIN_FARMACIST, LOGOUT_MEDIC, LOGOUT_FARMACIST, ADD_COMANDA, FIND_ALL_COMENZI, FIND_ALL_MEDICAMENTE, FIND_ALL_MEDICI, FIND_MEDICAMENT_BY_DENUMIRE;
}
