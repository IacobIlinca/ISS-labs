package Repository;

import domain.Medic;
import domain.Medicament;

public interface IMedicamentRepository extends Repository<Medicament, Integer> {

    Medicament findByDenumire(String denumire);
}
