package Repository;

import domain.Farmacist;

public interface IFarmacistRepository  extends Repository<Farmacist, Integer> {
    public Farmacist getAccount(String email, String parola);
}
