package service;

public interface ISpitalObserver {
    void updateComanda() throws SpitalException;
    void updateMedicament() throws SpitalException;
    void updateElementComanda() throws SpitalException;


}
