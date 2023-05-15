package Repository;

import domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class MedicamentDBRepository implements IMedicamentRepository {
    public final SessionFactory sessionFactory;

    public MedicamentDBRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public boolean add(Medicament elem) {
        return false;
    }

    @Override
    public void delete(Medicament elem) {

    }

    @Override
    public void update(Medicament elem, Integer id) {

    }

    @Override
    public Medicament findById(Integer id) {
        return null;
    }

    @Override
    public List<Medicament> findAll() {
        Medicament medicament = null;
        List<Medicament> medicamentFindAll = new ArrayList<>();
        try (
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Medicamente ");

            List<Objects[]> medicamente = query.list();
            for (Object[] a : medicamente) {

                medicament = new Medicament((Integer) a[0], a[1].toString(), a[2].toString());
                medicament.setId((Integer) a[0]);
                medicamentFindAll.add(medicament);
            }

        }
        return medicamentFindAll;
    }


    @Override
    public Collection<Medicament> getAll() {
        return null;
    }

    @Override
    public Medicament findByDenumire(String denumire) {
        Medicament medicament = null;
        try (
                Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Medicamente where denumire=? ");
            query.setParameter(1, denumire);

            List<Objects[]> medicamente = query.list();
            for (Object[] a : medicamente) {

                medicament = new Medicament((Integer) a[0], a[1].toString(), a[2].toString());
                medicament.setId((Integer) a[0]);
            }

        }
        return medicament;
    }
}
