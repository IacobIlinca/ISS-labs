package Repository;

import domain.Medic;

public interface IMedicRepository extends Repository<Medic, Integer> {
    public Medic getAccount(String email, String parola, String sectie);
}
