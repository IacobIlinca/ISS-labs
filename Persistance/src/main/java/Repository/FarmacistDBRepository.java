package Repository;

import domain.Farmacist;
import domain.Medic;
import domain.Spital;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class FarmacistDBRepository implements IFarmacistRepository {
    public final SessionFactory sessionFactory;

    public FarmacistDBRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Farmacist getAccount(String email, String parola) {
        Farmacist farmacist = null;
        Spital spital = null;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            var query =
                    session.createNativeQuery("select * from Farmacisti where email=? and parola=?");
            query.setParameter(1, email);
            query.setParameter(2, parola);


            List<Objects[]> farma = query.list();
            for (Object[] a : farma) {

                var query2 = session.createNativeQuery("SELECT * FROM Spitale where id=?");
                query2.setParameter(1, a[4]);
                List<Object[]> spitale = query2.list();
                for(Object[] s:spitale){
                    spital = new Spital((Integer) s[0], s[1].toString());
                }
                farmacist = new Farmacist((Integer) a[0], a[2].toString(), spital, a[1].toString(), a[3].toString());
                farmacist.setId((Integer) a[0]);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e);
        }
        return farmacist;
    }

    @Override
    public boolean add(Farmacist elem) {
        return false;
    }

    @Override
    public void delete(Farmacist elem) {

    }

    @Override
    public void update(Farmacist elem, Integer id) {

    }

    @Override
    public Farmacist findById(Integer id) {
        return null;
    }

    @Override
    public List<Farmacist> findAll() {
        return null;
    }

    @Override
    public Collection<Farmacist> getAll() {
        return null;
    }
}
