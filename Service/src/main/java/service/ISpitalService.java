package service;

import domain.*;

import java.util.List;

public interface ISpitalService {

    void logoutMedic(Medic medic, ISpitalObserver client) throws SpitalException;
    void logoutFarmacist(Farmacist medic, ISpitalObserver client) throws SpitalException;

    Medic loginMedic(MedicDTO medicDTO, ISpitalObserver client) throws SpitalException;
    Farmacist loginFarmacist(FarmacistDTO farmacistDTO, ISpitalObserver client) throws SpitalException;

    List<Comanda> findAllComenzi(ISpitalObserver client) throws SpitalException;
    List<Medicament> findAllMedicamente(ISpitalObserver client) throws SpitalException;
    List<Medic> findAllMedici(ISpitalObserver client) throws SpitalException;
    List<ElementComanda> findAllElementeComanda(ISpitalObserver client) throws SpitalException;

    Medicament findByDenumire(String denumire, ISpitalObserver client) throws SpitalException;

    void addComanda(ComandaDTO comanda, ISpitalObserver client) throws SpitalException;
    void addMedicament(Medicament med, ISpitalObserver client) throws SpitalException;
    void stergeComanda(Comanda comanda, ISpitalObserver client) throws SpitalException;
    void stergeMedicament(Medicament medicament, ISpitalObserver client) throws SpitalException;
    void updateMedicament(Medicament medicament, ISpitalObserver client) throws SpitalException;
    void updateElementComanda(ElementComanda elem, ISpitalObserver client) throws SpitalException;
    void updateComanda(Comanda cmd, ISpitalObserver client) throws SpitalException;

}
